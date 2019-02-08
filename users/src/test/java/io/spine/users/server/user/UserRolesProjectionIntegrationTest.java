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

package io.spine.users.server.user;

import io.spine.core.UserId;
import io.spine.testing.server.blackbox.BlackBoxBoundedContext;
import io.spine.users.GroupId;
import io.spine.users.RoleId;
import io.spine.users.server.group.GroupPartRepository;
import io.spine.users.server.group.GroupRolesPropagationRepository;
import io.spine.users.server.role.RoleAggregateRepository;
import io.spine.users.server.user.given.UserRolesProjectionTestEnv;
import io.spine.users.user.UserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.spine.testing.server.blackbox.verify.state.VerifyState.exactlyOne;
import static io.spine.users.server.given.GivenCommand.assignRoleToGroup;
import static io.spine.users.server.given.GivenCommand.assignRoleToUser;
import static io.spine.users.server.given.GivenCommand.unassignRoleFromGroup;
import static io.spine.users.server.given.GivenCommand.unassignRoleFromUser;
import static io.spine.users.server.given.GivenId.groupUuid;
import static io.spine.users.server.user.given.UserRolesProjectionTestCommands.createGroup;
import static io.spine.users.server.user.given.UserRolesProjectionTestCommands.createRole;
import static io.spine.users.server.user.given.UserRolesProjectionTestCommands.createUser;
import static io.spine.users.server.user.given.UserRolesProjectionTestCommands.joinGroup;
import static io.spine.users.server.user.given.UserRolesProjectionTestCommands.leaveGroup;
import static io.spine.users.server.user.given.UserRolesProjectionTestEnv.roleUuid;
import static io.spine.users.server.user.given.UserRolesProjectionTestEnv.userUuid;
import static io.spine.users.server.user.given.UserRolesProjectionTestEnv.userWithRole;

/**
 * {@link BlackBoxBoundedContext Integration tests} of {@link UserRolesProjection}.
 */
@DisplayName("UserRolesProjection should")
class UserRolesProjectionIntegrationTest {

    private final UserId user = userUuid();
    private BlackBoxBoundedContext boundedContext;

    @BeforeEach
    void setUp() {
        boundedContext = newBoundedContext();
    }

    @Test
    @DisplayName("have explicitly assigned roles")
    void assignExplicitRoles() {
        RoleId roleId = roleUuid();
        UserRoles expectedRoles = userWithRole(user, roleId);
        boundedContext.receivesCommands(createUser(user),
                                        createRole(roleId),
                                        assignRoleToUser(user, roleId))
                      .assertThat(exactlyOne(expectedRoles));
    }

    @Test
    @DisplayName("inherit group roles when joining a group")
    void inheritAlreadyPresentGroupRoles() {
        RoleId role = roleUuid();
        GroupId group = groupUuid();
        UserRoles expectedRoles = userWithRole(user, role);
        boundedContext.receivesCommands(createUser(user),
                                        createRole(role),
                                        createGroup(group),
                                        assignRoleToGroup(group, role))
                      // Join a group after the role assigned.
                      .receivesCommand(joinGroup(user, group))
                      .assertThat(exactlyOne(expectedRoles));
    }

    @Test
    @DisplayName("inherit a group role after its assignment")
    void trackRoleChangesOfGroup() {
        RoleId role = roleUuid();
        GroupId group = groupUuid();
        UserRoles expectedRoles = userWithRole(user, role);
        boundedContext.receivesCommands(createUser(user),
                                        createRole(role),
                                        createGroup(group),
                                        joinGroup(user, group))
                      // Assign a role when a user already joined a group
                      .receivesCommand(assignRoleToGroup(group, role))
                      .assertThat(exactlyOne(expectedRoles));
    }

    @Nested
    @DisplayName("when user has role")
    class UserWithRole {

        private final RoleId role = roleUuid();

        @BeforeEach
        void setUp() {
            boundedContext.receivesCommands(createUser(user),
                                            createRole(role),
                                            assignRoleToUser(user, role));
        }

        @Test
        @DisplayName("remove if it is unassigned")
        void removeUnassignedRole() {
            boundedContext.receivesCommand(unassignRoleFromUser(user, role))
                          .assertThat(exactlyOne(userWithoutRoles()));
        }
    }

    @Nested
    @DisplayName("when user is in group with a role")
    class UserInGroupWithRole {

        private final RoleId role = roleUuid();
        private final GroupId group = groupUuid();

        @BeforeEach
        void setUp() {
            boundedContext.receivesCommands(createUser(user),
                                            createRole(role),
                                            createGroup(group),
                                            joinGroup(user, group),
                                            assignRoleToGroup(group, role));
        }

        @Test
        @DisplayName("remove role if it is unassigned from group")
        void removeUnassignedGroupRole() {
            boundedContext.receivesCommand(unassignRoleFromGroup(group, role))
                          .assertThat(exactlyOne(userWithoutRoles()));
        }

        @Test
        @DisplayName("remove role if user leaves group")
        void removeRoleOfLeftGroup() {
            boundedContext.receivesCommand(leaveGroup(user, group))
                          .assertThat(exactlyOne(userWithoutRoles()));
        }
    }

    private UserRoles userWithoutRoles() {
        return UserRolesProjectionTestEnv.userWithoutRoles(user);
    }

    /** The bounded context with repositories related to {@link UserRolesProjection}. */
    private static BlackBoxBoundedContext newBoundedContext() {
        return BlackBoxBoundedContext.singleTenant()
                                     .with(new UserPartRepository(),
                                           new UserMembershipPartRepository(),
                                           new RoleAggregateRepository(),
                                           new GroupPartRepository(),
                                           new GroupRolesPropagationRepository(),
                                           new UserRolesRepository());
    }
}
