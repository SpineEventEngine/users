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

package io.spine.users.server.role;

import io.spine.server.BoundedContextBuilder;
import io.spine.users.GroupId;
import io.spine.users.RoleId;
import io.spine.users.group.Group;
import io.spine.users.group.command.UnassignRoleFromGroup;
import io.spine.users.group.event.RoleUnassignedFromGroup;
import io.spine.users.group.rejection.Rejections.RoleIsNotAssignedToGroup;
import io.spine.users.server.given.TestIdentifiers;
import io.spine.users.server.GroupCommandTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.base.Identifier.newUuid;
import static io.spine.users.server.given.TestCommands.assignRoleToGroup;
import static io.spine.users.server.given.TestCommands.unassignRoleFromGroup;
import static io.spine.users.server.given.TestIdentifiers.orgId;
import static io.spine.users.server.group.given.GroupTestEnv.groupOrgEntityOrganization;
import static io.spine.users.server.role.RoleIds.roleId;

@DisplayName("`UnassignRoleFromGroup` command should")
class UnassignRoleFromGroupTest
        extends GroupCommandTest<UnassignRoleFromGroup, RoleUnassignedFromGroup> {

    private static final RoleId ROLE_ID = roleId(orgId(), newUuid());

    static RoleId groupRole() {
        return roleId(groupOrgEntityOrganization(), "administrator");
    }

    @Override
    protected BoundedContextBuilder contextBuilder() {
        return UsersContextWithRoles.extend(super.contextBuilder());
    }

    @Test
    @DisplayName("throw `RoleIsNotAssignedToGroup` if the role isn't assigned to the group")
    void throwsRejection() {
        GroupId someGroupId = TestIdentifiers.groupId();
        UnassignRoleFromGroup unassignRole = unassignRoleFromGroup(someGroupId, groupRole());

        context().receivesCommand(unassignRole)
                 .assertEvents()
                 .withType(RoleIsNotAssignedToGroup.class)
                 .hasSize(1);
    }

    @Test
    @DisplayName("produce `RoleAssignedToGroup` event and add a role to the `Group` state")
    @Override
    protected void produceEventAndChangeState() {
        preCreateGroup();
        context().receivesCommand(assignRoleToGroup(entityId(), ROLE_ID));
        super.produceEventAndChangeState();
    }

    @Override
    protected UnassignRoleFromGroup command(GroupId id) {
        return unassignRoleFromGroup(id, ROLE_ID);
    }

    @Override
    protected RoleUnassignedFromGroup expectedEventAfter(UnassignRoleFromGroup command) {
        return RoleUnassignedFromGroup
                .newBuilder()
                .setId(command.getId())
                .setRoleId(command.getRoleId())
                .build();
    }

    @Override
    protected Group expectedStateAfter(UnassignRoleFromGroup command) {
        return Group
                .newBuilder()
                .setId(command.getId())
                .build();
    }
}