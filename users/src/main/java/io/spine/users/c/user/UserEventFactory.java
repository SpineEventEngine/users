/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import io.spine.core.ActorContext;
import io.spine.core.CommandContext;
import io.spine.users.Identity;
import io.spine.users.OrganizationOrUnit;
import io.spine.users.c.EntityEventFactory;
import io.spine.users.c.user.User.Status;

/**
 * An event factory for the {@linkplain UserPart User aggregate}.
 *
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings("OverlyCoupledClass") // It's OK for an event factory
final class UserEventFactory extends EntityEventFactory {

    /**
     * @see EntityEventFactory#EntityEventFactory(ActorContext)
     */
    private UserEventFactory(ActorContext actorContext) {
        super(actorContext);
    }

    /**
     * Retrieves an instance of {@link UserEventFactory}.
     *
     * @param context the {@link CommandContext} of the command to handle
     * @return new instance of {@link UserEventFactory}
     */
    static UserEventFactory instance(CommandContext context) {
        ActorContext actorContext = context.getActorContext();
        return new UserEventFactory(actorContext);
    }

    UserCreated create(CreateUser command) {
        UserCreated event =
                UserCreatedVBuilder.newBuilder()
                                   .setId(command.getId())
                                   .setDisplayName(command.getDisplayName())
                                   .setOrgEntity(command.getOrgEntity())
                                   .setPrimaryIdentity(command.getPrimaryIdentity())
                                   .addAllRole(command.getRoleList())
                                   .setStatus(command.getStatus())
                                   .setProfile(command.getProfile())
                                   .setNature(command.getNature())
                                   .build();
        return event;
    }

    UserMoved changeParent(MoveUser command, OrganizationOrUnit oldOrgEntity) {
        UserMoved event =
                UserMovedVBuilder.newBuilder()
                                 .setId(command.getId())
                                 .setNewOrgEntity(command.getNewOrgEntity())
                                 .setOldOrgEntity(oldOrgEntity)
                                 .build();
        return event;
    }

    UserJoinedGroup joinGroup(JoinGroup command) {
        UserJoinedGroup event =
                UserJoinedGroupVBuilder.newBuilder()
                                       .setId(command.getId())
                                       .setGroupId(command.getGroupId())
                                       .build();
        return event;
    }

    UserLeftGroup leaveGroup(LeaveGroup command) {
        UserLeftGroup event =
                UserLeftGroupVBuilder.newBuilder()
                                     .setId(command.getId())
                                     .setGroupId(command.getGroupId())
                                     .build();
        return event;
    }

    UserDeleted deleteUser(DeleteUser command) {
        UserDeleted event = UserDeletedVBuilder.newBuilder()
                                               .setId(command.getId())
                                               .build();
        return event;
    }

    RoleAssignedToUser assignRoleToUser(AssignRoleToUser command) {
        RoleAssignedToUser event = RoleAssignedToUserVBuilder.newBuilder()
                                                             .setId(command.getId())
                                                             .setRoleId(command.getRoleId())
                                                             .build();
        return event;
    }

    RoleUnassignedFromUser unassignRoleFromUser(UnassignRoleFromUser command) {
        RoleUnassignedFromUser event =
                RoleUnassignedFromUserVBuilder.newBuilder()
                                              .setId(command.getId())
                                              .setRoleId(command.getRoleId())
                                              .build();
        return event;
    }

    UserStatusChanged changeStatus(ChangeUserStatus command, Status oldStatus) {
        UserStatusChanged event = UserStatusChangedVBuilder.newBuilder()
                                                           .setId(command.getId())
                                                           .setNewStatus(command.getStatus())
                                                           .setOldStatus(oldStatus)
                                                           .build();
        return event;
    }

    SecondaryIdentityAdded addIdentity(AddSecondaryIdentity command) {
        SecondaryIdentityAdded event =
                SecondaryIdentityAddedVBuilder.newBuilder()
                                              .setId(command.getId())
                                              .setIdentity(command.getIdentity())
                                              .build();
        return event;
    }

    SecondaryIdentityRemoved removeIdentity(RemoveSecondaryIdentity command,
                                            Identity identity) {
        SecondaryIdentityRemoved event =
                SecondaryIdentityRemovedVBuilder.newBuilder()
                                                .setId(command.getId())
                                                .setIdentity(identity)
                                                .build();
        return event;
    }

    PrimaryIdentityChanged changePrimaryIdentity(ChangePrimaryIdentity command) {
        PrimaryIdentityChanged event =
                PrimaryIdentityChangedVBuilder.newBuilder()
                                              .setId(command.getId())
                                              .setIdentity(command.getIdentity())
                                              .build();
        return event;
    }

    PersonProfileUpdated updateProfile(UpdatePersonProfile command) {
        return PersonProfileUpdatedVBuilder.newBuilder()
                                         .setId(command.getId())
                                         .setUpdatedProfile(command.getUpdatedProfile())
                                         .build();
    }

    UserRenamed renameUser(RenameUser command, String oldName) {
        return UserRenamedVBuilder.newBuilder()
                                  .setId(command.getId())
                                  .setNewName(command.getNewName())
                                  .setOldName(oldName)
                                  .build();
    }
}
