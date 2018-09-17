/*
 * Copyright 2018, TeamDev. All rights reserved.
 *
 * Redistribution and use in source and/or binary forms, with or without
 * modification, must retain the above copyright notice and the following
 * disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.spine.users.c.group;

import com.google.protobuf.Any;
import io.spine.core.ActorContext;
import io.spine.core.CommandContext;
import io.spine.net.EmailAddress;
import io.spine.users.OrganizationalEntity;
import io.spine.users.c.AggregateEventFactory;

/**
 * An event factory for the {@linkplain GroupAggregate Group aggregate}.
 *
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings("OverlyCoupledClass")
        // It is OK for an event factory.
class GroupEventFactory extends AggregateEventFactory {

    /**
     * Prevents direct instantiation.
     */
    private GroupEventFactory(ActorContext actorContext) {
        super(actorContext);
    }

    /**
     * Retrieves an instance of {@link GroupEventFactory}.
     *
     * @return new instance of {@link GroupEventFactory}
     */
    static GroupEventFactory instance(CommandContext commandContext) {
        return new GroupEventFactory(commandContext.getActorContext());
    }

    GroupCreated createGroup(CreateGroup command) {
        return GroupCreatedVBuilder.newBuilder()
                                   .setId(command.getId())
                                   .setDisplayName(command.getDisplayName())
                                   .setEmail(command.getEmail())
                                   .addAllOwners(command.getOwnersList())
                                   .setOrgEntity(command.getOrgEntity())
                                   .addAllRole(command.getRoleList())
                                   .putAllAttributes(command.getAttributesMap())
                                   .build();
    }

    GroupMoved moveGroup(MoveGroup command, OrganizationalEntity oldOrgEntity) {
        return GroupMovedVBuilder.newBuilder()
                                 .setId(command.getId())
                                 .setNewOrgEntity(command.getNewOrgEntity())
                                 .setOldOrgEntity(oldOrgEntity)
                                 .build();
    }

    GroupDeleted deleteGroup(DeleteGroup command) {
        return GroupDeletedVBuilder.newBuilder()
                                   .setId(command.getId())
                                   .build();
    }

    ParentGroupJoined joinGroup(JoinParentGroup command) {
        return ParentGroupJoinedVBuilder.newBuilder()
                                        .setId(command.getId())
                                        .setParentGroupId(command.getParentGroupId())
                                        .build();
    }

    ParentGroupLeft leaveGroup(LeaveParentGroup command) {
        return ParentGroupLeftVBuilder.newBuilder()
                                      .setId(command.getId())
                                      .setParentGroupId(command.getParentGroupId())
                                      .build();
    }

    RoleAssignedToGroup assignRole(AssignRoleToGroup command) {
        return RoleAssignedToGroupVBuilder.newBuilder()
                                          .setId(command.getId())
                                          .setRoleId(command.getRoleId())
                                          .build();
    }

    RoleUnassignedFromGroup unassignRole(UnassignRoleFromGroup command) {
        return RoleUnassignedFromGroupVBuilder.newBuilder()
                                              .setId(command.getId())
                                              .setRoleId(command.getRoleId())
                                              .build();
    }

    GroupAttributeAdded addAttribute(AddGroupAttribute command) {
        return GroupAttributeAddedVBuilder.newBuilder()
                                          .setId(command.getId())
                                          .setName(command.getName())
                                          .setValue(command.getValue())
                                          .build();
    }

    GroupAttributeRemoved removeAttribute(RemoveGroupAttribute command, Any attributeValue) {
        return GroupAttributeRemovedVBuilder.newBuilder()
                                            .setId(command.getId())
                                            .setName(command.getName())
                                            .setValue(attributeValue)
                                            .build();
    }

    GroupAttributeUpdated updateAttribute(UpdateGroupAttribute command, Any oldValue) {
        return GroupAttributeUpdatedVBuilder.newBuilder()
                                            .setId(command.getId())
                                            .setName(command.getName())
                                            .setNewValue(command.getNewValue())
                                            .setOldValue(oldValue)
                                            .build();
    }

    GroupRenamed rename(RenameGroup command, String oldName) {
        return GroupRenamedVBuilder.newBuilder()
                                   .setId(command.getId())
                                   .setNewName(command.getNewName())
                                   .setOldName(oldName)
                                   .build();
    }

    GroupEmailChanged changeEmail(ChangeGroupEmail command, EmailAddress oldEmail) {
        return GroupEmailChangedVBuilder.newBuilder()
                                        .setId(command.getId())
                                        .setNewEmail(command.getNewEmail())
                                        .setOldEmail(oldEmail)
                                        .build();
    }

    GroupOwnerAdded addOwner(AddGroupOwner command) {
        return GroupOwnerAddedVBuilder.newBuilder()
                                      .setId(command.getId())
                                      .setNewOwner(command.getNewOwner())
                                      .build();
    }

    GroupOwnerRemoved removeOwner(RemoveGroupOwner command) {
        return GroupOwnerRemovedVBuilder.newBuilder()
                                        .setId(command.getId())
                                        .setRemovedOwner(command.getOwner())
                                        .build();
    }
}
