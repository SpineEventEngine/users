/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin;

import com.google.common.annotations.VisibleForTesting;
import io.spine.core.CommandContext;
import io.spine.core.EventContext;
import io.spine.core.UserId;
import io.spine.server.command.Assign;
import io.spine.server.command.Command;
import io.spine.server.procman.ProcessManager;
import io.spine.server.tuple.EitherOfTwo;
import io.spine.users.User;
import io.spine.users.UserAuthIdentity;
import io.spine.users.UserProfile;
import io.spine.users.c.signin.FailureReason;
import io.spine.users.c.signin.FinishSignIn;
import io.spine.users.c.signin.FinishSignInVBuilder;
import io.spine.users.c.signin.SignIn;
import io.spine.users.c.signin.SignInCompleted;
import io.spine.users.c.signin.SignInCompletedVBuilder;
import io.spine.users.c.signin.SignInFailed;
import io.spine.users.c.signin.SignInFailedVBuilder;
import io.spine.users.c.signin.SignInVBuilder;
import io.spine.users.c.signin.SignUserIn;
import io.spine.users.c.signin.SignUserInVBuilder;
import io.spine.users.user.CreateUser;
import io.spine.users.user.CreateUserVBuilder;
import io.spine.users.user.UserAggregate;
import io.spine.users.user.UserAggregateRepository;
import io.spine.users.user.UserCreated;

import java.util.Optional;

import static io.spine.server.tuple.EitherOfTwo.withA;
import static io.spine.server.tuple.EitherOfTwo.withB;
import static io.spine.users.User.Status.ACTIVE;
import static io.spine.users.c.signin.FailureReason.SIGN_IN_NOT_ALLOWED;
import static io.spine.users.c.signin.FailureReason.UNKNOWN_IDENTITY;
import static io.spine.users.c.signin.SignIn.Status.AWAITING_USER_CREATION;
import static io.spine.users.c.signin.SignIn.Status.COMPLETED;
import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * The process of a sign-in using the given {@linkplain UserAuthIdentity authentication identity}.
 *
 * <p>This process manager covers a straightforward sign-in scenario:
 *
 * <ol>
 *     <li>{@link SignUserIn} command initializes the sign-in process.
 *     <li>If a {@linkplain UserAggregate user} with the given {@linkplain UserId ID} already exists,
 *         {@link SignInCompleted} event is generated in response.
 *     <li>Otherwise, the process manager creates a user and then attempts to
 *         {@linkplain SignUserIn sign user in} again.
 * </ol>
 *
 * <p>To sign a user in, the process manager should ensure the following:
 *
 * <ul>
 *     <li>an {@linkplain IdentityProvider identity provider} is aware of the given authentication
 *         identity;
 *     <li>an identity provider allows the user to sign in (e.g. the opposite would be if the user
 *         account was suspended);
 *     <li>the given authentication identity is {@linkplain io.spine.users.user.AuthIdentityAdded associated}
 *          with the user.
 * </ul>
 *
 * <p>If one of the checks fails, the process is {@linkpla SignInFailed completed} immediately with
 * the corresponding error.
 *
 * @author Vladyslav Lubenskyi
 */
public class SignInPm extends ProcessManager<UserId, SignIn, SignInVBuilder> {

    private UserAggregateRepository userRepository;
    private IdentityProviderFactory identityProviders;

    /**
     * @see ProcessManager#ProcessManager(Object)
     */
    @VisibleForTesting
    public SignInPm(UserId id) {
        super(id);
    }

    void setIdentityProviderFactory(IdentityProviderFactory identityProviderFactory) {
        this.identityProviders = identityProviderFactory;
    }

    void setUserRepository(UserAggregateRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Command
    EitherOfTwo<FinishSignIn, CreateUser> handle(SignUserIn command, CommandContext context) {
        UserId id = command.getId();
        UserAuthIdentity identity = command.getIdentity();
        IdentityProvider identityProvider = identityProviders.get(identity.getProviderId());

        SignInVBuilder builder = getBuilder().setId(id)
                                             .setIdentity(identity);

        if (!identityProvider.hasIdentity(identity)) {
            return withA(finishWithError(UNKNOWN_IDENTITY));
        } else if (!identityProvider.signInAllowed(identity)) {
            return withA(finishWithError(SIGN_IN_NOT_ALLOWED));
        }

        Optional<UserAggregate> user = userRepository.find(id);
        if (!user.isPresent()) {
            builder.setStatus(AWAITING_USER_CREATION);
            return withB(createUserCommand(identityProvider));
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
            return of(signInCommand());
        }
        return empty();
    }

    @Assign
    EitherOfTwo<SignInCompleted, SignInFailed> handle(FinishSignIn command) {
        if (command.getSuccessfull()) {
            return withA(signInCompleted());
        } else {
            return withB(signInFailed(command.getFailureReason()));
        }
    }

    private boolean awaitsUserCreation() {
        return getBuilder().getStatus() == AWAITING_USER_CREATION;
    }

    private static boolean identityBelongsToUser(UserAggregate user, UserAuthIdentity identity) {
        User userState = user.getState();
        if (userState.getPrimaryAuthIdentity()
                     .equals(identity)) {
            return true;
        }
        return userState.getAuthIdentityList()
                        .contains(identity);
    }

    private FinishSignIn finishSuccessfully() {
        return FinishSignInVBuilder.newBuilder()
                                   .setId(getId())
                                   .setSuccessfull(true)
                                   .build();
    }

    private FinishSignIn finishWithError(FailureReason failureReason) {
        return FinishSignInVBuilder.newBuilder()
                                   .setId(getId())
                                   .setSuccessfull(false)
                                   .setFailureReason(failureReason)
                                   .build();
    }

    private SignUserIn signInCommand() {
        SignInVBuilder builder = getBuilder();
        return SignUserInVBuilder.newBuilder()
                                 .setId(builder.getId())
                                 .setIdentity(builder.getIdentity())
                                 .build();
    }

    private SignInCompleted signInCompleted() {
        return SignInCompletedVBuilder.newBuilder()
                                      .setId(getId())
                                      .setIdentity(getBuilder().getIdentity())
                                      .build();
    }

    private SignInFailed signInFailed(FailureReason reason) {
        return SignInFailedVBuilder.newBuilder()
                                   .setId(getId())
                                   .setIdentity(getBuilder().getIdentity())
                                   .setFailureReason(reason)
                                   .build();
    }

    private CreateUser createUserCommand(IdentityProvider identityProvider) {
        UserProfile profile = identityProvider.fetchUserProfile(getBuilder().getIdentity());
        String displayName = profile.getEmail()
                                    .getValue();
        return CreateUserVBuilder.newBuilder()
                                 .setId(getId())
                                 .setDisplayName(displayName)
                                 .setPrimaryIdentity(getBuilder().getIdentity())
                                 .setProfile(profile)
                                 .setStatus(ACTIVE)
                                 .build();
    }
}
