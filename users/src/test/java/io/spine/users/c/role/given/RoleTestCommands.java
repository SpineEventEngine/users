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

package io.spine.users.c.role.given;

import io.spine.users.RoleId;
import io.spine.users.c.role.CreateRole;
import io.spine.users.c.role.CreateRoleVBuilder;
import io.spine.users.c.role.DeleteRole;
import io.spine.users.c.role.DeleteRoleVBuilder;
import io.spine.users.c.role.RenameRole;
import io.spine.users.c.role.RenameRoleVBuilder;
import io.spine.users.c.role.RoleAggregate;

import static io.spine.users.c.role.given.RoleTestEnv.newRoleName;
import static io.spine.users.c.role.given.RoleTestEnv.roleName;
import static io.spine.users.c.role.given.RoleTestEnv.roleParent;

/**
 * Test commands for {@link RoleAggregate}.
 *
 * @author Vladyslav Lubenskyi
 */
public final class RoleTestCommands {

    /**
     * Prevents instantiation.
     */
    private RoleTestCommands() {
    }

    public static CreateRole createRole(RoleId id) {
        return CreateRoleVBuilder.newBuilder()
                                 .setId(id)
                                 .setBelongsTo(roleParent())
                                 .setDisplayName(roleName())
                                 .build();
    }

    public static DeleteRole deleteRole(RoleId id) {
        return DeleteRoleVBuilder.newBuilder()
                                 .setId(id)
                                 .build();
    }

    public static RenameRole renameRole(RoleId id) {
        return RenameRoleVBuilder.newBuilder()
                                 .setId(id)
                                 .setNewName(newRoleName())
                                 .build();
    }

}
