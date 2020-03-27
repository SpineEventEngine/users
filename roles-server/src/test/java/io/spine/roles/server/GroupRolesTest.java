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

import com.google.common.truth.extensions.proto.ProtoFluentAssertion;
import io.spine.roles.GroupRoles;
import io.spine.roles.RoleId;
import io.spine.roles.event.RoleAssignedToGroup;
import io.spine.roles.event.RoleAssignmentRemovedFromGroup;
import io.spine.roles.rejection.Rejections;
import io.spine.users.GroupId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.spine.roles.server.given.GivenCommand.createGroup;
import static io.spine.roles.server.given.GivenCommand.createRole;
import static io.spine.roles.server.given.GivenCommand.assignRoleToGroup;
import static io.spine.roles.server.given.GivenCommand.removeRoleFromGroup;
import static io.spine.testing.TestValues.randomString;
import static io.spine.users.server.given.TestIdentifiers.groupId;

@DisplayName("A group should")
class GroupRolesTest extends RolesContextTest {

    private GroupId group;
    private RoleId role;

    @BeforeEach
    void createGroupAndRole() {
        group = groupId();
        usersContext().receivesCommand(createGroup(group));
        role = RoleId.generate();
        rolesContext().receivesCommand(createRole(role, randomString()));
    }

    @Nested
    @DisplayName("have a role")
    class Assignment {

        @BeforeEach
        void assignRole() {
            rolesContext().receivesCommand(assignRoleToGroup(role, group));
        }

        @Test
        @DisplayName("assigned")
        void assigned() {
            assertEvent(
                    RoleAssignedToGroup
                            .newBuilder()
                            .setRole(role)
                            .setGroup(group)
                            .vBuild()
            );

            GroupRoles expected = groupWithAssignedRole(group, role);
            assertRoles().isEqualTo(expected);
        }

        @Test
        @DisplayName("removed")
        void removed() {
            rolesContext().receivesCommand(removeRoleFromGroup(role, group));

            assertEvent(
                    RoleAssignmentRemovedFromGroup
                            .newBuilder()
                            .setGroup(group)
                            .setRole(role)
                            .vBuild()
            );

            GroupRoles expected = groupWithoutRoles(group);
            assertRoles().isEqualTo(expected);
        }
    }

    @Test
    @DisplayName("throw `RoleIsNotAssignedToGroup` if the role isn't assigned to the group")
    void throwsRejection() {
        RoleId unassigned = RoleId.generate();
        rolesContext().receivesCommand(assignRoleToGroup(role, group));
        
        rolesContext().receivesCommand(removeRoleFromGroup(unassigned, group));

        assertEvent(
                Rejections.RoleIsNotAssignedToGroup
                        .newBuilder()
                        .setGroup(group)
                        .setRole(unassigned)
                        .build()
        );
        // Assert nothing was removed.
        assertRoles().isEqualTo(groupWithAssignedRole(group, role));
    }

    private static GroupRoles groupWithAssignedRole(GroupId group, RoleId role) {
        return GroupRoles
                .newBuilder()
                .setGroup(group)
                .addAssigned(role)
                .vBuild();
    }

    private static GroupRoles groupWithoutRoles(GroupId group) {
        return GroupRoles
                .newBuilder()
                .setGroup(group)
                .vBuild();
    }

    private ProtoFluentAssertion assertRoles() {
        return assertRolesOf(this.group);
    }

    private ProtoFluentAssertion assertRolesOf(GroupId group) {
        return rolesContext()
                .assertEntityWithState(GroupRoles.class, group)
                .hasStateThat()
                .comparingExpectedFieldsOnly();
    }
}
