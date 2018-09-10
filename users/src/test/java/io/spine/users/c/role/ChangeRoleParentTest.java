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

import io.spine.users.ParentEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.role.TestRoleFactory.createAggregate;
import static io.spine.users.c.role.given.RoleTestCommands.changeRoleParent;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("ChangeRoleParent command should")
class ChangeRoleParentTest extends RoleCommandTest<ChangeRoleParent> {

    ChangeRoleParentTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("produce RoleParentChanged event")
    void produceEvent() {
        ChangeRoleParent command = message();
        RoleAggregate aggregate = createAggregate(ROLE_ID);
        ParentEntity oldParent = aggregate.getState()
                                          .getBelongsTo();
        expectThat(aggregate).producesEvent(RoleParentChanged.class, event -> {
            assertEquals(command.getId(), event.getId());
            assertEquals(command.getNewParent(), event.getNewParent());
            assertEquals(oldParent, event.getOldParent());
        });
    }

    @Test
    @DisplayName("change the parent")
    void changeState() {
        RoleAggregate aggregate = createAggregate(ROLE_ID);
        expectThat(aggregate).hasState(state -> {
            assertEquals(message().getNewParent(), state.getBelongsTo());
        });
    }

    private static ChangeRoleParent createMessage() {
        return changeRoleParent(ROLE_ID);
    }
}
