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
import io.spine.core.UserId;
import io.spine.roles.RoleId;
import io.spine.roles.UserRolesV2;
import io.spine.roles.server.given.Given;
import io.spine.users.GroupId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.spine.roles.server.given.TestCommands.assignRoleToGroup;
import static io.spine.roles.server.given.TestCommands.assignRoleToUser;
import static io.spine.roles.server.given.TestCommands.unassignRoleFromGroup;
import static io.spine.roles.server.given.TestCommands.unassignRoleFromUser;
import static io.spine.roles.server.given.GivenCommand.createGroup;
import static io.spine.roles.server.given.GivenCommand.createRole;
import static io.spine.roles.server.given.GivenCommand.createUser;
import static io.spine.roles.server.given.GivenCommand.joinGroup;
import static io.spine.roles.server.given.GivenCommand.leaveGroup;
import static io.spine.roles.server.given.Given.roleUuid;
import static io.spine.roles.server.given.Given.userUuid;
import static io.spine.roles.server.given.Given.userWithRole;
import static io.spine.users.server.given.TestIdentifiers.groupId;

@DisplayName("`UserRolesAggregate` should")
class UserRolesTest extends RolesContextTest {

    private UserId user;
    private GroupId group;
    private RoleId role;
    private UserRolesV2 expectedRoles;

    @BeforeEach
    void createUserRoleAndGroup() {
        user =  userUuid();
        group = groupId();
        usersContext().receivesCommands(
                createUser(user),
                createGroup(group)
        );
        role = roleUuid();
        rolesContext().receivesCommand(createRole(role));
        expectedRoles = userWithRole(user, role);
    }

    private ProtoFluentAssertion assertRoles() {
        return rolesContext()
                .assertEntityWithState(UserRolesV2.class, user)
                .hasStateThat()
                .comparingExpectedFieldsOnly();
    }

    @Test
    @DisplayName("have explicitly assigned roles")
    void assignExplicitRoles() {
        rolesContext().receivesCommand(assignRoleToUser(user, role));

        assertRoles().isEqualTo(expectedRoles);
    }

    @Test
    @DisplayName("inherit group roles when joining a group")
    void inheritAlreadyPresentGroupRoles() {
        rolesContext().receivesCommand(assignRoleToGroup(group, role));
        // Join a group after the role assigned.
        usersContext().receivesCommand(joinGroup(user, group));

        assertRoles().isEqualTo(expectedRoles);
    }

    @Test
    @DisplayName("inherit a group role after its assignment")
    void trackRoleChangesOfGroup() {
        usersContext().receivesCommand(joinGroup(user, group));
        // Assign a role when a user already joined a group
        rolesContext().receivesCommand(assignRoleToGroup(group, role));

        assertRoles().isEqualTo(expectedRoles);
    }

    @Nested
    @DisplayName("when a user has a role")
    class UserWithRole {

        @BeforeEach
        void assigningRoleToUser() {
            rolesContext().receivesCommand(assignRoleToUser(user, role));
        }

        @Test
        @DisplayName("remove if it is unassigned")
        void removeUnassignedRole() {
            rolesContext().receivesCommand(unassignRoleFromUser(user, role));

            assertRoles().isEqualTo(userWithoutRoles());
        }
    }

    @Nested
    @DisplayName("when user is in group with a role")
    class UserInGroupWithRole {

        @BeforeEach
        void joinGroupAndAssignRoleToGroup() {
            usersContext().receivesCommand(joinGroup(user, group));
            rolesContext().receivesCommand(assignRoleToGroup(group, role));
        }

        @Test
        @DisplayName("remove role if it is unassigned from group")
        void removeUnassignedGroupRole() {
            rolesContext().receivesCommand(unassignRoleFromGroup(group, role));

            assertRoles().isEqualTo(userWithoutRoles());
        }

        @Test
        @DisplayName("remove role if user leaves group")
        void removeRoleOfLeftGroup() {
            usersContext().receivesCommand(leaveGroup(user, group));

            assertRoles().isEqualTo(userWithoutRoles());
        }
    }

    private UserRolesV2 userWithoutRoles() {
        return Given.userWithoutRoles(user);
    }
}
