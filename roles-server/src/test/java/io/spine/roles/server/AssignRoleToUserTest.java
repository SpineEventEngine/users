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
import io.spine.roles.UserRolesV2;
import io.spine.testing.server.blackbox.MultitenantBlackBoxContext;
import io.spine.roles.RoleId;
import io.spine.roles.command.AssignRoleToUser;
import io.spine.roles.event.RoleAssignedToUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.roles.server.RoleIds.roleId;
import static io.spine.users.server.user.given.UserTestEnv.userOrgEntity;

@DisplayName("`AssignRoleToUser` command should")
class AssignRoleToUserTest extends UserRolesCommandTest<AssignRoleToUser, RoleAssignedToUser> {

    @Override
    @Test
    @DisplayName("generate `RoleAssignedToUser` assign the role to the user")
    protected void produceEventAndChangeState() {
        preCreateUser();
        super.produceEventAndChangeState();
    }

    @Override
    protected void assertEvent(MultitenantBlackBoxContext afterCommand,
                               RoleAssignedToUser expectedEvent) {
        /*
         The second event of {@code RoleAssignedToUser} type is checked,
         as the {@link #preCreateUser} also leads to the {@code RoleAssignedToUser} event.
         */
        afterCommand.assertEvents()
                    .withType(expectedEvent.getClass())
                    .message(1)
                    .comparingExpectedFieldsOnly()
                    .isEqualTo(expectedEvent);
    }

    @Override
    protected AssignRoleToUser command(UserId id) {
        return assignRoleToUser(id);
    }

    @Override
    protected RoleAssignedToUser expectedEventAfter(AssignRoleToUser command) {
        return RoleAssignedToUser
                .newBuilder()
                .setId(command.getId())
                .setRoleId(command.getRoleId())
                .buildPartial();
    }

    @Override
    protected UserRolesV2 expectedStateAfter(AssignRoleToUser command) {
        return UserRolesV2
                .newBuilder()
                .setUser(command.getId())

                // The role which was set originally.
                .addExplicitRole(originalRole())

                // The one expected to be added after the command dispatching.
                .addExplicitRole(command.getRoleId())
                .buildPartial();
    }

    static AssignRoleToUser assignRoleToUser(UserId id) {
        return AssignRoleToUser
                .newBuilder()
                .setId(id)
                .setRoleId(editorRoleId())
                .build();
    }

    static RoleId editorRoleId() {
        return roleId(userOrgEntity(), "editor_role");
    }
}
