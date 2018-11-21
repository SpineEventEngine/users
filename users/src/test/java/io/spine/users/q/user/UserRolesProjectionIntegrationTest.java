/*
 * Copyright 2018, TeamDev. All rights reserved.
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

package io.spine.users.q.user;

import io.spine.core.UserId;
import io.spine.server.event.Enricher;
import io.spine.testing.server.ShardingReset;
import io.spine.testing.server.blackbox.BlackBoxBoundedContext;
import io.spine.users.GroupId;
import io.spine.users.RoleId;
import io.spine.users.UsersEnricher;
import io.spine.users.c.group.GroupPartRepository;
import io.spine.users.c.role.RoleAggregateRepository;
import io.spine.users.c.user.UserMembershipPartRepository;
import io.spine.users.c.user.UserPartRepository;
import io.spine.users.q.group.GroupViewProjectionRepository;
import io.spine.users.q.user.given.UserRolesProjectionTestEnv;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.spine.base.Identifier.newUuid;
import static io.spine.testing.server.blackbox.verify.state.VerifyState.exactlyOne;
import static io.spine.users.q.user.given.UserRolesProjectionTestCommands.assignRoleToGroup;
import static io.spine.users.q.user.given.UserRolesProjectionTestCommands.assignRoleToUser;
import static io.spine.users.q.user.given.UserRolesProjectionTestCommands.createGroup;
import static io.spine.users.q.user.given.UserRolesProjectionTestCommands.createRole;
import static io.spine.users.q.user.given.UserRolesProjectionTestCommands.createUser;
import static io.spine.users.q.user.given.UserRolesProjectionTestCommands.joinGroup;
import static io.spine.users.q.user.given.UserRolesProjectionTestCommands.leaveGroup;
import static io.spine.users.q.user.given.UserRolesProjectionTestCommands.unassignRoleFromGroup;
import static io.spine.users.q.user.given.UserRolesProjectionTestCommands.unassignRoleFromUser;
import static io.spine.users.q.user.given.UserRolesProjectionTestEnv.groupUuid;
import static io.spine.users.q.user.given.UserRolesProjectionTestEnv.newRole;
import static io.spine.users.q.user.given.UserRolesProjectionTestEnv.roleUuid;
import static io.spine.users.q.user.given.UserRolesProjectionTestEnv.userUuid;

/**
 * {@link BlackBoxBoundedContext Integration tests} of {@link UserRolesProjection}.
 */
@ExtendWith(ShardingReset.class)
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
        String roleName = newUuid();
        UserRoles expectedRoles = UserRoles.newBuilder()
                                           .setId(user)
                                           .addRole(newRole(roleId, roleName))
                                           .build();
        boundedContext.receivesCommands(createUser(user),
                                        createRole(roleId, roleName),
                                        assignRoleToUser(user, roleId))
                      .assertThat(exactlyOne(expectedRoles));
    }

    @Test
    @DisplayName("have implicitly assigned roles")
    void implicitlyAssignedRoles() {
        RoleId role = roleUuid();
        GroupId group = groupUuid();
        GroupRoles expectedGroupRoles = GroupRolesVBuilder.newBuilder()
                                                          .setGroup(group)
                                                          .addRole(role)
                                                          .build();
        UserRoles expectedRoles = UserRoles.newBuilder()
                                           .setId(user)
                                           .addGroupRole(expectedGroupRoles)
                                           .build();
        boundedContext.receivesCommands(createUser(user),
                                        createGroup(group),
                                        joinGroup(user, group),
                                        assignRoleToGroup(group, role))
                      .assertThat(exactlyOne(expectedRoles));
    }

    @Nested
    @DisplayName("when user has role")
    class UserWithRole {

        private final RoleId role = roleUuid();
        private final String roleName = newUuid();

        @BeforeEach
        void setUp() {
            boundedContext.receivesCommands(createUser(user),
                                            createRole(role, roleName),
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
        RoleAggregateRepository roleAggregateRepository = new RoleAggregateRepository();
        Enricher enricher = UsersEnricher.create(roleAggregateRepository);
        return BlackBoxBoundedContext.singletenant(enricher)
                                     .with(new UserPartRepository(),
                                           new UserMembershipPartRepository(),
                                           roleAggregateRepository,
                                           new GroupPartRepository(),
                                           new GroupViewProjectionRepository(),
                                           new UserRolesProjectionRepository());
    }
}
