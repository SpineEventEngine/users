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

import io.spine.net.EmailAddress;
import io.spine.users.OrganizationOrUnit;

/**
 * An event factory for the {@linkplain GroupPart Group aggregate}.
 *
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings("OverlyCoupledClass")
        // It is OK for an event factory.
class GroupEventFactory {

    /**
     * Prevents direct instantiation.
     */
    private GroupEventFactory() {
    }

    /**
     * Retrieves an instance of {@link GroupEventFactory}.
     */
    static GroupEventFactory instance() {
        return new GroupEventFactory();
    }

    GroupCreated createGroup(CreateGroup command) {
        return GroupCreatedVBuilder.newBuilder()
                                   .setId(command.getId())
                                   .setDisplayName(command.getDisplayName())
                                   .setEmail(command.getEmail())
                                   .setExternalDomain(command.getExternalDomain())
                                   .setOrgEntity(command.getOrgEntity())
                                   .addAllRole(command.getRoleList())
                                   .build();
    }

    GroupMoved moveGroup(MoveGroup command, OrganizationOrUnit oldOrgEntity) {
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

    JoinedParentGroup joinGroup(JoinParentGroup command) {
        return JoinedParentGroupVBuilder.newBuilder()
                                        .setId(command.getId())
                                        .setParentGroupId(command.getParentGroupId())
                                        .build();
    }

    LeftParentGroup leaveGroup(LeaveParentGroup command) {
        return LeftParentGroupVBuilder.newBuilder()
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
}
