/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.signin;

import io.spine.core.CommandContext;
import io.spine.core.EventContext;
import io.spine.core.UserId;
import io.spine.server.command.Assign;
import io.spine.server.command.Command;
import io.spine.server.procman.ProcessManager;
import io.spine.server.tuple.EitherOfTwo;
import io.spine.users.Identity;
import io.spine.users.PersonProfile;
import io.spine.users.c.IdentityProviderBridge;
import io.spine.users.c.IdentityProviderBridgeFactory;
import io.spine.users.c.user.CreateUser;
import io.spine.users.c.user.User;
import io.spine.users.c.user.UserCreated;
import io.spine.users.c.user.UserPart;
import io.spine.users.c.user.UserPartRepository;

import java.util.Optional;

import static io.spine.server.tuple.EitherOfTwo.withA;
import static io.spine.server.tuple.EitherOfTwo.withB;
import static io.spine.users.c.signin.SignIn.Status.AWAITING_USER_AGGREGATE_CREATION;
import static io.spine.users.c.signin.SignIn.Status.COMPLETED;
import static io.spine.users.c.signin.SignInFailureReason.SIGN_IN_NOT_AUTHORIZED;
import static io.spine.users.c.signin.SignInFailureReason.UNKNOWN_IDENTITY;
import static io.spine.users.c.signin.SignInFailureReason.UNSUPPORTED_IDENTITY;
import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * The process of a sign-in using the given {@linkplain Identity authentication identity}.
 *
 * <p>This process manager covers a straightforward sign-in scenario:
 *
 * <ol>
 *     <li>{@link SignUserIn} command initializes the sign-in process.
 *     <li>If a {@linkplain UserAggregate user} with the given {@linkplain UserId ID} already exists
 *         and all checks pass {@link SignInSuccessful} event is generated in response.
 *     <li>Otherwise, the process manager creates a {@link UserAggregate} and then attempts to
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
    EitherOfTwo<FinishSignIn, CreateUser> handle(SignUserIn command, CommandContext context) {
        UserId id = command.getId();
        Identity identity = command.getIdentity();
        Optional<IdentityProviderBridge> identityProviderOptional =
                identityProviders.get(identity.getProviderId());

        if (!identityProviderOptional.isPresent()) {
            return withA(commands().finishWithError(UNSUPPORTED_IDENTITY));
        }

        SignInVBuilder builder = getBuilder().setId(id)
                                             .setIdentity(identity);
        IdentityProviderBridge identityProvider = identityProviderOptional.get();
        if (!identityProvider.hasIdentity(identity)) {
            return withA(commands().finishWithError(UNKNOWN_IDENTITY));
        }
        if (!identityProvider.signInAllowed(identity)) {
            return withA(commands().finishWithError(SIGN_IN_NOT_AUTHORIZED));
        }

        Optional<UserAggregate> user = userRepository.find(id);
        if (!user.isPresent()) {
            builder.setStatus(AWAITING_USER_AGGREGATE_CREATION);
            return withB(createUser(identityProvider));
        }
        if (!identityBelongsToUser(user.get(), identity)) {
            return withA(commands().finishWithError(UNKNOWN_IDENTITY));
        }

        builder.setStatus(COMPLETED);
        return withA(commands().finishSuccessfully());
    }

    @Command
    Optional<SignUserIn> on(UserCreated event, EventContext context) {
        if (awaitsUserCreation()) {
            return of(commands().signIn(getBuilder().getIdentity()));
        }
        return empty();
    }

    @Assign
    EitherOfTwo<SignInSuccessful, SignInFailed> handle(FinishSignIn command,
                                                       CommandContext context) {
        UserId id = command.getId();
        Identity identity = getBuilder().getIdentity();
        if (command.getSuccessful()) {
            return withA(events(context).completeSignIn(id, identity));
        } else {

            return withB(events(context).failSignIn(id, identity, command.getFailureReason()));
        }
    }

    @Assign
    SignOutCompleted handle(SignUserOut command, CommandContext context) {
        return events(context).signOut(command.getId());
    }

    private CreateUser createUser(IdentityProviderBridge identityProvider) {
        PersonProfile profile = identityProvider.fetchPersonProfile(getBuilder().getIdentity());
        return commands().createUser(getBuilder().getIdentity(), profile);
    }

    private boolean awaitsUserCreation() {
        return getBuilder().getStatus() == AWAITING_USER_AGGREGATE_CREATION;
    }

    private static boolean identityBelongsToUser(UserAggregate user, Identity identity) {
        User userState = user.getState();
        if (userState.getPrimaryIdentity()
                     .equals(identity)) {
            return true;
        }
        return userState.getSecondaryIdentityList()
                        .contains(identity);
    }

    private static SignInEventFactory events(CommandContext context) {
        return SignInEventFactory.instance(context);
    }

    private SignInCommandFactory commands() {
        return SignInCommandFactory.instance(getId());
    }
}
