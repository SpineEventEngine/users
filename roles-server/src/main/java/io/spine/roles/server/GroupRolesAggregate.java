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

import io.spine.roles.GroupRoles;
import io.spine.roles.command.AssignRoleToGroup;
import io.spine.roles.command.RemoveRoleAssignmentFromGroup;
import io.spine.server.aggregate.Aggregate;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.GroupId;
import io.spine.roles.RoleId;
import io.spine.roles.event.RoleAssignedToGroup;
import io.spine.roles.event.RoleAssignmentRemovedFromGroup;
import io.spine.roles.rejection.RoleIsNotAssignedToGroup;

import java.util.List;

/**
 * Manages assignment of roles to a group.
 */
final class GroupRolesAggregate extends Aggregate<GroupId, GroupRoles, GroupRoles.Builder> {

    @Assign
    RoleAssignedToGroup handle(AssignRoleToGroup command) {
        return RoleAssignedToGroup
                .newBuilder()
                .setGroup(command.getGroup())
                .setRole(command.getRole())
                .build();
    }

    @Assign
    RoleAssignmentRemovedFromGroup handle(RemoveRoleAssignmentFromGroup command)
            throws RoleIsNotAssignedToGroup {
        List<RoleId> roles = state().getAssignedList();
        GroupId group = id();
        RoleId role = command.getRole();
        if (!roles.contains(role)) {
            throw RoleIsNotAssignedToGroup
                    .newBuilder()
                    .setGroup(group)
                    .setRole(role)
                    .build();
        }
        return RoleAssignmentRemovedFromGroup
                .newBuilder()
                .setGroup(group)
                .setRole(role)
                .build();
    }

    @Apply
    private void on(RoleAssignedToGroup event) {
        builder().addAssigned(event.getRole());
    }

    @Apply
    private void on(RoleAssignmentRemovedFromGroup event) {
        RoleId roleId = event.getRole();
        List<RoleId> roles = builder().getAssignedList();
        if (roles.contains(roleId)) {
            int index = roles.indexOf(roleId);
            builder().removeAssigned(index);
        }
    }
}
