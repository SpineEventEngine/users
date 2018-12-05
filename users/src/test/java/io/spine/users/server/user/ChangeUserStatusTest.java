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

package io.spine.users.server.user;

import io.spine.users.user.command.ChangeUserStatus;
import io.spine.users.user.event.UserStatusChanged;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.user.given.UserTestCommands.changeUserStatus;
import static io.spine.users.user.User.Status.NOT_READY;
import static io.spine.users.user.User.Status.SUSPENDED;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("ChangeUserStatus command should")
class ChangeUserStatusTest extends UserPartCommandTest<ChangeUserStatus> {

    ChangeUserStatusTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate UserStatusChanged event")
    void generateEvent() {
        UserPart aggregate = createPartWithState();
        expectThat(aggregate).producesEvent(UserStatusChanged.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getStatus(), event.getNewStatus());
            assertEquals(NOT_READY, event.getOldStatus());
        });
    }

    @Test
    @DisplayName("change status of the user")
    void changeState() {
        UserPart aggregate = createPartWithState();
        expectThat(aggregate).hasState(state -> assertEquals(SUSPENDED, state.getStatus()));
    }

    private static ChangeUserStatus createMessage() {
        return changeUserStatus(USER_ID);
    }
}
