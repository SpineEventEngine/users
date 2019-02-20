/*
 * Copyright 2019, TeamDev. All rights reserved.
 *
 * Redistribution and use in source and/or binary forms, with or without
 * modification, must retain the above copyright notice and the following
 * disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.spine.users.server.signin;

import io.spine.core.UserId;
import io.spine.server.command.Assign;
import io.spine.server.command.Command;
import io.spine.server.procman.ProcessManager;
import io.spine.server.tuple.EitherOf2;
import io.spine.users.PersonProfile;
import io.spine.users.server.Directory;
import io.spine.users.server.DirectoryFactory;
import io.spine.users.server.user.UserPart;
import io.spine.users.server.user.UserPartRepository;
import io.spine.users.signin.SignIn;
import io.spine.users.signin.SignInFailureReason;
import io.spine.users.signin.SignInVBuilder;
import io.spine.users.signin.command.FinishSignIn;
import io.spine.users.signin.command.FinishSignInVBuilder;
import io.spine.users.signin.command.SignUserIn;
import io.spine.users.signin.command.SignUserInVBuilder;
import io.spine.users.signin.command.SignUserOut;
import io.spine.users.signin.event.SignInFailed;
import io.spine.users.signin.event.SignInFailedVBuilder;
import io.spine.users.signin.event.SignInSuccessful;
import io.spine.users.signin.event.SignInSuccessfulVBuilder;
import io.spine.users.signin.event.SignOutCompleted;
import io.spine.users.signin.event.SignOutCompletedVBuilder;
import io.spine.users.user.Identity;
import io.spine.users.user.User;
import io.spine.users.user.command.CreateUser;
import io.spine.users.user.command.CreateUserVBuilder;
import io.spine.users.user.event.UserCreated;

import java.util.Optional;

import static io.spine.server.tuple.EitherOf2.withA;
import static io.spine.server.tuple.EitherOf2.withB;
import static io.spine.users.signin.SignIn.Status.AWAITING_USER_AGGREGATE_CREATION;
import static io.spine.users.signin.SignIn.Status.COMPLETED;
import static io.spine.users.signin.SignInFailureReason.SIGN_IN_NOT_AUTHORIZED;
import static io.spine.users.signin.SignInFailureReason.UNKNOWN_IDENTITY;
import static io.spine.users.signin.SignInFailureReason.UNSUPPORTED_IDENTITY;
import static io.spine.users.user.User.Status.ACTIVE;
import static io.spine.users.user.UserNature.PERSON;
import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * The process of a sign-in using the given {@linkplain Identity authentication identity}.
 *
 * <p>This process manager covers a straightforward sign-in scenario:
 *
 * <ol>
 *     <li>{@link SignUserIn} command initializes the sign-in process.
 *     <li>If a {@linkplain UserPart user} with the given {@linkplain UserId ID} already exists
 *         and all checks pass {@link SignInSuccessful} event is generated in response.
 *     <li>Otherwise, the process manager creates a {@link UserPart} and then attempts to
 *         {@linkplain SignUserIn sign user in} again.
 * </ol>
 *
 * <p>To sign a user in, the process manager ensures the following:
 *
 * <ul>
 *     <li>an {@linkplain Directory identity provider} is aware of the given
 *         authentication identity;
 *     <li>an identity provider authorize the user to sign in (e.g. the opposite would be if the user
 *         account was suspended);
 *     <li>the given authentication identity is associated with the user (that is, serves as the
 *         primary or a secondary authentication identity).
 * </ul>
 *
 * <p>If one of the checks fails, the process is {@linkplain SignInFailed completed} immediately.
 *
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings("OverlyCoupledClass") // It is OK for a process manager.
public class SignInPm extends ProcessManager<UserId, SignIn, SignInVBuilder> {

    private UserPartRepository userRepository;
    private DirectoryFactory directories;

    /**
     * @see ProcessManager#ProcessManager(Object)
     */
    SignInPm(UserId id) {
        super(id);
    }

    void setDirectoryFactory(DirectoryFactory directoryFactory) {
        this.directories = directoryFactory;
    }

    void setUserRepository(UserPartRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Command
    EitherOf2<FinishSignIn, CreateUser> handle(SignUserIn command) {
        UserId id = command.getId();
        Identity identity = command.getIdentity();
        Optional<Directory> directoryOptional =
                directories.get(identity.getProviderId());
        if (!directoryOptional.isPresent()) {
            return withA(finishWithError(UNSUPPORTED_IDENTITY));
        }

        SignInVBuilder builder = getBuilder().setId(id)
                                             .setIdentity(identity);
        Directory directory = directoryOptional.get();
        if (!directory.hasIdentity(identity)) {
            return withA(finishWithError(UNKNOWN_IDENTITY));
        }
        if (!directory.isSignInAllowed(identity)) {
            return withA(finishWithError(SIGN_IN_NOT_AUTHORIZED));
        }

        Optional<UserPart> user = userRepository.find(id);
        if (!user.isPresent()) {
            builder.setStatus(AWAITING_USER_AGGREGATE_CREATION);
            return withB(createUser(directory));
        }
        if (!identityBelongsToUser(user.get(), identity)) {
            return withA(finishWithError(UNKNOWN_IDENTITY));
        }

        builder.setStatus(COMPLETED);
        return withA(finishSuccessfully());
    }

    @Command
    Optional<SignUserIn> on(UserCreated event) {
        if (awaitsUserCreation()) {
            return of(signIn(getBuilder().getIdentity()));
        }
        return empty();
    }

    @Assign
    EitherOf2<SignInSuccessful, SignInFailed> handle(FinishSignIn command) {
        UserId id = command.getId();
        Identity identity = getBuilder().getIdentity();
        if (command.getSuccessful()) {
            return withA(signInSuccessful(id, identity));
        } else {

            return withB(signInFailed(id, identity, command.getFailureReason()));
        }
    }

    @Assign
    SignOutCompleted handle(SignUserOut command) {
        return signOutCompleted(command.getId());
    }

    private CreateUser createUser(Directory identityProvider) {
        PersonProfile profile = identityProvider.fetchProfile(getBuilder().getIdentity());
        return createUser(getBuilder().getIdentity(), profile);
    }

    private boolean awaitsUserCreation() {
        return getBuilder().getStatus() == AWAITING_USER_AGGREGATE_CREATION;
    }

    private static boolean identityBelongsToUser(UserPart user, Identity identity) {
        User userState = user.getState();
        if (userState.getPrimaryIdentity()
                     .equals(identity)) {
            return true;
        }
        return userState.getSecondaryIdentityList()
                        .contains(identity);
    }

    FinishSignIn finishWithError(SignInFailureReason failureReason) {
        return FinishSignInVBuilder
                .newBuilder()
                .setId(getId())
                .setSuccessful(false)
                .setFailureReason(failureReason)
                .build();
    }

    FinishSignIn finishSuccessfully() {
        return FinishSignInVBuilder
                .newBuilder()
                .setId(getId())
                .setSuccessful(true)
                .build();
    }

    SignUserIn signIn(Identity identity) {
        return SignUserInVBuilder
                .newBuilder()
                .setId(getId())
                .setIdentity(identity)
                .build();
    }

    CreateUser createUser(Identity identity, PersonProfile profile) {
        String displayName = profile.getEmail()
                                    .getValue();
        return CreateUserVBuilder
                .newBuilder()
                .setId(getId())
                .setDisplayName(displayName)
                .setPrimaryIdentity(identity)
                .setProfile(profile)
                .setStatus(ACTIVE)
                .setExternalDomain(identity.getDomain())
                .setNature(PERSON)
                .build();
    }

    SignInSuccessful signInSuccessful(UserId id, Identity identity) {
        return SignInSuccessfulVBuilder
                .newBuilder()
                .setId(id)
                .setIdentity(identity)
                .build();
    }

    SignInFailed signInFailed(UserId id, Identity identity,
                              SignInFailureReason reason) {
        return SignInFailedVBuilder
                .newBuilder()
                .setId(id)
                .setIdentity(identity)
                .setFailureReason(reason)
                .build();
    }

    SignOutCompleted signOutCompleted(UserId id) {
        return SignOutCompletedVBuilder
                .newBuilder()
                .setId(id)
                .build();
    }
}
