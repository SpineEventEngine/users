/*
 * Copyright 2019, TeamDev. All rights reserved.
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

package io.spine.users.server.user;

import io.spine.core.Subscribe;
import io.spine.core.UserId;
import io.spine.server.projection.Projection;
import io.spine.users.RoleId;
import io.spine.users.group.event.RoleDisinheritedByUser;
import io.spine.users.group.event.RoleInheritedByUser;
import io.spine.users.user.UserRoles;
import io.spine.users.user.event.RoleAssignedToUser;
import io.spine.users.user.event.RoleUnassignedFromUser;
import io.spine.users.user.event.UserCreated;

/**
 * Aggregates all all user roles assigned both explicitly and implicitly.
 */
public final class UserRolesProjection extends Projection<UserId, UserRoles, UserRoles.Builder> {

    @Subscribe
    void on(UserCreated event) {
        UserId userId = event.getId();
        builder().setId(userId);
    }

    @Subscribe
    void on(RoleInheritedByUser event) {
        RoleId inheritedRole = event.getRoleId();
        builder().addRole(inheritedRole);
    }

    @Subscribe
    void on(RoleDisinheritedByUser event) {
        removeRole(event.getRoleId());
    }

    @Subscribe
    void on(RoleAssignedToUser event) {
        RoleId assignedRole = event.getRoleId();
        builder().addRole(assignedRole);
    }

    @Subscribe
    void on(RoleUnassignedFromUser event) {
        removeRole(event.getRoleId());
    }

    private void removeRole(RoleId roleId) {
        int roleIndex = builder().getRoleList()
                                 .indexOf(roleId);
        builder().removeRole(roleIndex);
    }
}
