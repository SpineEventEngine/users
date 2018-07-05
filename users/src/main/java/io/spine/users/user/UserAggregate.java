/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user;

import com.google.common.annotations.VisibleForTesting;
import com.google.protobuf.Message;
import io.spine.users.signin.RemoteIdentitySignInFinished;
import io.spine.users.*;
import io.spine.core.CommandContext;
import io.spine.core.EventContext;
import io.spine.core.React;
import io.spine.core.UserId;
import io.spine.server.aggregate.Aggregate;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static io.spine.util.Exceptions.newIllegalStateException;

/**
 * A user of the application.
 *
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
    GroupMembershipStarted handle(StartGroupMembership command, CommandContext context) {
        logCommand(command);
        return events(context).startGroupMembership(command);
    }

    @Assign
    GroupMembershipStopped handle(StopGroupMembership command, CommandContext context) {
        logCommand(command);
        return events(context).stopGroupMembership(command);
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
        List<UserAttribute> attributes = getState().getAttributeList();
        String attributeName = command.getAttributeName();
        Optional<UserAttribute> attribute =
                attributes.stream()
                          .filter(userAttribute -> userAttribute.getName()
                                                                .equals(attributeName))
                          .findFirst();
        if (attribute.isPresent()) {
            return events(context).removeAttribute(command, attribute.get());
        } else {
            throw newIllegalStateException("Trying to remove an attribute that is not there");
        }
    }

    @Assign
    UserAttributeUpdated handle(UpdateUserAttribute command, CommandContext context) {
        String attributeName = command.getAttribute()
                                      .getName();
        Optional<UserAttribute> attributeToUpdate = findAttributeByName(attributeName);
        if (attributeToUpdate.isPresent()) {
            return events(context).updateAttribute(command, attributeToUpdate.get());
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
        logEvent(event);
        getBuilder().setId(event.getId())
                    .setDisplayName(event.getDisplayName())
                    .setParentEntity(event.getParentEntity())
                    .setPrimaryAuthIdentity(event.getPrimaryIdentity())
                    .addAllRole(event.getRoleList())
                    .addAllAttribute(event.getAttributeList())
                    .setWhenCreated(event.getWhenCreated())
                    .setProfile(event.getProfile())
                    .setStatus(event.getStatus());
    }

    @Apply
    void on(UserMoved event) {
        logEvent(event);
        getBuilder().setParentEntity(event.getNewParentEntity());
    }

    @Apply
    void on(GroupMembershipStarted event) {
        logEvent(event);
        getBuilder().addMembership(event.getGroupId());

    }

    @Apply
    void on(GroupMembershipStopped event) {
        logEvent(event);
        removeGroupMembership(event.getGroupId());
    }

    @Apply
    void on(UserDeleted event) {
        logEvent(event);
        setDeleted(true);
    }

    @Apply
    void on(RoleAssignedToUser event) {
        logEvent(event);
        getBuilder().addRole(event.getRoleId());
    }

    @Apply
    void on(RoleUnassignedFromUser event) {
        logEvent(event);
        removeRole(event.getRoleId());
    }

    @Apply
    void on(UserAttributeAdded event) {
        logEvent(event);
        getBuilder().addAttribute(event.getAttribute());
    }

    @Apply
    void on(UserAttributeRemoved event) {
        logEvent(event);
        removeAttribute(event.getAttribute());
    }

    @Apply
    void on(UserAttributeUpdated event) {
        logEvent(event);
        removeAttribute(event.getOldAttribute());
        getBuilder().addAttribute(event.getNewAttribute());
    }

    @Apply
    void on(UserStatusChanged event) {
        logEvent(event);
        getBuilder().setStatus(event.getNewStatus());
    }

    @React
    UserSignedIn on(RemoteIdentitySignInFinished event, EventContext context) {
        logEvent(event);
        return events(context.getCommandContext()).signIn(event);
    }

    @Apply
    void on(UserSignedIn event) {
        logEvent(event);
    }

    @Apply
    void on(UserSignedOut event) {
        logEvent(event);
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

    private void logEvent(Message event) {
        log().info("'{}' event came to {}. For ID: {}.", event.getClass()
                                                              .getSimpleName(),
                   getClass().getSimpleName(), getId().getValue());
    }

    private Optional<UserAuthIdentity> findAuthIdentity(RemoveAuthIdentity command) {
        return getState().getAuthIdentityList()
                         .stream()
                         .filter(identityMatcher(command))
                         .findFirst();
    }

    private Optional<UserAttribute> findAttributeByName(String name) {
        for (UserAttribute attribute : getState().getAttributeList()) {
            if (attribute.getName()
                         .equals(name)) {
                return Optional.of(attribute);
            }
        }
        return Optional.empty();
    }

    private void removeAttribute(UserAttribute attribute) {
        List<UserAttribute> attributes = getBuilder().getAttribute();
        if (attributes.contains(attribute)) {
            int index = attributes.indexOf(attribute);
            getBuilder().removeAttribute(index);
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
