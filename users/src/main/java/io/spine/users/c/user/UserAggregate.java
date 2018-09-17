/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import com.google.protobuf.Any;
import io.spine.core.CommandContext;
import io.spine.core.EventContext;
import io.spine.core.UserId;
import io.spine.server.aggregate.Aggregate;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.server.event.React;
import io.spine.users.GroupId;
import io.spine.users.RoleId;
import io.spine.users.UserAuthIdentity;
import io.spine.users.c.group.Group;
import io.spine.users.c.organization.Organization;
import io.spine.users.c.orgunit.OrgUnit;
import io.spine.users.c.role.Role;
import io.spine.users.c.signin.SignInSuccessful;
import io.spine.users.c.signin.SignOutCompleted;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * An aggregate for user of the application, either a person or machine.
 *
 * <h3>Group And Roles</h3>
 *
 * <p>To reflect a user's functions and functional roles in the organization, the user can be
 * assigned multiple {@link Role Roles} (please, see {@link AssignRoleToUser} and
 * {@link UnassignRoleFromUser} commands).
 *
 * <p>However, if a user share its functions and functional roles with a number of other users it
 * can also join one or more {@link Group groups} (please, see {@link JoinGroup} and
 * {@link LeaveGroup} commands).
 *
 * <h3>Organizational Structure</h3>
 *
 * <p>A user is a leaf in the hierarchical structure of the organization. It can have either
 * a single {@link Organization} or single {@link OrgUnit} as a parent organizational entity.
 *
 * <h3>User Attributes</h3>
 *
 * <p>To make {@link UserAggregate} meet specific requirements of the application, it can be
 * extended by custom attributes.
 *
 * <p>The following commands are available to work with the user attributes:
 *
 * <ul>
 *     <li>{@link AddUserAttribute} to add a new attribute, or replace the existing one;
 *     <li>{@link RemoveUserAttribute} to remove an attribute;
 *     <li>{@link UpdateUserAttribute} to update an existing attribute.
 * </ul>
 *
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings({"OverlyCoupledClass", "ClassWithTooManyMethods"}) // It is OK for aggregate.
public class UserAggregate extends Aggregate<UserId, User, UserVBuilder> {

    /**
     * @see Aggregate#Aggregate(Object)
     */
    UserAggregate(UserId id) {
        super(id);
    }

    // TODO:2018-08-27:vladyslav.lubenskyi: https://github.com/SpineEventEngine/users/issues/13
    @Assign
    UserCreated handle(CreateUser command, CommandContext context) {
        return events(context).create(command);
    }

    @Assign
    UserMoved handle(MoveUser command, CommandContext context) {
        return events(context).changeParent(command, getState().getOrgEntity());
    }

    @Assign
    UserJoinedGroup handle(JoinGroup command, CommandContext context) {
        return events(context).joinGroup(command);
    }

    @Assign
    UserLeftGroup handle(LeaveGroup command, CommandContext context) {
        return events(context).leaveGroup(command);
    }

    @Assign
    UserDeleted handle(DeleteUser command, CommandContext context) {
        return events(context).deleteUser(command);
    }

    @Assign
    RoleAssignedToUser handle(AssignRoleToUser command, CommandContext context) {
        return events(context).assignRoleToUser(command);
    }

    @Assign
    RoleUnassignedFromUser handle(UnassignRoleFromUser command, CommandContext context)
            throws RoleIsNotAssignedToUser {
        List<RoleId> roles = getState().getRoleList();
        RoleId roleId = command.getRoleId();
        if (!roles.contains(roleId)) {
            throw new RoleIsNotAssignedToUser(getId(), roleId);
        }
        return events(context).unassignRoleFromUser(command);
    }

    @Assign
    UserAttributeAdded handle(AddUserAttribute command, CommandContext context) {
        return events(context).addAttribute(command);
    }

    @Assign
    UserAttributeRemoved handle(RemoveUserAttribute command, CommandContext context)
            throws UserAttributeDoesNotExist {
        Map<String, Any> attributes = getState().getAttributesMap();
        String attributeName = command.getName();
        if (attributes.containsKey(attributeName)) {
            return events(context).removeAttribute(command, attributeName,
                                                   attributes.get(attributeName));
        } else {
            throw new UserAttributeDoesNotExist(getId(), attributeName);
        }
    }

    @Assign
    UserAttributeUpdated handle(UpdateUserAttribute command, CommandContext context)
            throws UserAttributeDoesNotExist {
        String attributeName = command.getName();
        Map<String, Any> attributes = getState().getAttributesMap();
        if (attributes.containsKey(attributeName)) {
            return events(context).updateAttribute(command, attributes.get(attributeName));
        } else {
            throw new UserAttributeDoesNotExist(getId(), attributeName);
        }
    }

