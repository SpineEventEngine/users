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

import static io.spine.users.c.group.given.GroupTestCommands.changeGroupDescription;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("ChangeGroupDescription command should")
class ChangeGroupDescriptionTest extends GroupCommandTest<ChangeGroupDescription> {

    ChangeGroupDescriptionTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("produce GroupDescriptionChanged event")
    void produceEvent() {
        GroupPart aggregate = createPartWithState();
        String oldDescription = aggregate.getState()
                                         .getDescription();
        expectThat(aggregate).producesEvent(GroupDescriptionChanged.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getDescription(), event.getNewDescription());
            assertEquals(oldDescription, event.getOldDescription());
        });
    }

    @Test
    @DisplayName("change the email")
    void changeState() {
        GroupPart aggregate = createPartWithState();
        expectThat(aggregate).hasState(state -> {
            assertEquals(message().getDescription(), state.getDescription());
        });
    }

    private static ChangeGroupDescription createMessage() {
        return changeGroupDescription(GROUP_ID);
    }
}
