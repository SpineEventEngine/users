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

import io.spine.net.EmailAddress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.group.TestGroupFactory.createAggregate;
import static io.spine.users.c.group.given.GroupTestCommands.changeGroupEmail;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("ChangeGroupEmail command should")
class ChangeGroupEmailTest extends GroupCommandTest<ChangeGroupEmail> {

    ChangeGroupEmailTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("produce GroupEmailChanged event")
    void produceEvent() {
        GroupAggregate aggregate = createAggregate(GROUP_ID);
        EmailAddress oldEmail = aggregate.getState()
                                         .getEmail();
        expectThat(aggregate).producesEvent(GroupEmailChanged.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getNewEmail(), event.getNewEmail());
            assertEquals(oldEmail, event.getOldEmail());
        });
    }

    @Test
    @DisplayName("change the email")
    void changeState() {
        GroupAggregate aggregate = createAggregate(GROUP_ID);

        expectThat(aggregate).hasState(state -> {
            assertEquals(message().getNewEmail(), state.getEmail());
        });
    }

    private static ChangeGroupEmail createMessage() {
        return changeGroupEmail(GROUP_ID);
    }
}
