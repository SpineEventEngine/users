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

import com.google.common.collect.ImmutableList;
import io.spine.base.CommandMessage;
import io.spine.base.EventMessage;
import io.spine.core.UserId;
import io.spine.roles.UserRolesV2;
import io.spine.roles.command.AssignRoleToUser;
import io.spine.server.BoundedContextBuilder;
import io.spine.roles.RoleId;
import io.spine.users.server.CommandTest;

import static io.spine.roles.server.RoleIds.roleId;
import static io.spine.users.server.user.given.UserTestEnv.userId;
import static io.spine.users.server.user.given.UserTestEnv.userOrgEntity;

public abstract class UserRolesCommandTest<C extends CommandMessage, E extends EventMessage>
        extends CommandTest<UserId, C, E, UserRolesV2, UserRolesAggregate> {

    private static final UserId USER_ID = userId();

    @Override
    protected UserId entityId() {
        return USER_ID;
    }

    @Override
    protected Class<UserRolesAggregate> entityClass() {
        return UserRolesAggregate.class;
    }

    @Override
    protected BoundedContextBuilder contextBuilder() {
        return RolesContext.extend(super.contextBuilder());
    }

    protected Iterable<CommandMessage> setupCommands() {
        AssignRoleToUser assignRole = AssignRoleToUser
                .newBuilder()
                .setId(entityId())
                .setRoleId(originalRole())
                .vBuild();
        ImmutableList.Builder<CommandMessage> commands = ImmutableList.builder();
        commands.add(assignRole);
        return commands.build();
    }

    protected void preCreateUser() {
        setupCommands().forEach(context()::receivesCommand);
    }

    static RoleId adminRoleId() {
        return roleId(userOrgEntity(), "admin_role");
    }

    RoleId originalRole() {
        return adminRoleId();
    }
}
