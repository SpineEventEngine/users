/*
 * Copyright 2020, TeamDev. All rights reserved.
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

package io.spine.roles.server;

import io.spine.roles.GroupRolesV2;
import io.spine.roles.command.AssignRoleToGroup;
import io.spine.roles.command.UnassignRoleFromGroup;
import io.spine.server.aggregate.Aggregate;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.GroupId;
import io.spine.roles.RoleId;
import io.spine.roles.event.RoleAssignedToGroup;
import io.spine.roles.server.event.RoleUnassignedFromGroup;
import io.spine.roles.rejection.RoleIsNotAssignedToGroup;

import java.util.List;

final class GroupRolesAggregate extends Aggregate<GroupId, GroupRolesV2, GroupRolesV2.Builder> {

    @Assign
    RoleAssignedToGroup handle(AssignRoleToGroup command) {
        return RoleAssignedToGroup
                .newBuilder()
                .setId(command.getId())
                .setRoleId(command.getRoleId())
                .build();
    }

    @Assign
    RoleUnassignedFromGroup handle(UnassignRoleFromGroup command)
            throws RoleIsNotAssignedToGroup {
        List<RoleId> roles = state().getExplicitRoleList();
        RoleId roleId = command.getRoleId();
        if (!roles.contains(roleId)) {
            throw RoleIsNotAssignedToGroup
                    .newBuilder()
                    .setId(id())
                    .setRoleId(roleId)
                    .build();
        }
        return RoleUnassignedFromGroup
                .newBuilder()
                .setId(command.getId())
                .setRoleId(command.getRoleId())
                .build();
    }

    @Apply
    private void on(RoleAssignedToGroup event) {
        builder().addExplicitRole(event.getRoleId());
    }

    @Apply
    private void on(RoleUnassignedFromGroup event) {
        RoleId roleId = event.getRoleId();
        List<RoleId> roles = builder().getExplicitRoleList();
        if (roles.contains(roleId)) {
            int index = roles.indexOf(roleId);
            builder().removeExplicitRole(index);
        }
    }
}
