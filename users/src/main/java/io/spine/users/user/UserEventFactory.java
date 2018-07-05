/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user;

import io.spine.core.ActorContext;
import io.spine.core.CommandContext;
import io.spine.time.OffsetDateTime;
import io.spine.time.OffsetDateTimes;
import io.spine.time.ZoneOffset;
import io.spine.users.ParentEntity;
import io.spine.users.User.Status;
import io.spine.users.UserAuthIdentity;
import io.spine.users.signin.RemoteIdentitySignInFinished;

/**
 * An event factory for the {@linkplain UserAggregate User aggregate}.
 *
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings("OverlyCoupledClass")
// It's OK for an event factory
final class UserEventFactory {

    private final ActorContext actorContext;

    /**
     * Prevents direct instantiation.
     */
    private UserEventFactory(ActorContext actorContext) {
        this.actorContext = actorContext;
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
                        .setParentEntity(command.getParentEntity())
                        .setPrimaryIdentity(command.getPrimaryIdentity())
                        .addAllRole(command.getRoleList())
                        .addAllAttribute(command.getAttributeList())
                        .setStatus(command.getStatus())
                        .setWhenCreated(now())
                        .setProfile(command.getProfile())
                        .build();
        return event;
    }

    UserMoved changeParent(MoveUser command, ParentEntity oldParentEntity) {
        UserMoved event =
                UserMovedVBuilder.newBuilder()
                        .setId(command.getId())
                        .setNewParentEntity(command.getNewParentEntity())
                        .setOldParentEntity(oldParentEntity)
                        .build();
        return event;
    }

    GroupMembershipStarted startGroupMembership(StartGroupMembership command) {
        GroupMembershipStarted event =
                GroupMembershipStartedVBuilder.newBuilder()
                        .setId(command.getId())
                        .setGroupId(command.getGroupId())
                        .build();
        return event;
    }

    GroupMembershipStopped stopGroupMembership(StopGroupMembership command) {
        GroupMembershipStopped event =
                GroupMembershipStoppedVBuilder.newBuilder()
                        .setId(command.getId())
                        .setGroupId(command.getGroupId())
                        .build();
        return event;
    }

    UserDeleted deleteUser(DeleteUser command) {
        UserDeleted event = UserDeletedVBuilder.newBuilder()
                .setId(command.getId())
                .setWhenDeleted(now())
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

    UserAttributeAdded addAttribute(AddUserAttribute command) {
        UserAttributeAdded event =
                UserAttributeAddedVBuilder.newBuilder()
                        .setId(command.getId())
                        .setAttribute(command.getAttribute())
                        .build();
        return event;
    }

    UserAttributeRemoved removeAttribute(RemoveUserAttribute command,
                                         UserAttribute removedAttrbite) {
        UserAttributeRemoved event =
                UserAttributeRemovedVBuilder.newBuilder()
                        .setId(command.getId())
                        .setAttribute(removedAttrbite)
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

    UserSignedIn signIn(RemoteIdentitySignInFinished event) {
        UserSignedIn result = UserSignedInVBuilder.newBuilder()
                .setId(event.getId())
                .setIdentity(event.getIdentity())
                .setWhenSignedIn(now())
                .build();
        return result;
    }

    UserSignedOut signOut(SignUserOut command) {
        UserSignedOut event = UserSignedOutVBuilder.newBuilder()
                .setId(command.getId())
                .setWhenSignedOut(now())
                .build();
        return event;
    }

    AuthIdentityAdded addIdentity(AddAuthIdentity command) {
        AuthIdentityAdded event =
                AuthIdentityAddedVBuilder.newBuilder()
                        .setId(command.getId())
                        .setIdentity(command.getIdentity())
                        .build();
        return event;
    }

    AuthIdentityRemoved removeIdentity(RemoveAuthIdentity command,
                                       UserAuthIdentity identity) {
        AuthIdentityRemoved event =
                AuthIdentityRemovedVBuilder.newBuilder()
                        .setId(command.getId())
                        .setIdentity(identity)
                        .build();
        return event;
    }

    UserAttributeUpdated updateAttribute(UpdateUserAttribute command, UserAttribute oldAttribute) {
        UserAttributeUpdated event =
                UserAttributeUpdatedVBuilder.newBuilder()
                        .setId(command.getId())
                        .setNewAttribute(command.getAttribute())
                        .setOldAttribute(oldAttribute)
                        .build();
        return event;
    }

    PrimaryAuthIdentityChanged changePrimaryIdentity(ChangePrimaryAuthIdentity command) {
        PrimaryAuthIdentityChanged event =
                PrimaryAuthIdentityChangedVBuilder.newBuilder()
                        .setId(command.getId())
                        .setIdentity(command.getIdentity())
                        .build();
        return event;
    }

    private OffsetDateTime now() {
        final ZoneOffset zoneOffset = actorContext.getZoneOffset();
        return OffsetDateTimes.now(zoneOffset);
    }
}

