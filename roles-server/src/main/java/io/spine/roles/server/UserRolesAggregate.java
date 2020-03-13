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

import io.spine.core.CommandContext;
import io.spine.core.UserId;
import io.spine.roles.UserRolesV2;
import io.spine.roles.command.AssignRoleToUser;
import io.spine.roles.command.UnassignRoleFromUser;
import io.spine.roles.event.RoleAssignedToUser;
import io.spine.roles.event.RoleUnassignedFromUser;
import io.spine.server.aggregate.Aggregate;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.RoleId;
import io.spine.users.user.rejection.RoleIsNotAssignedToUser;

import java.util.List;

final class UserRolesAggregate extends Aggregate<UserId, UserRolesV2, UserRolesV2.Builder> {

    @Assign
    RoleAssignedToUser handle(AssignRoleToUser command, CommandContext context) {
        RoleAssignedToUser event = RoleAssignedToUser
                .newBuilder()
                .setId(command.getId())
                .setRoleId(command.getRoleId())
                .vBuild();
        return event;
    }

    @Assign
    RoleUnassignedFromUser handle(UnassignRoleFromUser command, CommandContext context)
            throws RoleIsNotAssignedToUser {
        List<RoleId> roles = state().getExplicitRoleList();
        RoleId roleId = command.getRoleId();
        if (!roles.contains(roleId)) {
            throw RoleIsNotAssignedToUser
                    .newBuilder()
                    .setId(id())
                    .setRoleId(roleId)
                    .build();
        }
        RoleUnassignedFromUser event = RoleUnassignedFromUser
                .newBuilder()
                .setId(command.getId())
                .setRoleId(command.getRoleId())
                .vBuild();
        return event;
    }

    @Apply
    private void on(RoleAssignedToUser event) {
        builder().addExplicitRole(event.getRoleId());
    }

    @Apply
    private void on(RoleUnassignedFromUser event) {
        removeRole(event.getRoleId());
    }

    private void removeRole(RoleId roleId) {
        List<RoleId> roles = builder().getExplicitRoleList();
        if (roles.contains(roleId)) {
            int index = roles.indexOf(roleId);
            builder().removeExplicitRole(index);
        }
    }
}
