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

package io.spine.users.server.group;

import io.spine.testing.server.blackbox.SingleTenantBlackBoxContext;
import io.spine.users.group.Group;
import io.spine.users.group.command.AssignRoleToGroup;
import io.spine.users.group.event.RoleAssignedToGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.base.Identifier.newUuid;
import static io.spine.users.server.given.GivenCommand.assignRoleToGroup;
import static io.spine.users.server.given.GivenId.organizationUuid;
import static io.spine.users.server.role.RoleIds.roleId;

@DisplayName("`AssignRoleToGroup` command should")
class AssignRoleToGroupTest extends GroupCommandTest<AssignRoleToGroup> {

    @Test
    @DisplayName("produce `RoleAssignedToGroup` event and add a role to the `Group` state")
    void produceEventAndChangeState() {
        createPartWithState();
        AssignRoleToGroup assignRole = assignRoleToGroup(GROUP_ID,
                                                         roleId(organizationUuid(), newUuid()));
        SingleTenantBlackBoxContext afterCommand = context().receivesCommand(assignRole);
        RoleAssignedToGroup expectedEvent = expectedEvent(assignRole);
        afterCommand.assertEvents()
                    .message(0)
                    .comparingExpectedFieldsOnly()
                    .isEqualTo(expectedEvent);

        Group expectedState = expectedState(assignRole);
        afterCommand.assertEntity(GroupPart.class, GROUP_ID)
                    .hasStateThat()
                    .comparingExpectedFieldsOnly()
                    .isEqualTo(expectedState);
    }

    private static Group expectedState(AssignRoleToGroup assignRole) {
        return Group
                .newBuilder()
                .setId(GROUP_ID)
                .addRole(assignRole.getRoleId())
                .build();
    }

    private static RoleAssignedToGroup expectedEvent(AssignRoleToGroup assignRole) {
        return RoleAssignedToGroup
                .newBuilder()
                .setId(assignRole.getId())
                .setRoleId(assignRole.getRoleId())
                .build();
    }
}
