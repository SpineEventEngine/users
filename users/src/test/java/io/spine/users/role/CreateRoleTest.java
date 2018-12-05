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

package io.spine.users.c.role;

import io.spine.users.RoleId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.role.TestRoleFactory.createEmptyAggregate;
import static io.spine.users.c.role.given.RoleTestCommands.createRole;

@DisplayName("CreateRole command should")
class CreateRoleTest extends RoleCommandTest<CreateRole> {

    CreateRoleTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("produce RoleCreated event")
    void produceEvent() {
        CreateRole command = message();
        expectThat(createEmptyAggregate(ROLE_ID)).producesEvent(RoleCreated.class, event -> {
            assertEquals(command.getId(), event.getId());
        });
    }

    @Test
    @DisplayName("create a role")
    void changeState() {
        CreateRole command = message();
        RoleId roleId = command.getId();
        expectThat(createEmptyAggregate(ROLE_ID)).hasState(state -> {
            assertEquals(roleId, state.getId());
            assertEquals(roleId.getName(), state.getDisplayName());
            assertEquals(roleId.getOrgEntity(), state.getOrgEntity());
        });
    }

    private static CreateRole createMessage() {
        return createRole(ROLE_ID);
    }
}
