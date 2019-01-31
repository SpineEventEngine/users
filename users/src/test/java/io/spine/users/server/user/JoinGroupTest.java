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

import io.spine.users.user.command.JoinGroup;
import io.spine.users.user.event.UserJoinedGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.user.given.UserTestCommands.startGroupMembership;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("JoinGroup command should")
class JoinGroupTest extends UserMembershipCommandTest<JoinGroup> {

    JoinGroupTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate UserJoinedGroup event")
    void generateEvent() {
        UserMembershipPart part = createPart();
        expectThat(part).producesEvent(UserJoinedGroup.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getGroupId(), event.getGroupId());
        });
    }

    @Test
    @DisplayName("add a new group membership")
    void changeState() {
        UserMembershipPart part = createPart();
        expectThat(part).hasState(
                state -> assertEquals(message().getGroupId(), state.getMembership(0)
                                                                   .getGroupId()));
    }

    private static JoinGroup createMessage() {
        return startGroupMembership(USER_ID);
    }
}
