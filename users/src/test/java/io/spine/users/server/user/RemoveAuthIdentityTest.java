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

import io.spine.users.user.Identity;
import io.spine.users.user.command.RemoveSecondaryIdentity;
import io.spine.users.user.event.SecondaryIdentityRemoved;
import io.spine.users.user.rejection.Rejections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.user.given.UserTestCommands.removeAuthIdentity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("RemoveSecondaryIdentity command should")
class RemoveAuthIdentityTest extends UserPartCommandTest<RemoveSecondaryIdentity> {

    RemoveAuthIdentityTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate AuthIdentityRemoved event")
    void generateEvent() {
        UserPart aggregate = createPartWithState();
        expectThat(aggregate).producesEvent(SecondaryIdentityRemoved.class, event -> {
            assertEquals(message().getId(), event.getId());
            Identity eventIdentity = event.getIdentity();
            assertEquals(message().getDirectoryId(), eventIdentity.getDirectoryId());
            assertEquals(message().getUserId(), eventIdentity.getUserId());
        });
    }

    @Test
    @DisplayName("remove an identity")
    void changeState() {
        UserPart aggregate = createPartWithState();
        expectThat(aggregate).hasState(state -> assertTrue(state.getSecondaryIdentityList()
                                                                .isEmpty()));
    }

    @Test
    @DisplayName("throw rejection if auth identity doesn't exist")
    void generateRejection() {
        expectThat(new UserPart(root(USER_ID))).throwsRejection(Rejections.IdentityDoesNotExist.class);
    }

    private static RemoveSecondaryIdentity createMessage() {
        return removeAuthIdentity(USER_ID);
    }
}
