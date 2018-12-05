/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user;

import io.spine.core.CommandContext;
import io.spine.core.UserId;
import io.spine.server.aggregate.Aggregate;
import io.spine.server.aggregate.AggregatePart;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.RoleId;
import io.spine.users.group.Group;
import io.spine.users.organization.Organization;
import io.spine.users.orgunit.OrgUnit;
import io.spine.users.role.Role;
import io.spine.users.user.command.AddSecondaryIdentity;
import io.spine.users.user.command.AssignRoleToUser;
import io.spine.users.user.command.ChangePrimaryIdentity;
import io.spine.users.user.command.ChangeUserStatus;
import io.spine.users.user.command.CreateUser;
import io.spine.users.user.command.DeleteUser;
import io.spine.users.user.command.MoveUser;
import io.spine.users.user.command.RemoveSecondaryIdentity;
import io.spine.users.user.command.RenameUser;
import io.spine.users.user.command.UnassignRoleFromUser;
import io.spine.users.user.command.UpdatePersonProfile;
import io.spine.users.user.event.PersonProfileUpdated;
import io.spine.users.user.event.PersonProfileUpdatedVBuilder;
import io.spine.users.user.event.PrimaryIdentityChanged;
import io.spine.users.user.event.PrimaryIdentityChangedVBuilder;
import io.spine.users.user.event.RoleAssignedToUser;
import io.spine.users.user.event.RoleAssignedToUserVBuilder;
import io.spine.users.user.event.RoleUnassignedFromUser;
import io.spine.users.user.event.RoleUnassignedFromUserVBuilder;
import io.spine.users.user.event.SecondaryIdentityAdded;
import io.spine.users.user.event.SecondaryIdentityAddedVBuilder;
import io.spine.users.user.event.SecondaryIdentityRemoved;
import io.spine.users.user.event.SecondaryIdentityRemovedVBuilder;
import io.spine.users.user.event.UserCreated;
import io.spine.users.user.event.UserCreatedVBuilder;
import io.spine.users.user.event.UserDeleted;
import io.spine.users.user.event.UserDeletedVBuilder;
import io.spine.users.user.event.UserMoved;
import io.spine.users.user.event.UserMovedVBuilder;
import io.spine.users.user.event.UserRenamed;
import io.spine.users.user.event.UserRenamedVBuilder;
import io.spine.users.user.event.UserStatusChanged;
import io.spine.users.user.event.UserStatusChangedVBuilder;
import io.spine.users.user.rejection.CannotMoveExternalUser;
import io.spine.users.user.rejection.IdentityDoesNotExist;
import io.spine.users.user.rejection.RoleIsNotAssignedToUser;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static io.spine.users.user.User.OriginCase.EXTERNAL_DOMAIN;
import static io.spine.util.Exceptions.newIllegalArgumentException;

