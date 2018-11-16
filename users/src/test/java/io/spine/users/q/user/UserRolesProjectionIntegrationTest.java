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
import io.spine.testing.server.ShardingReset;
import io.spine.testing.server.blackbox.BlackBoxBoundedContext;
import io.spine.users.GroupId;
import io.spine.users.RoleId;
import io.spine.users.c.group.GroupPartRepository;
import io.spine.users.c.user.UserMembershipPartRepository;
import io.spine.users.c.user.UserPartRepository;
import io.spine.users.q.group.GroupViewProjectionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.google.common.collect.ImmutableSet.of;
import static io.spine.testing.server.blackbox.VerifyState.exactly;
import static io.spine.users.q.user.given.UserRolesProjectionTestEnv.assignRoleToGroup;
import static io.spine.users.q.user.given.UserRolesProjectionTestEnv.assignRoleToUser;
import static io.spine.users.q.user.given.UserRolesProjectionTestEnv.createGroup;
import static io.spine.users.q.user.given.UserRolesProjectionTestEnv.createUser;
import static io.spine.users.q.user.given.UserRolesProjectionTestEnv.groupUuid;
import static io.spine.users.q.user.given.UserRolesProjectionTestEnv.joinGroup;
import static io.spine.users.q.user.given.UserRolesProjectionTestEnv.roleUuid;
import static io.spine.users.q.user.given.UserRolesProjectionTestEnv.userUuid;

/**
 * {@link BlackBoxBoundedContext Integration tests} of {@link UserRolesProjection}.
 */
@ExtendWith(ShardingReset.class)
@DisplayName("UserRolesProjection should")
class UserRolesProjectionIntegrationTest {

    @Test
    @DisplayName("have explicitly assigned roles")
    void explicitlyAssignedRoles() {
        UserId user = userUuid();
        RoleId role = roleUuid();
        newBoundedContext()
                .receivesCommands(createUser(user),
                                  assignRoleToUser(user, role))
                .assertThat(exactly(UserRoles.class, of(
                        UserRoles.newBuilder()
                                 .setId(user)
                                 .addRole(role)
                                 .build()
                )));
    }

    @Test
    @DisplayName("have implicitly assigned roles")
    void implicitlyAssignedRoles() {
        UserId user = userUuid();
        RoleId role = roleUuid();
        GroupId group = groupUuid();
        GroupRoles expectedRoles = GroupRolesVBuilder.newBuilder()
                                                     .setGroup(group)
                                                     .addRole(role)
                                                     .build();
        newBoundedContext()
                .receivesCommands(createUser(user),
                                  createGroup(group),
                                  joinGroup(user, group),
                                  assignRoleToGroup(group, role))
                .assertThat(exactly(UserRoles.class, of(
                        UserRoles.newBuilder()
                                 .setId(user)
                                 .addGroupRole(expectedRoles)
                                 .build()
                )));
    }

    /** The bounded context with repositories related to {@link UserRolesProjection}. */
    private static BlackBoxBoundedContext newBoundedContext() {
        return BlackBoxBoundedContext.newInstance()
                                     .with(new UserPartRepository(),
                                           new UserMembershipPartRepository(),
                                           new GroupPartRepository(),
                                           new GroupViewProjectionRepository(),
                                           new UserRolesProjectionRepository());
    }
}
