/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin;

import com.google.common.annotations.VisibleForTesting;
import com.google.protobuf.Empty;
import com.google.protobuf.Message;
import io.spine.users.signin.CommandSentVBuilder;
import io.spine.users.signin.RemoteIdentitySignInFailedVBuilder;
import io.spine.users.signin.RemoteIdentitySignInFinishedVBuilder;
import io.spine.users.signin.RemoteIdentitySignInVBuilder;
import io.spine.users.signin.identity.*;
import io.spine.users.User;
import io.spine.users.UserAuthIdentity;
import io.spine.users.UserProfile;
import io.spine.users.user.*;
import io.spine.core.CommandContext;
import io.spine.core.EventContext;
import io.spine.core.React;
import io.spine.core.UserId;
import io.spine.protobuf.AnyPacker;
import io.spine.server.command.Assign;
import io.spine.server.procman.ProcessManager;
import io.spine.server.tuple.EitherOfTwo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static io.spine.users.signin.RemoteIdentitySignIn.Status.*;
import static io.spine.users.User.Status.ACTIVE;
import static io.spine.protobuf.AnyPacker.pack;

// @formatter:off
/**
 * The basic sign-in process.
 *
 * <p>The process is aimed to work with remote identity providers.
 *
 * <p>On every sign-in, the process manager checks:
 * <ul>
 *     <li>if the user exists in the system;
 *     <li>if the user is still active at the remote identity provider.
 * </ul>
 *
 * <p>If the user is not yet in the system, the process manager fetches the user profile from the
 * remote identity provider and creates the new {@link User} aggregate.
 *
 * <p>If the user is active, the process ends with {@link RemoteIdentitySignInFinished} event.
 * Otherwise, the process ends with {@link RemoteIdentitySignInFailed} event. If necessary,
 * the process manager synchronizes the user status between the remote identity provider
 * and the {@link User} aggregate state.
 *
 * <p>IMPORTANT: This process manager delegates the remote identity provider querying to the
 * {@link RemoteIdentityProviderProcMan} process manager. Please, make sure to have the
 * {@link RemoteIdentityProviderProcMan} implementation registered in the bounded context.
 *
 * @author Vladyslav Lubenskyi
 */