/**
 * An aggregate for user of the application, either a person or machine.
 *
 * <h3>Group And Roles</h3>
 *
 * <p>To reflect a user's functions and functional roles in the organization, the user can be
 * assigned multiple {@link Role Roles} (please see {@link AssignRoleToUser} and
 * {@link UnassignRoleFromUser} commands).
 *
 * <p>However, if a user share its functions and functional roles with a number of other users it
 * can also join one or more {@link Group groups} (please see {@code JoinGroup} and
 * {@code LeaveGroup} commands).
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
        UserCreatedVBuilder eventBuilder =
                UserCreatedVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setDisplayName(command.getDisplayName())
                        .setPrimaryIdentity(command.getPrimaryIdentity())
                        .setStatus(command.getStatus())
                        .setProfile(command.getProfile())
                        .setNature(command.getNature());

        switch (command.getOriginCase()) {
            case ORG_ENTITY:
                eventBuilder.setOrgEntity(command.getOrgEntity());
                break;
            case EXTERNAL_DOMAIN:
                eventBuilder.setExternalDomain(command.getExternalDomain());
                break;
            case ORIGIN_NOT_SET: // Fallthrough intended.
            default:
                throw newIllegalArgumentException("No `origin` found in CreateUser command");
        }
        return eventBuilder.build();
    }

    @Assign
    UserMoved handle(MoveUser command, CommandContext context) throws CannotMoveExternalUser {
        if (getState().getOriginCase() == EXTERNAL_DOMAIN) {
            throw CannotMoveExternalUser
                    .newBuilder()
                    .setId(command.getId())
                    .setExternalDomain(getState().getExternalDomain())
                    .build();
        }
        UserMoved event =
                UserMovedVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setNewOrgEntity(command.getNewOrgEntity())
                        .setOldOrgEntity(getState().getOrgEntity())
                        .build();
        return event;
    }

    @Assign
    UserDeleted handle(DeleteUser command, CommandContext context) {
        UserDeleted event =
                UserDeletedVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .build();
        return event;
    }

    @Assign
    RoleAssignedToUser handle(AssignRoleToUser command, CommandContext context) {
        RoleAssignedToUser event =
                RoleAssignedToUserVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setRoleId(command.getRoleId())
                        .build();
        return event;
    }

    @Assign
    RoleUnassignedFromUser handle(UnassignRoleFromUser command, CommandContext context)
            throws RoleIsNotAssignedToUser {
        List<RoleId> roles = getState().getRoleList();
        RoleId roleId = command.getRoleId();
        if (!roles.contains(roleId)) {
            throw RoleIsNotAssignedToUser
                    .newBuilder()
                    .setId(getId())
                    .setRoleId(roleId)
                    .build();
        }
        RoleUnassignedFromUser event =
                RoleUnassignedFromUserVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setRoleId(command.getRoleId())
                        .build();
        return event;
    }

    @Assign
    UserStatusChanged handle(ChangeUserStatus command, CommandContext context) {
        UserStatusChanged event =
                UserStatusChangedVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setNewStatus(command.getStatus())
                        .setOldStatus(getState().getStatus())
                        .build();
        return event;
    }

    @Assign
    SecondaryIdentityAdded handle(AddSecondaryIdentity command, CommandContext context) {
        SecondaryIdentityAdded event =
                SecondaryIdentityAddedVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setIdentity(command.getIdentity())
                        .build();
        return event;
    }

    @Assign
    SecondaryIdentityRemoved handle(RemoveSecondaryIdentity command,
                                    CommandContext context) throws IdentityDoesNotExist {
        Optional<Identity> identityToRemove = findAuthIdentity(command);
        if (identityToRemove.isPresent()) {
            SecondaryIdentityRemoved event =
                    SecondaryIdentityRemovedVBuilder
                            .newBuilder()
                            .setId(command.getId())
                            .setIdentity(identityToRemove.get())
                            .build();
            return event;
        } else {
            throw identityDoesNotExist(command);
        }
    }

    @Assign
    PrimaryIdentityChanged handle(ChangePrimaryIdentity command, CommandContext context) {
        PrimaryIdentityChanged event =
                PrimaryIdentityChangedVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setIdentity(command.getIdentity())
                        .build();
        return event;
    }

    @Assign
    UserRenamed handle(RenameUser command, CommandContext context) {
        UserRenamed event =
                UserRenamedVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setNewName(command.getNewName())
                        .setOldName(getState().getDisplayName())
                        .build();
        return event;
    }

    @Assign
    PersonProfileUpdated handle(UpdatePersonProfile command, CommandContext context) {
        PersonProfileUpdated event =
                PersonProfileUpdatedVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setUpdatedProfile(command.getUpdatedProfile())
                        .build();
        return event;
    }

    @Apply
    void on(UserCreated event) {
        UserVBuilder builder = getBuilder();
        builder.setId(event.getId())
                    .setDisplayName(event.getDisplayName())
                    .setPrimaryIdentity(event.getPrimaryIdentity())
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
            case ORIGIN_NOT_SET: // Fallthrough intended.
            default:
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
        return IdentityDoesNotExist
                .newBuilder()
                .setId(command.getId())
                .setProviderId(command.getProviderId())
                .setUserId(command.getUserId())
                .build();
    }
}
