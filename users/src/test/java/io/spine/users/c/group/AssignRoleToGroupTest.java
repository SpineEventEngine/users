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

package io.spine.users.c.group;

import io.spine.users.RoleId;
import io.spine.users.RoleIdVBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.base.Identifier.newUuid;
import static io.spine.users.c.group.given.GroupTestCommands.assignRoleToGroup;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("AssignRoleToGroup command should")
class AssignRoleToGroupTest extends GroupCommandTest<AssignRoleToGroup> {

    private static final RoleId NEW_ROLE = RoleIdVBuilder.newBuilder()
                                                         .setValue(newUuid())
                                                         .build();

    AssignRoleToGroupTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("produce RoleAssignedToGroup event")
    void produceEvent() {
        GroupPart aggregate = createPartWithState();
        expectThat(aggregate).producesEvent(RoleAssignedToGroup.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getRoleId(), event.getRoleId());
        });
    }

    @Test
    @DisplayName("add a role assignment")
    void changeState() {
        GroupPart aggregate = createPartWithState();

        expectThat(aggregate).hasState(state -> {
            RoleId expectedRole = message().getRoleId();
            assertTrue(state.getRoleList()
                            .contains(expectedRole));
        });
    }

    private static AssignRoleToGroup createMessage() {
        return assignRoleToGroup(GROUP_ID, NEW_ROLE);
    }
}
