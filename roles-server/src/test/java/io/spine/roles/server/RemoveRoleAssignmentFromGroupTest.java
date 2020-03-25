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
import io.spine.roles.command.RemoveRoleAssignmentFromGroup;
import io.spine.roles.event.RoleAssignmentRemovedFromGroup;
import io.spine.server.BoundedContextBuilder;
import io.spine.users.GroupId;
import io.spine.roles.RoleId;
import io.spine.roles.rejection.Rejections.RoleIsNotAssignedToGroup;
import io.spine.users.server.given.TestIdentifiers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.roles.server.given.TestCommands.assignRoleToGroup;
import static io.spine.roles.server.given.TestCommands.removeRoleFromGroup;

@DisplayName("`UnassignRoleFromGroup` command should")
class RemoveRoleAssignmentFromGroupTest
        extends GroupRolesCommandTest<RemoveRoleAssignmentFromGroup, RoleAssignmentRemovedFromGroup> {

    private static final RoleId REGULAR_ROLE = RoleId.generate();
    private static final RoleId ADMIN_ROLE = RoleId.generate();

    static RoleId groupRole() {
        return ADMIN_ROLE;
    }

    @Override
    protected BoundedContextBuilder contextBuilder() {
        return RolesContext.newBuilder();
    }

    @Test
    @DisplayName("throw `RoleIsNotAssignedToGroup` if the role isn't assigned to the group")
    void throwsRejection() {
        GroupId someGroupId = TestIdentifiers.groupId();
        RemoveRoleAssignmentFromGroup unassignRole = removeRoleFromGroup(someGroupId, groupRole());

        context().receivesCommand(unassignRole)
                 .assertEvents()
                 .withType(RoleIsNotAssignedToGroup.class)
                 .hasSize(1);
    }

    @Test
    @DisplayName("produce `RoleAssignedToGroup` event and add a role to the `Group` state")
    @Override
    protected void produceEventAndChangeState() {
        context().receivesCommand(assignRoleToGroup(entityId(), REGULAR_ROLE));
        super.produceEventAndChangeState();
    }

    @Override
    protected RemoveRoleAssignmentFromGroup command(GroupId id) {
        return removeRoleFromGroup(id, REGULAR_ROLE);
    }

    @Override
    protected RoleAssignmentRemovedFromGroup expectedEventAfter(
            RemoveRoleAssignmentFromGroup command) {
        return RoleAssignmentRemovedFromGroup
                .newBuilder()
                .setGroup(command.getGroup())
                .setRole(command.getRole())
                .build();
    }

    @Override
    protected GroupRoles expectedStateAfter(RemoveRoleAssignmentFromGroup command) {
        return GroupRoles
                .newBuilder()
                .setGroup(command.getGroup())
                .build();
    }
}
