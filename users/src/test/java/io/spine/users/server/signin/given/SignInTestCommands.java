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
import io.spine.users.server.signin.SignInPm;
import io.spine.users.signin.command.FinishSignIn;
import io.spine.users.signin.command.FinishSignInVBuilder;
import io.spine.users.signin.command.SignUserIn;
import io.spine.users.signin.command.SignUserInVBuilder;
import io.spine.users.signin.command.SignUserOut;
import io.spine.users.signin.command.SignUserOutVBuilder;
import io.spine.users.user.Identity;

import static io.spine.users.server.signin.given.SignInTestEnv.failureReason;

/**
 * Test commands for {@link SignInPm}.
 *
 * @author Vladyslav Lubenskyi
 */
public final class SignInTestCommands {

    /**
     * Prevents instantiation.
     */
    private SignInTestCommands() {
    }

    public static SignUserIn signInCommand(UserId id, Identity identity) {
        return SignUserInVBuilder.newBuilder()
                                 .setId(id)
                                 .setIdentity(identity)
                                 .build();
    }

    public static SignUserOut signOutCommand(UserId id) {
        return SignUserOutVBuilder.newBuilder()
                                  .setId(id)
                                  .build();
    }

    public static FinishSignIn finishSignInSuccessfully(UserId id) {
        return FinishSignInVBuilder.newBuilder()
                                   .setId(id)
                                   .setSuccessful(true)
                                   .build();
    }

    public static FinishSignIn finishSignInUnsuccessfully(UserId id) {
        return FinishSignInVBuilder.newBuilder()
                                   .setId(id)
                                   .setSuccessful(false)
                                   .setFailureReason(failureReason())
                                   .build();
    }
}
