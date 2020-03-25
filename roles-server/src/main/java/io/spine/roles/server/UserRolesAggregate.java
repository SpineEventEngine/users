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

import io.spine.core.UserId;
import io.spine.roles.RoleId;
import io.spine.roles.UserRoles;
import io.spine.roles.command.AssignRoleToUser;
import io.spine.roles.command.RemoveRoleAssignmentFromUser;
import io.spine.roles.event.RoleAssignedToUser;
import io.spine.roles.event.RoleAssignmentRemovedFromUser;
import io.spine.roles.rejection.RoleIsNotAssignedToUser;
import io.spine.roles.server.event.RolePropagatedToUser;
import io.spine.roles.server.event.RolePropagationCanceledForUser;
import io.spine.server.aggregate.Aggregate;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.server.event.React;
import io.spine.server.model.Nothing;

import java.util.List;

/**
 * Manages role assignment and propagation for a user.
 */
final class UserRolesAggregate extends Aggregate<UserId, UserRoles, UserRoles.Builder> {

    @Assign
    RoleAssignedToUser handle(AssignRoleToUser c) {
        RoleAssignedToUser event = RoleAssignedToUser
                .newBuilder()
                .setUser(c.getUser())
                .setRole(c.getRole())
                .vBuild();
        return event;
    }

    @Assign
    RoleAssignmentRemovedFromUser handle(RemoveRoleAssignmentFromUser c)
            throws RoleIsNotAssignedToUser {
        List<RoleId> roles = state().getAssignedList();
        RoleId role = c.getRole();
        final UserId user = id();
        if (!roles.contains(role)) {
            throw RoleIsNotAssignedToUser
                    .newBuilder()
                    .setUser(user)
                    .setRole(role)
                    .build();
        }
        RoleAssignmentRemovedFromUser event = RoleAssignmentRemovedFromUser
                .newBuilder()
                .setUser(user)
                .setRole(role)
                .vBuild();
        return event;
    }

    @Apply
    private void on(RoleAssignedToUser e) {
        builder().addAssigned(e.getRole());
    }

    @Apply
    private void on(RoleAssignmentRemovedFromUser e) {
        removeAssignedRole(e.getRole());
    }

    @React
    Nothing on(RolePropagatedToUser e) {
        builder().addPropagated(e.getRole());
        return nothing();
    }

    @React
    Nothing on(RolePropagationCanceledForUser e) {
        final RoleId role = e.getRole();
        removePropagatedRole(role);
        return nothing();
    }

    private void removeAssignedRole(RoleId role) {
        int index = state().getAssignedList()
                           .indexOf(role);
        if (index != -1) {
            builder().removeAssigned(index);
        }
    }

    private void removePropagatedRole(RoleId role) {
        int index = state().getPropagatedList()
                           .indexOf(role);
        if (index != -1) {
            builder().removePropagated(index);
        }
    }
}
