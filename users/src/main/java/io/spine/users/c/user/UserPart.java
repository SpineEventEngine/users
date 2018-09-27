/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import io.spine.core.CommandContext;
import io.spine.core.UserId;
import io.spine.server.aggregate.Aggregate;
import io.spine.server.aggregate.AggregatePart;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.Identity;
import io.spine.users.RoleId;
import io.spine.users.c.group.Group;
import io.spine.users.c.organization.Organization;
import io.spine.users.c.orgunit.OrgUnit;
import io.spine.users.c.role.Role;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static io.spine.users.c.user.User.OriginCase.EXTERNAL_DOMAIN;
import static io.spine.util.Exceptions.newIllegalArgumentException;

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
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings({"OverlyCoupledClass", "ClassWithTooManyMethods"}) // It is OK for aggregate.
public class UserPart extends AggregatePart<UserId, User, UserVBuilder, UserRoot> {

    /**
     * @see Aggregate#Aggregate(Object)
     */
    UserPart(UserRoot root) {
        super(root);
    }

    // TODO:2018-08-27:vladyslav.lubenskyi: https://github.com/SpineEventEngine/users/issues/13
    @Assign
    UserCreated handle(CreateUser command, CommandContext context) {
        return events().create(command);
    }

    @Assign
    UserMoved handle(MoveUser command, CommandContext context) throws CanNotMoveExternalUser {
        if (getState().getOriginCase() == EXTERNAL_DOMAIN) {
            throw new CanNotMoveExternalUser(command.getId(), getState().getExternalDomain());
        }
        return events().changeParent(command, getState().getOrgEntity());
    }

    @Assign
    UserDeleted handle(DeleteUser command, CommandContext context) {
        return events().deleteUser(command);
    }

    @Assign
    RoleAssignedToUser handle(AssignRoleToUser command, CommandContext context) {
        return events().assignRoleToUser(command);
    }

    @Assign
    RoleUnassignedFromUser handle(UnassignRoleFromUser command, CommandContext context)
            throws RoleIsNotAssignedToUser {
        List<RoleId> roles = getState().getRoleList();
        RoleId roleId = command.getRoleId();
        if (!roles.contains(roleId)) {
            throw new RoleIsNotAssignedToUser(getId(), roleId);
        }
        return events().unassignRoleFromUser(command);
    }

    @Assign
    UserStatusChanged handle(ChangeUserStatus command, CommandContext context) {
        return events().changeStatus(command, getState().getStatus());
    }

    @Assign
    SecondaryIdentityAdded handle(AddSecondaryIdentity command, CommandContext context) {
        return events().addIdentity(command);
    }

    @Assign
    SecondaryIdentityRemoved handle(RemoveSecondaryIdentity command,
                                    CommandContext context) throws IdentityDoesNotExist {
        Optional<Identity> identityToRemove = findAuthIdentity(command);
        if (identityToRemove.isPresent()) {
            return events().removeIdentity(command, identityToRemove.get());
        } else {
            throw identityDoesNotExist(command);
        }
    }

    @Assign
    PrimaryIdentityChanged handle(ChangePrimaryIdentity command, CommandContext context) {
        return events().changePrimaryIdentity(command);
    }

    @Assign
    UserRenamed handle(RenameUser command, CommandContext context) {
        return events().renameUser(command, getState().getDisplayName());
    }

    @Assign
    PersonProfileUpdated handle(UpdatePersonProfile command, CommandContext context) {
        return events().updateProfile(command);
    }

    @Apply
    void on(UserCreated event) {
        UserVBuilder builder = getBuilder();
        builder.setId(event.getId())
                    .setDisplayName(event.getDisplayName())
                    .setPrimaryIdentity(event.getPrimaryIdentity())
                    .addAllRole(event.getRoleList())
                    .setProfile(event.getProfile())
                    .setNature(event.getNature())
                    .setStatus(event.getStatus());

        switch (event.getOriginCase()) {
            case ORG_ENTITY:
                builder.setOrgEntity(event.getOrgEntity());
                break;
            case EXTERNAL_DOMAIN:
                builder.setExternalDomain(event.getExternalDomain());
                break;
            case ORIGIN_NOT_SET:
                throw newIllegalArgumentException(
                        "No `origin` found in UserCreated event");
        }
    }

    @Apply
    void on(UserMoved event) {
        getBuilder().setOrgEntity(event.getNewOrgEntity());
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
    void on(UserStatusChanged event) {
        getBuilder().setStatus(event.getNewStatus());
    }

    @Apply
    void on(SecondaryIdentityAdded event) {
        getBuilder().addSecondaryIdentity(event.getIdentity());
    }

    @Apply
    void on(SecondaryIdentityRemoved event) {
        removeIdentity(event.getIdentity());
    }

    @Apply
    void on(PrimaryIdentityChanged event) {
        getBuilder().setPrimaryIdentity(event.getIdentity());
    }

    @Apply
    void on(UserRenamed event) {
        getBuilder().setDisplayName(event.getNewName());
    }

    @Apply
    void on(PersonProfileUpdated event) {
        getBuilder().setProfile(event.getUpdatedProfile());
    }

    private Optional<Identity> findAuthIdentity(RemoveSecondaryIdentity command) {
        return getState().getSecondaryIdentityList()
                         .stream()
                         .filter(identityMatcher(command))
                         .findFirst();
    }

    private void removeRole(RoleId roleId) {
        List<RoleId> roles = getBuilder().getRole();
        if (roles.contains(roleId)) {
            int index = roles.indexOf(roleId);
            getBuilder().removeRole(index);
        }
    }

    private void removeIdentity(Identity identity) {
        List<Identity> identities = getBuilder().getSecondaryIdentity();
        if (identities.contains(identity)) {
            int index = identities.indexOf(identity);
            getBuilder().removeSecondaryIdentity(index);
        }
    }

    private static Predicate<Identity> identityMatcher(RemoveSecondaryIdentity command) {
        return identity -> {
            boolean idMatches = identity.getUserId()
                                        .equals(command.getUserId());
            boolean providerMatches = identity.getProviderId()
                                              .equals(command.getProviderId());
            return idMatches && providerMatches;
        };
    }

    private static IdentityDoesNotExist identityDoesNotExist(
            RemoveSecondaryIdentity command) {
        return new IdentityDoesNotExist(command.getId(), command.getProviderId(),
                                        command.getUserId());
    }

    private static UserEventFactory events() {
        return UserEventFactory.instance();
    }
}
