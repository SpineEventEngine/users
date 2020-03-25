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
import io.spine.roles.UserRoles;
import io.spine.roles.command.RemoveRoleAssignmentFromUser;
import io.spine.roles.event.RoleAssignmentRemovedFromUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("`UnassignRoleFromUser` command should")
class RemoveRoleAssignmentFromUserTest
        extends UserRolesCommandTest<RemoveRoleAssignmentFromUser, RoleAssignmentRemovedFromUser> {

    @Override
    @Test
    @DisplayName("generate `RoleUnassignedFromUser` and unassign the role from the user")
    protected void produceEventAndChangeState() {
        preCreateUser();
        super.produceEventAndChangeState();
    }

    @Override
    protected RemoveRoleAssignmentFromUser command(UserId id) {
        return removeRoleFromUser(id);
    }

    @Override
    protected RoleAssignmentRemovedFromUser expectedEventAfter(RemoveRoleAssignmentFromUser command) {
        return RoleAssignmentRemovedFromUser
                .newBuilder()
                .setUser(command.getUser())
                .setRole(command.getRole())
                .buildPartial();
    }

    @Override
    protected UserRoles expectedStateAfter(RemoveRoleAssignmentFromUser command) {
        return UserRoles
                .newBuilder()
                .setUser(command.getUser())
                .buildPartial();
    }

    static RemoveRoleAssignmentFromUser removeRoleFromUser(UserId user) {
        return RemoveRoleAssignmentFromUser
                .newBuilder()
                .setUser(user)
                .setRole(adminRoleId())
                .build();
    }
}
