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

import io.spine.users.user.command.CreateUser;
import io.spine.users.user.event.UserCreated;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.user.given.UserTestCommands.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("CreateUser command should")
class CreateUserTest extends UserPartCommandTest<CreateUser> {

    CreateUserTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate UserCreated event")
    void produceEvent() {
        expectThat(new UserPart(root(USER_ID))).producesEvent(UserCreated.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getDisplayName(), event.getDisplayName());
            assertEquals(message().getPrimaryIdentity(), event.getPrimaryIdentity());
            assertEquals(message().getOrgEntity(), event.getOrgEntity());
            assertEquals(message().getStatus(), event.getStatus());
            assertEquals(message().getNature(), event.getNature());
        });
    }

    @Test
    @DisplayName("create the user")
    void changeState() {
        expectThat(new UserPart(root(USER_ID))).hasState(state -> {
            assertEquals(message().getId(), state.getId());
            assertEquals(message().getDisplayName(), state.getDisplayName());
            assertEquals(message().getPrimaryIdentity(), state.getPrimaryIdentity());
            assertEquals(message().getOrgEntity(), state.getOrgEntity());
            assertEquals(message().getStatus(), state.getStatus());
            assertEquals(message().getNature(), state.getNature());
        });
    }

    private static CreateUser createMessage() {
        return createUser(USER_ID);
    }
}
