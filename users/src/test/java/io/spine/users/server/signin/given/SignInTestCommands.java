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

package io.spine.users.server.signin.given;

import io.spine.core.UserId;
import io.spine.users.server.signin.SignInProcess;
import io.spine.users.server.signin.Signals;
import io.spine.users.signin.command.FinishSignIn;
import io.spine.users.signin.command.SignUserOut;

import static io.spine.users.signin.SignInFailureReason.SIGN_IN_NOT_AUTHORIZED;

/**
 * Test commands for {@link SignInProcess}.
 */
public final class SignInTestCommands {

    /**
     * Prevents instantiation.
     */
    private SignInTestCommands() {
    }

    public static SignUserOut signOutCommand(UserId id) {
        return SignUserOut
                .newBuilder()
                .setId(id)
                .build();
    }

    public static FinishSignIn finishSignInUnsuccessfully(UserId id) {
        return Signals.finishWithError(id, SIGN_IN_NOT_AUTHORIZED);
    }
}
