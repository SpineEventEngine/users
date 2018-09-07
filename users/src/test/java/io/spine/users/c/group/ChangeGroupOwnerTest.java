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
import static io.spine.users.c.group.given.GroupTestCommands.changeOwner;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("ChangeGroupOwner command should")
class ChangeGroupOwnerTest extends GroupCommandTest<ChangeGroupOwner> {

    private static final UserId NEW_OWNER = UserIdVBuilder.newBuilder()
                                                          .setValue(newUuid())
                                                          .build();

    ChangeGroupOwnerTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("produce GroupOwnerChanged event")
    void produceEvent() {
        GroupAggregate aggregate = createAggregate();
        UserId oldOwner = aggregate.getState()
                                   .getOwner();
        expectThat(aggregate).producesEvent(GroupOwnerChanged.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getOwner(), event.getNewOwner());
            assertEquals(oldOwner, event.getOldOwner());
        });
    }

    @Test
    @DisplayName("change the owner")
    void changeState() {
        GroupAggregate aggregate = createAggregate();

        expectThat(aggregate).hasState(state -> {
            assertEquals(state.getOwner(), message().getOwner());
        });
    }

    private static ChangeGroupOwner createMessage() {
        return changeOwner(GROUP_ID, NEW_OWNER);
    }
}
