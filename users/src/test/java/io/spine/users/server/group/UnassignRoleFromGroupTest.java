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
import io.spine.users.GroupId;
import io.spine.users.group.Group;
import io.spine.users.group.command.AssignRoleToGroup;
import io.spine.users.group.command.UnassignRoleFromGroup;
import io.spine.users.group.event.RoleUnassignedFromGroup;
import io.spine.users.group.rejection.Rejections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.base.Identifier.newUuid;
import static io.spine.users.server.given.GivenCommand.assignRoleToGroup;
import static io.spine.users.server.given.GivenCommand.unassignRoleFromGroup;
import static io.spine.users.server.given.GivenId.organizationUuid;
import static io.spine.users.server.group.given.GroupTestEnv.createGroupId;
import static io.spine.users.server.group.given.GroupTestEnv.groupRole;
import static io.spine.users.server.role.RoleIds.roleId;

@DisplayName("`UnassignRoleFromGroup` command should")
class UnassignRoleFromGroupTest extends GroupCommandTest<UnassignRoleFromGroup> {

    @Test
    @DisplayName("throw `RoleIsNotAssignedToGroup` if the role isn't assigned to the group")
    void throwsRejection() {
        GroupId someGroupId = createGroupId();
        UnassignRoleFromGroup unassignRole = unassignRoleFromGroup(someGroupId, groupRole());

        context().receivesCommand(unassignRole)
                 .assertRejectedWith(Rejections.RoleIsNotAssignedToGroup.class);
    }

    @Test
    @DisplayName("produce `RoleAssignedToGroup` event and add a role to the `Group` state")
    void produceEventAndChangeState() {
        createPartWithState();
        AssignRoleToGroup assignRole =
                assignRoleToGroup(GROUP_ID, roleId(organizationUuid(), newUuid()));
        UnassignRoleFromGroup unassignRole =
                unassignRoleFromGroup(GROUP_ID, assignRole.getRoleId());

        SingleTenantBlackBoxContext afterCommand =
                context().receivesCommands(assignRole, unassignRole);
        RoleUnassignedFromGroup expectedEvent = expectedEvent(unassignRole);
        afterCommand.assertEvents()
                    .message(1)
                    .comparingExpectedFieldsOnly()
                    .isEqualTo(expectedEvent);

        Group expectedState = expectedState(unassignRole);
        afterCommand.assertEntity(GroupPart.class, GROUP_ID)
                    .hasStateThat()
                    .comparingExpectedFieldsOnly()
                    .isEqualTo(expectedState);
    }

    private static Group expectedState(UnassignRoleFromGroup command) {
        return Group
                .newBuilder()
                .setId(command.getId())
                .build();
    }

    private static RoleUnassignedFromGroup expectedEvent(UnassignRoleFromGroup command) {
        return RoleUnassignedFromGroup
                .newBuilder()
                .setId(command.getId())
                .setRoleId(command.getRoleId())
                .build();
    }
}
