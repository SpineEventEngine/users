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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.role.TestRoleFactory.createAggregate;
import static io.spine.users.c.role.given.RoleTestCommands.renameRole;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("RenameRole command should")
class RenameRoleTest extends RoleCommandTest<RenameRole> {

    RenameRoleTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("produce RoleRenamed event")
    void produceEvent() {
        RenameRole command = message();
        RoleAggregate aggregate = createAggregate(ROLE_ID);
        String oldName = aggregate.getState()
                                  .getDisplayName();
        expectThat(aggregate).producesEvent(RoleRenamed.class, event -> {
            assertEquals(command.getId(), event.getId());
            assertEquals(command.getNewName(), event.getNewName());
            assertEquals(oldName, event.getOldName());
        });
    }

    @Test
    @DisplayName("rename the role")
    void changeState() {
        RenameRole command = message();
        RoleAggregate aggregate = createAggregate(ROLE_ID);
        expectThat(aggregate).hasState(state -> {
            assertEquals(command.getId(), state.getId());
            assertEquals(command.getNewName(), state.getDisplayName());
        });
    }

    private static RenameRole createMessage() {
        return renameRole(ROLE_ID);
    }
}
