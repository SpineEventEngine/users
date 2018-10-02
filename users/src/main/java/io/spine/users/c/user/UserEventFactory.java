/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import io.spine.users.Identity;
import io.spine.users.OrganizationOrUnit;
import io.spine.users.c.user.User.Status;

import static io.spine.users.c.user.RoleInGroup.MEMBER;
import static io.spine.util.Exceptions.newIllegalArgumentException;

/**
 * An event factory for the {@linkplain UserPart User aggregate}.
 *
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings("OverlyCoupledClass") // It's OK for an event factory
final class UserEventFactory {

    /**
     * Prevents direct instantiation.
     */
    private UserEventFactory() {
    }

    /**
     * Retrieves an instance of {@link UserEventFactory}.
     */
    static UserEventFactory instance() {
        return new UserEventFactory();
    }

    UserCreated create(CreateUser command) {
        UserCreatedVBuilder eventBuilder =
                UserCreatedVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setDisplayName(command.getDisplayName())
                        .setPrimaryIdentity(command.getPrimaryIdentity())
                        .addAllRole(command.getRoleList())
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
            case ORIGIN_NOT_SET:
                throw newIllegalArgumentException("No `origin` found in CreateUser command");
        }
        return eventBuilder.build();
    }

    UserMoved changeParent(MoveUser command, OrganizationOrUnit oldOrgEntity) {
        UserMoved event =
                UserMovedVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setNewOrgEntity(command.getNewOrgEntity())
                        .setOldOrgEntity(oldOrgEntity)
                        .build();
        return event;
    }

    UserJoinedGroup joinGroup(JoinGroup command) {
        UserJoinedGroup event =
                UserJoinedGroupVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setGroupId(command.getGroupId())
                        .setRole(MEMBER)
                        .build();
        return event;
    }

    UserLeftGroup leaveGroup(LeaveGroup command) {
        UserLeftGroup event =
                UserLeftGroupVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setGroupId(command.getGroupId())
                        .build();
        return event;
    }

    UserDeleted deleteUser(DeleteUser command) {
        UserDeleted event = UserDeletedVBuilder
                .newBuilder()
                                               .setId(command.getId())
                                               .build();
        return event;
    }

    RoleAssignedToUser assignRoleToUser(AssignRoleToUser command) {
        RoleAssignedToUser event = RoleAssignedToUserVBuilder
                .newBuilder()
                                                             .setId(command.getId())
                                                             .setRoleId(command.getRoleId())
                                                             .build();
        return event;
    }

    RoleUnassignedFromUser unassignRoleFromUser(UnassignRoleFromUser command) {
        RoleUnassignedFromUser event =
                RoleUnassignedFromUserVBuilder
                        .newBuilder()
                                              .setId(command.getId())
                                              .setRoleId(command.getRoleId())
                                              .build();
        return event;
    }

    UserStatusChanged changeStatus(ChangeUserStatus command, Status oldStatus) {
        UserStatusChanged event = UserStatusChangedVBuilder
                .newBuilder()
                                                           .setId(command.getId())
                                                           .setNewStatus(command.getStatus())
                                                           .setOldStatus(oldStatus)
                                                           .build();
        return event;
    }

    SecondaryIdentityAdded addIdentity(AddSecondaryIdentity command) {
        SecondaryIdentityAdded event =
                SecondaryIdentityAddedVBuilder
                        .newBuilder()
                                              .setId(command.getId())
                                              .setIdentity(command.getIdentity())
                                              .build();
        return event;
    }

    SecondaryIdentityRemoved removeIdentity(RemoveSecondaryIdentity command,
                                            Identity identity) {
        SecondaryIdentityRemoved event =
                SecondaryIdentityRemovedVBuilder
                        .newBuilder()
                                                .setId(command.getId())
                                                .setIdentity(identity)
                                                .build();
        return event;
    }

    PrimaryIdentityChanged changePrimaryIdentity(ChangePrimaryIdentity command) {
        PrimaryIdentityChanged event =
                PrimaryIdentityChangedVBuilder
                        .newBuilder()
                                              .setId(command.getId())
                                              .setIdentity(command.getIdentity())
                                              .build();
        return event;
    }

    PersonProfileUpdated updateProfile(UpdatePersonProfile command) {
        return PersonProfileUpdatedVBuilder
                .newBuilder()
                                         .setId(command.getId())
                                         .setUpdatedProfile(command.getUpdatedProfile())
                                         .build();
    }

    UserRenamed renameUser(RenameUser command, String oldName) {
        return UserRenamedVBuilder
                .newBuilder()
                                  .setId(command.getId())
                                  .setNewName(command.getNewName())
                                  .setOldName(oldName)
                                  .build();
    }
}