    @Assign
    UserStatusChanged handle(ChangeUserStatus command, CommandContext context) {
        return events(context).changeStatus(command, getState().getStatus());
    }

    @Assign
    SecondaryAuthIdentityAdded handle(AddSecondaryAuthIdentity command, CommandContext context) {
        return events(context).addIdentity(command);
    }

    @Assign
    SecondaryAuthIdentityRemoved handle(RemoveSecondaryAuthIdentity command,
                                        CommandContext context) throws AuthIdentityDoesNotExist {
        Optional<UserAuthIdentity> identityToRemove = findAuthIdentity(command);
        if (identityToRemove.isPresent()) {
            return events(context).removeIdentity(command, identityToRemove.get());
        } else {
            throw identityDoesNotExist(command);
        }
    }

    @Assign
    PrimaryAuthIdentityChanged handle(ChangePrimaryAuthIdentity command, CommandContext context) {
        return events(context).changePrimaryIdentity(command);
    }

    @Assign
    UserRenamed handle(RenameUser command, CommandContext context) {
        return events(context).renameUser(command, getState().getDisplayName());
    }

    @Assign
    UserProfileUpdated handle(UpdateUserProfile command, CommandContext context) {
        return events(context).updateProfile(command);
    }

    @Apply
    void on(UserCreated event) {
        getBuilder().setId(event.getId())
                    .setDisplayName(event.getDisplayName())
                    .setOrgEntity(event.getOrgEntity())
                    .setPrimaryAuthIdentity(event.getPrimaryIdentity())
                    .addAllRole(event.getRoleList())
                    .putAllAttributes(event.getAttributesMap())
                    .setWhenCreated(event.getWhenCreated())
                    .setProfile(event.getProfile())
                    .setStatus(event.getStatus());
    }

    @Apply
    void on(UserMoved event) {
        getBuilder().setOrgEntity(event.getNewOrgEntity());
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
        String attributeName = event.getName();
        removeAttribute(attributeName);
        getBuilder().putAttributes(attributeName, event.getNewValue());
    }

    @Apply
    void on(UserStatusChanged event) {
        getBuilder().setStatus(event.getNewStatus());
    }

    @React
    UserSignedIn on(SignInSuccessful event, EventContext context) {
        return events(context.getCommandContext()).signIn(event);
    }

    @React
    UserSignedOut on(SignOutCompleted event, EventContext context) {
        return events(context.getCommandContext()).signOut(event);
    }

    @Apply
    void on(UserSignedIn event) {
    }

    @Apply
    void on(UserSignedOut event) {
    }

    @Apply
    void on(SecondaryAuthIdentityAdded event) {
        getBuilder().addSecondaryAuthIdentity(event.getIdentity());
    }

    @Apply
    void on(SecondaryAuthIdentityRemoved event) {
        removeAuthIdentity(event.getIdentity());
    }

    @Apply
    void on(PrimaryAuthIdentityChanged event) {
        getBuilder().setPrimaryAuthIdentity(event.getIdentity());
    }

    @Apply
    void on(UserRenamed event) {
        getBuilder().setDisplayName(event.getNewName());
    }

    @Apply
    void on(UserProfileUpdated event) {
        getBuilder().setProfile(event.getUpdatedProfile());
    }

    private Optional<UserAuthIdentity> findAuthIdentity(RemoveSecondaryAuthIdentity command) {
        return getState().getSecondaryAuthIdentityList()
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
        List<UserAuthIdentity> identities = getBuilder().getSecondaryAuthIdentity();
        if (identities.contains(identity)) {
            int index = identities.indexOf(identity);
            getBuilder().removeSecondaryAuthIdentity(index);
        }
    }

    private static Predicate<UserAuthIdentity> identityMatcher(
            RemoveSecondaryAuthIdentity command) {
        return identity -> {
            boolean idMatches = identity.getUserId()
                                        .equals(command.getUserId());
            boolean providerMatches = identity.getProviderId()
                                              .equals(command.getProviderId());
            return idMatches && providerMatches;
        };
    }

    private static AuthIdentityDoesNotExist identityDoesNotExist(
            RemoveSecondaryAuthIdentity command) {
        return new AuthIdentityDoesNotExist(command.getId(), command.getProviderId(),
                                            command.getUserId());
    }

    private static UserEventFactory events(CommandContext context) {
        return UserEventFactory.instance(context);
    }
}
