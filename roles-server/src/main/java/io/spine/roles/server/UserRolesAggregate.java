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
import io.spine.roles.event.RoleDisinheritedByUser;
import io.spine.roles.event.RoleInheritedByUser;
import io.spine.roles.event.RoleUnassignedFromUser;
import io.spine.server.aggregate.Aggregate;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.roles.RoleId;
import io.spine.roles.rejection.RoleIsNotAssignedToUser;
import io.spine.server.event.React;
import io.spine.server.model.Nothing;

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
    void on(RoleAssignedToUser event) {
        builder().addExplicitRole(event.getRoleId());
    }

    @Apply
    void on(RoleUnassignedFromUser event) {
        removeExplicitRole(event.getRoleId());
    }

    @React
    Nothing on(RoleInheritedByUser event) {
        builder().addImplicitRole(event.getRoleId());
        return nothing();
    }

    @React
    Nothing on(RoleDisinheritedByUser event) {
        final RoleId role = event.getRoleId();
        removeImplicitRole(role);
        return nothing();
    }

    private void removeExplicitRole(RoleId role) {
        int index = state().getExplicitRoleList()
                           .indexOf(role);
        if (index != -1) {
            builder().removeExplicitRole(index);
        }
    }

    private void removeImplicitRole(RoleId role) {
        int index = state().getImplicitRoleList()
                           .indexOf(role);
        if (index != -1) {
            builder().removeImplicitRole(index);
        }
    }
}
