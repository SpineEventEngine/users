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

package io.spine.users.server.signin;

import io.spine.users.server.signin.given.SignInTestEnv;
import io.spine.users.signin.command.SignUserIn;
import io.spine.users.user.event.UserCreated;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.signin.TestProcManFactory.nonEmptyProcMan;
import static io.spine.users.server.signin.given.SignInTestEvents.userCreated;
import static io.spine.users.signin.SignIn.Status.AWAITING_USER_AGGREGATE_CREATION;
import static io.spine.users.signin.SignIn.Status.COMPLETED;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("SignInPM should, when UserCreated")
public class UserCreatedEventTest
        extends SignInPmEventTest<UserCreated> {

    protected UserCreatedEventTest() {
        super(SignInTestEnv.userId(), event());
    }

    @Test
    @DisplayName("do nothing in a wrong status")
    void ignoreMessage() {
        SignInPm emptyProcMan = nonEmptyProcMan(COMPLETED);
        expectThat(emptyProcMan).ignoresMessage();
    }

    @Test
    @DisplayName("start SignIn again")
    void checkStatus() {
        SignInPm emptyProcMan = nonEmptyProcMan(AWAITING_USER_AGGREGATE_CREATION);
        expectThat(emptyProcMan).producesCommand(SignUserIn.class, command -> {
            assertEquals(message().getId(), command.getId());
            assertEquals(emptyProcMan.getState()
                                     .getIdentity(), command.getIdentity());
        });
    }

    private static UserCreated event() {
        return userCreated(SignInTestEnv.userId());
    }
}
