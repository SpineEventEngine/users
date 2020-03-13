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

import io.spine.server.BoundedContextBuilder;
import io.spine.roles.RoleId;
import io.spine.roles.Role;
import io.spine.roles.command.CreateRole;
import io.spine.roles.event.RoleCreated;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.roles.server.given.TestCommands.createRole;

@DisplayName("`CreateRole` command should")
class CreateRoleTest extends RoleCommandTest<CreateRole, RoleCreated> {

    @Override
    protected BoundedContextBuilder contextBuilder() {
        return UsersContextWithRoles.extend(super.contextBuilder());
    }

    @Test
    @DisplayName("produce `RoleCreated` event and create the role")
    @Override
    protected void produceEventAndChangeState() {
        super.produceEventAndChangeState();
    }

    @Override
    protected CreateRole command(RoleId id) {
        return createRole(id);
    }

    @Override
    protected RoleCreated expectedEventAfter(CreateRole command) {
        return RoleCreated
                .newBuilder()
                .setId(command.getId())
                .build();
    }

    @Override
    protected Role expectedStateAfter(CreateRole command) {
        return Role
                .newBuilder()
                .setId(command.getId())
                .build();
    }
}
