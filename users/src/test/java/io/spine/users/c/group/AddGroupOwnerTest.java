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

import io.spine.core.UserId;
import io.spine.core.UserIdVBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.base.Identifier.newUuid;
import static io.spine.users.c.group.TestGroupFactory.createAggregate;
import static io.spine.users.c.group.given.GroupTestCommands.addGroupOwner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("AddGroupOwner command should")
class AddGroupOwnerTest extends GroupCommandTest<AddGroupOwner> {

    private static final UserId NEW_OWNER = UserIdVBuilder.newBuilder()
                                                          .setValue(newUuid())
                                                          .build();

    AddGroupOwnerTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("produce GroupOwnerAdded event")
    void produceEvent() {
        GroupAggregate aggregate = createAggregate(GROUP_ID);
        expectThat(aggregate).producesEvent(GroupOwnerAdded.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getNewOwner(), event.getNewOwner());
        });
    }

    @Test
    @DisplayName("add the owner")
    void changeState() {
        GroupAggregate aggregate = createAggregate(GROUP_ID);

        expectThat(aggregate).hasState(state -> {
            UserId newOwner = message().getNewOwner();
            assertTrue(state.getOwnersList()
                            .contains(newOwner));
        });
    }

    private static AddGroupOwner createMessage() {
        return addGroupOwner(GROUP_ID, NEW_OWNER);
    }
}
