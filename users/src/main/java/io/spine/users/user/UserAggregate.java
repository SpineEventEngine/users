/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user;

import com.google.common.annotations.VisibleForTesting;
import com.google.protobuf.Any;
import com.google.protobuf.Message;
import io.spine.core.CommandContext;
import io.spine.core.EventContext;
import io.spine.core.UserId;
import io.spine.server.aggregate.Aggregate;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.server.event.React;
import io.spine.users.GroupId;
import io.spine.users.RoleId;
import io.spine.users.User;
import io.spine.users.UserAuthIdentity;
import io.spine.users.UserVBuilder;
import io.spine.users.signin.SignInCompleted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static io.spine.util.Exceptions.newIllegalStateException;

/**
 * A user of the application.
 *
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings({"OverlyCoupledClass", "ClassWithTooManyMethods"}) // It is OK for aggregate.
public class UserAggregate extends Aggregate<UserId, User, UserVBuilder> {

    /**
     * @see Aggregate#Aggregate(Object)
     */
    @VisibleForTesting
    public UserAggregate(UserId id) {
        super(id);
    }

    // TODO:2018-08-27:vladyslav.lubenskyi: https://github.com/SpineEventEngine/users/issues/13
    @Assign
    UserCreated handle(CreateUser command, CommandContext context) {
        logCommand(command);
        return events(context).create(command);
    }

    @Assign
    UserMoved handle(MoveUser command, CommandContext context) {
        logCommand(command);
        return events(context).changeParent(command, getState().getParentEntity());
    }

    @Assign
    UserJoinedGroup handle(JoinGroup command, CommandContext context) {
        logCommand(command);
        return events(context).joinGroup(command);
    }

    @Assign
    UserLeftGroup handle(LeaveGroup command, CommandContext context) {
        logCommand(command);
        return events(context).leaveGroup(command);
    }

    @Assign
    UserDeleted handle(DeleteUser command, CommandContext context) {
        logCommand(command);
        return events(context).deleteUser(command);
    }

    @Assign
    RoleAssignedToUser handle(AssignRoleToUser command, CommandContext context) {
        logCommand(command);
        return events(context).assignRoleToUser(command);
    }

    @Assign
    RoleUnassignedFromUser handle(UnassignRoleFromUser command, CommandContext context) {
        logCommand(command);
        return events(context).unassignRoleFromUser(command);
    }

    @Assign
    UserAttributeAdded handle(AddUserAttribute command, CommandContext context) {
        logCommand(command);
        return events(context).addAttribute(command);
    }

    @Assign
    UserAttributeRemoved handle(RemoveUserAttribute command, CommandContext context) {
        logCommand(command);
        Map<String, Any> attributes = getState().getAttributesMap();
        String attributeName = command.getName();
        if (attributes.containsKey(attributeName)) {
            return events(context).removeAttribute(command, attributeName,
                                                   attributes.get(attributeName));
        } else {
            throw newIllegalStateException("Trying to remove an attribute that is not there");
        }
    }

    @Assign
    UserAttributeUpdated handle(UpdateUserAttribute command, CommandContext context) {
        String attributeName = command.getName();
        Map<String, Any> attributes = getState().getAttributes();
        if (attributes.containsKey(attributeName)) {
            return events(context).updateAttribute(command, attributes.get(attributeName));
        } else {
            throw newIllegalStateException("No attribute with the name {} found");
        }
    }

    @Assign
    UserStatusChanged handle(ChangeUserStatus command, CommandContext context) {
        logCommand(command);
        return events(context).changeStatus(command, getState().getStatus());
    }

    @Assign
    UserSignedOut handle(SignUserOut command, CommandContext context) {
        logCommand(command);
        return events(context).signOut(command);
    }

    @Assign
    AuthIdentityAdded handle(AddAuthIdentity command, CommandContext context) {
        logCommand(command);
        return events(context).addIdentity(command);
    }

    @Assign
    AuthIdentityRemoved handle(RemoveAuthIdentity command,
                               CommandContext context) {
        Optional<UserAuthIdentity> identityToRemove = findAuthIdentity(command);
        if (identityToRemove.isPresent()) {
            return events(context).removeIdentity(command, identityToRemove.get());
        } else {
            throw newIllegalStateException("No such auth identity");
        }
    }

    @Assign
    PrimaryAuthIdentityChanged handle(ChangePrimaryAuthIdentity command, CommandContext context) {
        return events(context).changePrimaryIdentity(command);
    }

    @Apply
    void on(UserCreated event) {
        getBuilder().setId(event.getId())
                    .setDisplayName(event.getDisplayName())
                    .setParentEntity(event.getParentEntity())
                    .setPrimaryAuthIdentity(event.getPrimaryIdentity())
                    .addAllRole(event.getRoleList())
                    .putAllAttributes(event.getAttributesMap())
                    .setWhenCreated(event.getWhenCreated())
                    .setProfile(event.getProfile())
                    .setStatus(event.getStatus());
    }

    @Apply
    void on(UserMoved event) {
        getBuilder().setParentEntity(event.getNewParentEntity());
    }

    @Apply
    void on(UserJoinedGroup event) {
        getBuilder().addMembership(event.getGroupId());

    }

    @Apply
    void on(UserLeftGroup event) {
        removeGroupMembership(event.getGroupId());
    }

    @Apply
    void on(UserDeleted event) {
        setDeleted(true);
    }

    @Apply
    void on(RoleAssignedToUser event) {
        getBuilder().addRole(event.getRoleId());
    }

    @Apply
    void on(RoleUnassignedFromUser event) {
        removeRole(event.getRoleId());
    }

    @Apply
    void on(UserAttributeAdded event) {
        getBuilder().putAttributes(event.getName(), event.getValue());
    }

    @Apply
    void on(UserAttributeRemoved event) {
        removeAttribute(event.getName());
    }

    @Apply
    void on(UserAttributeUpdated event) {
        removeAttribute(event.getName());
        getBuilder().putAttributes(event.getName(), event.getNewValue());
    }

    @Apply
    void on(UserStatusChanged event) {
        getBuilder().setStatus(event.getNewStatus());
    }

    @React
    UserSignedIn on(SignInCompleted event, EventContext context) {
        return events(context.getCommandContext()).signIn(event);
    }

    @Apply
    void on(UserSignedIn event) {
    }

    @Apply
    void on(UserSignedOut event) {
    }

    @Apply
    void on(AuthIdentityAdded event) {
        getBuilder().addAuthIdentity(event.getIdentity());
    }

    @Apply
    void on(AuthIdentityRemoved event) {
        removeAuthIdentity(event.getIdentity());
    }

    @Apply
    void on(PrimaryAuthIdentityChanged event) {
        getBuilder().setPrimaryAuthIdentity(event.getIdentity());
    }

    @SuppressWarnings("MethodMayBeStatic")
    private UserEventFactory events(CommandContext context) {
        return UserEventFactory.instance(context);
    }

    private Optional<UserAuthIdentity> findAuthIdentity(RemoveAuthIdentity command) {
        return getState().getAuthIdentityList()
                         .stream()
                         .filter(identityMatcher(command))
                         .findFirst();
    }

    private void removeAttribute(String attributeName) {
        UserVBuilder builder = getBuilder();
        Map<String, Any> attributes = builder.getAttributes();
        if (attributes.containsKey(attributeName)) {
            builder.removeAttributes(attributeName);
        }
    }

    private void removeRole(RoleId roleId) {
        List<RoleId> roles = getBuilder().getRole();
        if (roles.contains(roleId)) {
            int index = roles.indexOf(roleId);
            getBuilder().removeRole(index);
        }
    }

    private void removeGroupMembership(GroupId groupId) {
        List<GroupId> groups = getBuilder().getMembership();
        if (groups.contains(groupId)) {
            int index = groups.indexOf(groupId);
            getBuilder().removeMembership(index);
        }
    }

    private void removeAuthIdentity(UserAuthIdentity identity) {
        List<UserAuthIdentity> identities = getBuilder().getAuthIdentity();
        if (identities.contains(identity)) {
            int index = identities.indexOf(identity);
            getBuilder().removeAuthIdentity(index);
        }
    }

    private static Predicate<UserAuthIdentity> identityMatcher(
            RemoveAuthIdentity command) {
        return identity -> {
            boolean idMatches = identity.getUid()
                                        .equals(command.getUid());
            boolean providerMatches = identity.getProviderId()
                                              .equals(command.getProviderId());
            return idMatches && providerMatches;
        };
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
        private final Logger value = LoggerFactory.getLogger(UserAggregate.class);
    }
}