// @formatter:on
public class RemoteIdentitySignInProcMan
        extends ProcessManager<UserId, RemoteIdentitySignIn, RemoteIdentitySignInVBuilder> {

    private UserAggregateRepository userRepository;

    /**
     * @see ProcessManager#ProcessManager(Object)
     */
    @VisibleForTesting
    public RemoteIdentitySignInProcMan(UserId id) {
        super(id);
    }

    void setUserRepository(UserAggregateRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Assign
    @SuppressWarnings("Guava")
        // Spine Java 7 API.
    CommandSent handle(SignInRemoteIdentity command, CommandContext context) {
        logCommand(command);

        UserId id = command.getId();
        UserAuthIdentity identity = command.getIdentity();

        getBuilder().setId(id)
                    .setIdentity(identity);

        com.google.common.base.Optional<UserAggregate> user = userRepository.find(id);

        if (user.isPresent()) {
            log().info("The user already exists. Proceeding to check the user status.");
            getBuilder().setStatus(USER_STATUS_CHECK);
            User userState = user.get()
                                 .getState();
            ensureIdentity(userState, identity);
            CheckUserStatus checkStatusCommand = checkStatusCommand(id, identity);
            return commandSent(id, checkStatusCommand);
        } else {
            log().info("The user doesn't exist. Proceeding to create the user.");
            getBuilder().setStatus(USER_PROFILE_FETCHING);
            FetchUserDetails fetchUserDetails = fetchUserDetails(id, identity);
            return commandSent(id, fetchUserDetails);
        }
    }

    @React
    CommandSent on(UserDetailsFetched event) {
        logEvent(event);
        getBuilder().setStatus(USER_CREATING);
        CreateUser command = createUserCommand(event);
        return commandSent(event.getUserId(), command);
    }

    @React
    EitherOfTwo<CommandSent, Empty> on(UserCreated event, EventContext context) {
        logEvent(event);
        UserId id = event.getId();
        if (getState().getStatus() == USER_CREATING) {
            CheckUserStatus checkStatusCommand =
                    checkStatusCommand(id, getState().getIdentity());
            return EitherOfTwo.withA(commandSent(id, checkStatusCommand));
        }
        return EitherOfTwo.withB(Empty.getDefaultInstance());
    }

    @React
    EitherOfTwo<RemoteIdentitySignInFinished, RemoteIdentitySignInFailed>
    on(UserStatusChecked event) {
        logEvent(event);
        if (event.getStatus() == ACTIVE) {
            getBuilder().setStatus(SIGNED_IN);
            RemoteIdentitySignInFinished signedIn = signInFinished(event.getUserId(),
                                                                   getState().getIdentity());
            return EitherOfTwo.withA(signedIn);
        } else {
            getBuilder().setStatus(FAILED);
            RemoteIdentitySignInFailed signInFailed = signInFailed(event.getUserId(),
                                                                   getState().getIdentity());
            return EitherOfTwo.withB(signInFailed);
        }
    }

    @React
    Empty on(CommandSent event, EventContext context) {
        logEvent(event);
        Message command = AnyPacker.unpack(event.getCommand());
        newRouterFor(event, context.getCommandContext()).add(command)
                                                        .routeAll();
        return Empty.getDefaultInstance();
    }

    private static RemoteIdentitySignInFinished signInFinished(UserId id,
                                                               UserAuthIdentity identity) {
        RemoteIdentitySignInFinished result =
                RemoteIdentitySignInFinishedVBuilder.newBuilder()
                                                    .setId(id)
                                                    .setIdentity(identity)
                                                    .build();
        return result;
    }

    private static CommandSent commandSent(UserId id, Message command) {
        return CommandSentVBuilder.newBuilder()
                                  .setId(id)
                                  .setCommand(pack(command))
                                  .build();
    }

    private static CreateUser createUserCommand(UserDetailsFetched event) {
        UserProfile profile = event.getProfile();
        return CreateUserVBuilder.newBuilder()
                                 .setId(event.getUserId())
                                 .setDisplayName(profile.getEmail()
                                                        .getValue())
                                 .setPrimaryIdentity(event.getIdentity())
                                 .setProfile(profile)
                                 .addAllAttribute(event.getAttributeList())
                                 .setStatus(ACTIVE)
                                 .build();
    }

    private static RemoteIdentitySignInFailed signInFailed(UserId id, UserAuthIdentity identity) {
        RemoteIdentitySignInFailed result =
                RemoteIdentitySignInFailedVBuilder.newBuilder()
                                                  .setId(id)
                                                  .setIdentity(identity)
                                                  .build();
        return result;
    }

    private static void ensureIdentity(User user, UserAuthIdentity identityToCheck) {
        if (user.getPrimaryAuthIdentity()
                .equals(identityToCheck)) {
            return;
        }
        Optional<UserAuthIdentity> foundIdentity =
                user.getAuthIdentityList()
                    .stream()
                    .filter(identity -> identity.equals(identityToCheck))
                    .findFirst();
        if (!foundIdentity.isPresent()) {
            throw new IllegalArgumentException("Unknown user identity");
        }
    }

    private static FetchUserDetails fetchUserDetails(UserId id, UserAuthIdentity identity) {
        return FetchUserDetailsVBuilder.newBuilder()
                                       .setId(identity.getProviderId())
                                       .setUserId(id)
                                       .setIdentity(identity)
                                       .build();
    }

    private static CheckUserStatus checkStatusCommand(UserId id, UserAuthIdentity identity) {
        return CheckUserStatusVBuilder.newBuilder()
                                      .setId(identity.getProviderId())
                                      .setIdentity(identity)
                                      .setUserId(id)
                                      .build();
    }

    private void logEvent(Message event) {
        log().info("'{}' event came to {}. For ID: {}.", event.getClass()
                                                              .getSimpleName(),
                   getClass().getSimpleName(), getId().getValue());
    }

    private void logCommand(Message event) {
        log().info("Asked to '{}'. For ID: {}.", event.getClass()
                                                      .getSimpleName(), getId().getValue());
    }

    private static Logger log() {
        return LogSingleton.INSTANCE.value;
    }

    private enum LogSingleton {
        INSTANCE;
        @SuppressWarnings("NonSerializableFieldInSerializableClass")
        private final Logger value = LoggerFactory.getLogger(RemoteIdentitySignInProcMan.class);
    }
}
