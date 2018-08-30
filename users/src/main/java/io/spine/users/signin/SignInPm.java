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
import io.spine.users.user.CreateUser;
import io.spine.users.user.CreateUserVBuilder;
import io.spine.users.user.UserAggregate;
import io.spine.users.user.UserAggregateRepository;
import io.spine.users.user.UserCreated;

import java.util.Optional;

import static io.spine.server.tuple.EitherOfTwo.withA;
import static io.spine.server.tuple.EitherOfTwo.withB;
import static io.spine.users.User.Status.ACTIVE;
import static io.spine.users.c.signin.FailureReason.IDENTITY_NOT_FOUND;
import static io.spine.users.c.signin.FailureReason.SIGN_IN_NOT_ALLOWED;
import static io.spine.users.c.signin.FailureReason.USER_IDENTITY_MISMATCH;
import static io.spine.users.signin.SignIn.Status.AWAITING_USER_CREATION;
import static io.spine.users.signin.SignIn.Status.COMPLETED;
import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * The basic sign-in process. // TODO: what is basic?
 *
 * <p>The process is aimed to work with remote identity providers.
 *
 * <p>On every sign-in, the process manager checks:
 * <ul>
 * <li>if the user exists in the system;
 * <li>if the user is still active at the remote identity provider.
 * </ul>
 *
 * <p>If the user is not yet in the system, the process manager fetches the user profile from the
 * remote identity provider and creates the new {@link User} aggregate.
 *
 * <p>If the user is active, the process ends with {@link SignInFinished} event. // TODO: fix this
 * Otherwise, the process ends with {@link SignInFailed} event. If necessary,
 * the process manager synchronizes the user status between the remote identity provider
 * and the {@link User} aggregate state.
 * <p>
 * TODO: mention IdentityProvider. Create NoOp identity provider? Is that the correct behavior?
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
    EitherOfTwo<FinishSignIn, CreateUser> handle(SignIn command, CommandContext context) {
        UserId id = command.getId();
        UserAuthIdentity identity = command.getIdentity();
        IdentityProvider identityProvider = identityProviders.get(identity.getProviderId());

        SignInVBuilder builder = getBuilder().setId(id)
                                             .setIdentity(identity);

        if (!identityProvider.hasIdentity(identity)) {
            return withA(finishWithError(IDENTITY_NOT_FOUND));
        } else if (!identityProvider.signInAllowed(identity)) {
            return withA(finishWithError(SIGN_IN_NOT_ALLOWED));
        }

        Optional<UserAggregate> user = userRepository.find(id);
        if (!user.isPresent()) {
            builder.setStatus(AWAITING_USER_CREATION);
            return withB(createUserCommand(identityProvider));
        }
        if (!identityBelongsToUser(user.get(), identity)) {
            return withA(finishWithError(USER_IDENTITY_MISMATCH));
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
