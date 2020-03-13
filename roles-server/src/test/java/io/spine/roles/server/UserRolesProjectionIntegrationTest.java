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
import io.spine.server.BoundedContextBuilder;
import io.spine.testing.server.blackbox.BlackBoxBoundedContext;
import io.spine.users.GroupId;
import io.spine.roles.RoleId;
import io.spine.users.server.UsersContextTest;
import io.spine.roles.server.given.UserRolesProjectionTestEnv;
import io.spine.roles.UserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.spine.roles.server.given.TestCommands.assignRoleToGroup;
import static io.spine.roles.server.given.TestCommands.assignRoleToUser;
import static io.spine.roles.server.given.TestCommands.unassignRoleFromGroup;
import static io.spine.roles.server.given.TestCommands.unassignRoleFromUser;
import static io.spine.users.server.given.TestIdentifiers.groupId;
import static io.spine.roles.server.given.UserRolesProjectionTestCommands.createGroup;
import static io.spine.roles.server.given.UserRolesProjectionTestCommands.createRole;
import static io.spine.roles.server.given.UserRolesProjectionTestCommands.createUser;
import static io.spine.roles.server.given.UserRolesProjectionTestCommands.joinGroup;
import static io.spine.roles.server.given.UserRolesProjectionTestCommands.leaveGroup;
import static io.spine.roles.server.given.UserRolesProjectionTestEnv.roleUuid;
import static io.spine.roles.server.given.UserRolesProjectionTestEnv.userUuid;
import static io.spine.roles.server.given.UserRolesProjectionTestEnv.userWithRole;

/**
 * {@link BlackBoxBoundedContext Integration tests} of {@link UserRolesProjection}.
 */
@DisplayName("UserRolesProjection should")
class UserRolesProjectionIntegrationTest extends UsersContextTest {

    private UserId user;
    private RoleId role;
    private GroupId group;
    private UserRoles expectedRole;

    @Override
    protected BoundedContextBuilder contextBuilder() {
        return UsersContextWithRoles.extend(super.contextBuilder());
    }

    @BeforeEach
    void createUserRoleAndGroup() {
        user =  userUuid();
        role = roleUuid();
        group = groupId();
        context().receivesCommands(
                createUser(user),
                createRole(role),
                createGroup(group)
        );
        expectedRole = userWithRole(user, role);
    }

    private ProtoFluentAssertion assertRoles() {
        return context()
                .assertEntityWithState(UserRoles.class, user)
                .hasStateThat()
                .comparingExpectedFieldsOnly();
    }

    @Test
    @DisplayName("have explicitly assigned roles")
    void assignExplicitRoles() {
        context().receivesCommand(assignRoleToUser(user, role));

        assertRoles().isEqualTo(expectedRole);
    }

    @Test
    @DisplayName("inherit group roles when joining a group")
    void inheritAlreadyPresentGroupRoles() {
        context().receivesCommands(
                assignRoleToGroup(group, role),
                 // Join a group after the role assigned.
                 joinGroup(user, group)
        );

        assertRoles().isEqualTo(expectedRole);
    }

    @Test
    @DisplayName("inherit a group role after its assignment")
    void trackRoleChangesOfGroup() {
        context().receivesCommand(joinGroup(user, group))
                 // Assign a role when a user already joined a group
                 .receivesCommand(assignRoleToGroup(group, role));

        assertRoles().isEqualTo(expectedRole);
    }

    @Nested
    @DisplayName("when user has a role")
    class UserWithRole {

        @BeforeEach
        void assigningRoleToUser() {
            context().receivesCommand(assignRoleToUser(user, role));
        }

        @Test
        @DisplayName("remove if it is unassigned")
        void removeUnassignedRole() {
            context().receivesCommand(unassignRoleFromUser(user, role));

            assertRoles().isEqualTo(userWithoutRoles());
        }
    }

    @Nested
    @DisplayName("when user is in group with a role")
    class UserInGroupWithRole {

        @BeforeEach
        void joinGroupAndAssignRoleToGroup() {
            context().receivesCommands(
                    joinGroup(user, group),
                    assignRoleToGroup(group, role)
            );
        }

        @Test
        @DisplayName("remove role if it is unassigned from group")
        void removeUnassignedGroupRole() {
            context().receivesCommand(unassignRoleFromGroup(group, role));

            assertRoles().isEqualTo(userWithoutRoles());
        }

        @Test
        @DisplayName("remove role if user leaves group")
        void removeRoleOfLeftGroup() {
            context().receivesCommand(leaveGroup(user, group));

            assertRoles().isEqualTo(userWithoutRoles());
        }
    }

    private UserRoles userWithoutRoles() {
        return UserRolesProjectionTestEnv.userWithoutRoles(user);
    }
}
