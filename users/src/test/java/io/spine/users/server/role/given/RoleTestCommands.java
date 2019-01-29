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

package io.spine.users.server.role.given;

import io.spine.users.RoleId;
import io.spine.users.role.command.CreateRole;
import io.spine.users.role.command.CreateRoleVBuilder;
import io.spine.users.role.command.DeleteRole;
import io.spine.users.role.command.DeleteRoleVBuilder;
import io.spine.users.server.role.RoleAggregate;

/**
 * Test commands for {@link RoleAggregate}.
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
                                 .build();
    }

    public static DeleteRole deleteRole(RoleId id) {
        return DeleteRoleVBuilder.newBuilder()
                                 .setId(id)
                                 .build();
    }
}
