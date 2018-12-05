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

package io.spine.users.user;

import io.spine.core.Subscribe;
import io.spine.core.UserId;
import io.spine.server.projection.Projection;
import io.spine.users.RoleId;
import io.spine.users.group.event.RoleDisinheritedByUser;
import io.spine.users.group.event.RoleInheritedByUser;
import io.spine.users.user.event.RoleAssignedToUser;
import io.spine.users.user.event.RoleUnassignedFromUser;
import io.spine.users.user.event.UserCreated;

/**
 * A projection of all user roles (both explicitly and implicitly assigned).
 */
public class UserRolesProjection extends Projection<UserId, UserRoles, UserRolesVBuilder> {

    protected UserRolesProjection(UserId id) {
        super(id);
    }

    @Subscribe
    public void on(UserCreated event) {
        UserId userId = event.getId();
        getBuilder().setId(userId);
    }

    @Subscribe
    public void on(RoleInheritedByUser event) {
        RoleId inheritedRole = event.getRoleId();
        getBuilder().addRole(inheritedRole);
    }

    @Subscribe
    public void on(RoleDisinheritedByUser event) {
        removeRole(event.getRoleId());
    }

    @Subscribe
    public void on(RoleAssignedToUser event) {
        RoleId assignedRole = event.getRoleId();
        getBuilder().addRole(assignedRole);
    }

    @Subscribe
    public void on(RoleUnassignedFromUser event) {
        removeRole(event.getRoleId());
    }

    private void removeRole(RoleId roleId) {
        int roleIndex = getBuilder().getRole()
                                    .indexOf(roleId);
        getBuilder().removeRole(roleIndex);
    }
}
