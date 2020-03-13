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

import com.google.common.collect.ImmutableList;
import io.spine.base.CommandMessage;
import io.spine.base.EventMessage;
import io.spine.users.RoleId;
import io.spine.users.server.UserPartCommandTest;
import io.spine.users.user.command.AssignRoleToUser;

import static io.spine.users.server.role.RoleIds.roleId;
import static io.spine.users.server.user.given.UserTestEnv.userOrgEntity;

public abstract class UserRolePartCommandTest<C extends CommandMessage, E extends EventMessage>
        extends UserPartCommandTest<C, E> {

    @Override
    protected Iterable<CommandMessage> setupCommands() {
        AssignRoleToUser assignRole = AssignRoleToUser
                .newBuilder()
                .setId(entityId())
                .setRoleId(originalRole())
                .vBuild();
        ImmutableList.Builder<CommandMessage> commands = ImmutableList.builder();
        commands.addAll(super.setupCommands())
                .add(assignRole);
        return commands.build();
    }

    static RoleId adminRoleId() {
        return roleId(userOrgEntity(), "admin_role");
    }

    RoleId originalRole() {
        return adminRoleId();
    }
}
