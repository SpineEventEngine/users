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

package io.spine.users.server.signin;

import io.spine.core.UserId;
import io.spine.users.signin.command.SignUserIn;
import io.spine.users.signin.command.SignUserOut;
import io.spine.users.signin.event.SignOutCompleted;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.signin.given.SignInTestCommands.signInCommand;
import static io.spine.users.server.signin.given.SignInTestCommands.signOutCommand;
import static io.spine.users.server.signin.given.SignInTestEnv.identity;
import static io.spine.users.server.signin.given.SignInTestEnv.userId;

@DisplayName("`SignInPm` should, when `SignUserOut` command is dispatched to it,")
class SignUserOutCommandTest extends SignInPmTest {

    @Test
    @DisplayName("emit `SignOutCompleted` event")
    void emitSignOutCompleted() {
        UserId userId = userId();
        SignUserIn signIn = signInCommand(userId, identity());
        SignUserOut signOut = signOutCommand(userId);
        SignOutCompleted expectedEvent =
                SignOutCompleted
                        .newBuilder()
                        .setId(userId)
                        .build();
        context().receivesCommands(signIn, signOut)
                 .assertEvents()
                 .withType(expectedEvent.getClass())
                 .message(0)
                 .comparingExpectedFieldsOnly()
                 .isEqualTo(expectedEvent);
    }
}
