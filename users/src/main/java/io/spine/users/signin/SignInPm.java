/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin;

import io.spine.core.CommandContext;
import io.spine.core.EventContext;
import io.spine.core.UserId;
import io.spine.server.command.Assign;
import io.spine.server.command.Command;
import io.spine.server.procman.ProcessManager;
import io.spine.server.tuple.EitherOf2;
import io.spine.users.IdentityProviderBridge;
import io.spine.users.IdentityProviderBridgeFactory;
import io.spine.users.PersonProfile;
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
import io.spine.users.user.UserPart;
import io.spine.users.user.UserPartRepository;
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
 *     <li>an {@linkplain IdentityProviderBridge identity provider} is aware of the given
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
    private IdentityProviderBridgeFactory identityProviders;

    /**
     * @see ProcessManager#ProcessManager(Object)
     */
    SignInPm(UserId id) {
        super(id);
    }

    void setIdentityProviderFactory(IdentityProviderBridgeFactory identityProviderFactory) {
        this.identityProviders = identityProviderFactory;
    }

    void setUserRepository(UserPartRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Command
    EitherOf2<FinishSignIn, CreateUser> handle(SignUserIn command, CommandContext context) {
        UserId id = command.getId();
        Identity identity = command.getIdentity();
        Optional<IdentityProviderBridge> identityProviderOptional =
                identityProviders.get(identity.getProviderId());

        if (!identityProviderOptional.isPresent()) {
            return withA(finishWithError(UNSUPPORTED_IDENTITY));
        }

        SignInVBuilder builder = getBuilder().setId(id)
                                             .setIdentity(identity);
        IdentityProviderBridge identityProvider = identityProviderOptional.get();
        if (!identityProvider.hasIdentity(identity)) {
            return withA(finishWithError(UNKNOWN_IDENTITY));
        }
        if (!identityProvider.isSignInAllowed(identity)) {
            return withA(finishWithError(SIGN_IN_NOT_AUTHORIZED));
        }

        Optional<UserPart> user = userRepository.find(id);
        if (!user.isPresent()) {
            builder.setStatus(AWAITING_USER_AGGREGATE_CREATION);
            return withB(createUser(identityProvider));
        }
        if (!identityBelongsToUser(user.get(), identity)) {
            return withA(finishWithError(UNKNOWN_IDENTITY));
        }

        builder.setStatus(COMPLETED);
        return withA(finishSuccessfully());
    }

    @Command
    Optional<SignUserIn> on(UserCreated event, EventContext context) {
        if (awaitsUserCreation()) {
            return of(signIn(getBuilder().getIdentity()));
        }
        return empty();
    }

    @Assign
    EitherOf2<SignInSuccessful, SignInFailed> handle(FinishSignIn command,
                                                     CommandContext context) {
        UserId id = command.getId();
        Identity identity = getBuilder().getIdentity();
        if (command.getSuccessful()) {
            return withA(signInSuccessful(id, identity));
        } else {

            return withB(signInFailed(id, identity, command.getFailureReason()));
        }
    }

    @Assign
    SignOutCompleted handle(SignUserOut command, CommandContext context) {
        return signOutCompleted(command.getId());
    }

    private CreateUser createUser(IdentityProviderBridge identityProvider) {
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
