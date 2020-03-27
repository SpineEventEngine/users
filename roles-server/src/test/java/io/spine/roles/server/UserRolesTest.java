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
import io.spine.roles.UserRoles;
import io.spine.roles.event.RoleAssignedToUser;
import io.spine.roles.event.RoleAssignmentRemovedFromUser;
import io.spine.roles.event.UserInheritedRole;
import io.spine.users.GroupId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.spine.roles.server.given.Given.userUuid;
import static io.spine.roles.server.given.Given.userWithAssignedRole;
import static io.spine.roles.server.given.Given.userWithInheritedRole;
import static io.spine.roles.server.given.Given.userWithoutRoles;
import static io.spine.roles.server.given.GivenCommand.createGroup;
import static io.spine.roles.server.given.GivenCommand.createRole;
import static io.spine.roles.server.given.GivenCommand.createUser;
import static io.spine.roles.server.given.GivenCommand.joinGroup;
import static io.spine.roles.server.given.GivenCommand.leaveGroup;
import static io.spine.roles.server.given.TestCommands.assignRoleToGroup;
import static io.spine.roles.server.given.TestCommands.assignRoleToUser;
import static io.spine.roles.server.given.TestCommands.removeRoleFromGroup;
import static io.spine.roles.server.given.TestCommands.removeRoleFromUser;
import static io.spine.users.server.given.TestIdentifiers.groupId;

@DisplayName("A user should")
class UserRolesTest extends RolesContextTest {

    private UserId user;
    private GroupId group;
    private RoleId role;

    @BeforeEach
    void createUserRoleAndGroup() {
        user =  userUuid();
        group = groupId();
        usersContext().receivesCommands(
                createUser(user),
                createGroup(group)
        );
        role = RoleId.generate();
        rolesContext().receivesCommand(createRole(role));
    }

    private ProtoFluentAssertion assertRoles() {
        return assertRolesOf(this.user);
    }

    @Nested
    @DisplayName("have a role")
    class ExplicitAssignment {

        @BeforeEach
        void assigningRoleToUser() {
            rolesContext().receivesCommand(assignRoleToUser(user, role));
        }

        @Test
        @DisplayName("assigned")
        void assigned() {
            assertEvent(
                    RoleAssignedToUser
                            .newBuilder()
                            .setUser(user)
                            .setRole(role)
                            .vBuild()
            );

            UserRoles expected = userWithAssignedRole(user, role);
            assertRoles().isEqualTo(expected);
        }

        @Test
        @DisplayName("removed")
        void removeUnassignedRole() {
            rolesContext().receivesCommand(removeRoleFromUser(user, role));

            assertEvent(
                    RoleAssignmentRemovedFromUser
                            .newBuilder()
                            .setRole(role)
                            .setUser(user)
                            .vBuild()
            );

            assertRoles().isEqualTo(userWithoutRoles(user));
        }
    }

    @Nested
    @DisplayName("inherit a group role")
    class InheritGroupRole {

        @Test
        @DisplayName("when joining a group")
        void inheritAlreadyPresentGroupRoles() {
            rolesContext().receivesCommand(assignRoleToGroup(group, role));
            // Join a group after the role assigned.
            usersContext().receivesCommand(joinGroup(user, group));

            assertInheritedEvent();
            assertRolesState();
        }

        @Test
        @DisplayName("after the role is assigned to the group")
        void inheritFromGroup() {
            usersContext().receivesCommand(joinGroup(user, group));
            // Assign a role when a user already joined a group
            rolesContext().receivesCommand(assignRoleToGroup(group, role));

            assertInheritedEvent();
            assertRolesState();
        }

        private void assertInheritedEvent() {
            assertEvent(
                    UserInheritedRole
                            .newBuilder()
                            .setUser(user)
                            .setGroup(group)
                            .setRole(role)
                            .vBuild()
            );
        }

        private void assertRolesState() {
            UserRoles expected = userWithInheritedRole(user, group, role);
            assertRoles().isEqualTo(expected);
        }
    }


    @Nested
    @DisplayName("when being in a group with a role, lose the role")
    class UserInGroupWithRole {

        @BeforeEach
        void joinGroupAndAssignRoleToGroup() {
            usersContext().receivesCommand(joinGroup(user, group));
            rolesContext().receivesCommand(assignRoleToGroup(group, role));
        }

        @Test
        @DisplayName("when its assignment removed from the group")
        void removeUnassignedGroupRole() {
            rolesContext().receivesCommand(removeRoleFromGroup(group, role));

            assertRoles().isEqualTo(userWithoutRoles(user));
        }

        @Test
        @DisplayName("when being removed from the group")
        void removeRoleOfLeftGroup() {
            usersContext().receivesCommand(leaveGroup(user, group));

            assertRoles().isEqualTo(userWithoutRoles(user));
        }
    }
}
