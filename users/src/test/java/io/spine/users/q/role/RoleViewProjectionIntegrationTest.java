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

package io.spine.users.q.role;

import io.spine.core.UserId;
import io.spine.testing.core.given.GivenUserId;
import io.spine.testing.server.ShardingReset;
import io.spine.testing.server.blackbox.BlackBoxBoundedContext;
import io.spine.users.GroupId;
import io.spine.users.RoleId;
import io.spine.users.c.group.GroupPartRepository;
import io.spine.users.c.role.RoleAggregateRepository;
import io.spine.users.c.user.UserPartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.spine.testing.server.blackbox.verify.state.VerifyState.exactlyOne;
import static io.spine.users.given.GivenCommand.assignRoleToGroup;
import static io.spine.users.given.GivenCommand.assignRoleToUser;
import static io.spine.users.given.GivenCommand.unassignRoleFromGroup;
import static io.spine.users.given.GivenCommand.unassignRoleFromUser;
import static io.spine.users.given.GivenId.groupUuid;
import static io.spine.users.given.GivenId.roleUuid;
import static io.spine.users.q.role.given.RoleViewTestEnv.createGroup;
import static io.spine.users.q.role.given.RoleViewTestEnv.createRole;
import static io.spine.users.q.role.given.RoleViewTestEnv.createUser;
import static io.spine.users.q.role.given.RoleViewTestEnv.createdRoleView;
import static io.spine.users.q.role.given.RoleViewTestEnv.displayName;
import static io.spine.users.q.role.given.RoleViewTestEnv.orgEntity;

@ExtendWith(ShardingReset.class)
@DisplayName("RoleViewProjection should")
class RoleViewProjectionIntegrationTest {

    private BlackBoxBoundedContext boundedContext;
    private final RoleId role = roleUuid();

    @BeforeEach
    void setUp() {
        boundedContext = newBoundedContext();
    }

    @Test
    @DisplayName("initialize state on RoleCreated")
    void roleCreated() {
        RoleView expectedState = RoleViewVBuilder.newBuilder()
                                                 .setId(role)
                                                 .setDisplayName(displayName())
                                                 .setOrgEntity(orgEntity())
                                                 .build();
        boundedContext.receivesCommand(createRole(role))
                      .assertThat(exactlyOne(expectedState));
    }

    @Nested
    @DisplayName("when created")
    class AfterCreation {

        @BeforeEach
        void setUp() {
            boundedContext.receivesCommand(createRole(role));
        }

        @Test
        @DisplayName("be aware of users with this role")
        void awareOfUsers() {
            UserId user = GivenUserId.newUuid();
            RoleView expectedState = createdRoleView(role).addUser(user)
                                                          .build();
            boundedContext.receivesCommands(createUser(user),
                                            assignRoleToUser(user, role))
                          .assertThat(exactlyOne(expectedState));
        }

        @Test
        @DisplayName("be aware of groups with this role")
        void awareOfGroups() {
            GroupId group = groupUuid();
            RoleView expectedState = createdRoleView(role).addGroup(group)
                                                          .build();
            boundedContext.receivesCommands(createGroup(group),
                                            assignRoleToGroup(group, role))
                          .assertThat(exactlyOne(expectedState));
        }

        @Nested
        @DisplayName("and assigned to a user")
        class AfterAssignmentToUser {

            private final UserId user = GivenUserId.newUuid();

            @BeforeEach
            void setUp() {
                boundedContext.receivesCommands(createUser(user),
                                                assignRoleToUser(user, role));
            }

            @Test
            @DisplayName("then be unassigned")
            void unassigned() {
                RoleView expectedState = createdRoleView(role).build();
                boundedContext.receivesCommand(unassignRoleFromUser(user, role))
                              .assertThat(exactlyOne(expectedState));
            }
        }

        @Nested
        @DisplayName("and assigned to a group")
        class AfterAssignmentToGroup {

            private final GroupId group = groupUuid();

            @BeforeEach
            void setUp() {
                boundedContext.receivesCommands(createGroup(group),
                                                assignRoleToGroup(group, role));
            }

            @Test
            @DisplayName("then be unassigned")
            void unassigned() {
                RoleView expectedState = createdRoleView(role).build();
                boundedContext.receivesCommand(unassignRoleFromGroup(group, role))
                              .assertThat(exactlyOne(expectedState));
            }
        }
    }

    private static BlackBoxBoundedContext newBoundedContext() {
        return BlackBoxBoundedContext.newInstance()
                                     .with(new UserPartRepository(),
                                           new GroupPartRepository(),
                                           new RoleAggregateRepository(),
                                           new RoleViewRepository());
    }
}
