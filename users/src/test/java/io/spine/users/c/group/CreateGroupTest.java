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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.group.TestGroupFactory.createEmptyAggregate;
import static io.spine.users.c.group.given.GroupTestCommands.createGroup;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings("Duplicates") // We perform the same assertions for resulting event and state
@DisplayName("CreateGroup command should")
class CreateGroupTest extends GroupCommandTest<CreateGroup> {

    CreateGroupTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("produce GroupCreated event")
    void produceEvent() {
        CreateGroup command = message();
        expectThat(createEmptyAggregate(GROUP_ID)).producesEvent(GroupCreated.class, event -> {
            assertEquals(command.getId(), event.getId());
            assertEquals(command.getDisplayName(), event.getDisplayName());
            assertEquals(command.getEmail(), event.getEmail());
            assertEquals(command.getAttributesMap(), event.getAttributesMap());
            assertEquals(command.getRoleList(), event.getRoleList());
            assertEquals(command.getOwnersList(), event.getOwnersList());
            assertEquals(command.getOrgEntity(), event.getOrgEntity());
        });
    }

    @Test
    @DisplayName("create a group")
    void changeState() {
        CreateGroup command = message();
        expectThat(createEmptyAggregate(GROUP_ID)).hasState(state -> {
            assertEquals(command.getId(), state.getId());
            assertEquals(command.getDisplayName(), state.getDisplayName());
            assertEquals(command.getEmail(), state.getEmail());
            assertEquals(command.getAttributesMap(), state.getAttributesMap());
            assertEquals(command.getRoleList(), state.getRoleList());
            assertEquals(command.getOwnersList(), state.getOwnersList());
            assertEquals(command.getOrgEntity(), state.getOrgEntity());
        });
    }

    private static CreateGroup createMessage() {
        return createGroup(GROUP_ID);
    }
}