/*
 * Copyright 2019, TeamDev. All rights reserved.
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

import io.spine.users.OrganizationOrUnit;
import io.spine.users.user.command.MoveUser;
import io.spine.users.user.event.UserMoved;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.user.given.UserTestCommands.moveUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("MoveUser command should")
class MoveUserTest extends UserPartCommandTest<MoveUser> {

    MoveUserTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate UserMoved event")
    void generateEvent() {
        UserPart aggregate = createPartWithState();
        OrganizationOrUnit oldParent = aggregate.state()
                                                  .getOrgEntity();
        expectThat(aggregate).producesEvent(UserMoved.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getNewOrgEntity(), event.getNewOrgEntity());
            assertEquals(oldParent, event.getOldOrgEntity());
        });
    }

    @Test
    @DisplayName("change parent entity of the user")
    void changeState() {
        UserPart aggregate = createPartWithState();
        expectThat(aggregate).hasState(
                state -> assertEquals(message().getNewOrgEntity(), state.getOrgEntity()));
    }

    private static MoveUser createMessage() {
        return moveUser(USER_ID);
    }
}
