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

import io.spine.users.signin.SignIn;
import io.spine.users.signin.command.FinishSignIn;
import io.spine.users.signin.event.SignInFailed;
import io.spine.users.signin.event.SignInSuccessful;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.signin.TestProcManFactory.nonEmptyProcMan;
import static io.spine.users.server.signin.given.SignInTestCommands.finishSignInSuccessfully;
import static io.spine.users.server.signin.given.SignInTestCommands.finishSignInUnsuccessfully;
import static io.spine.users.server.signin.given.SignInTestEnv.failureReason;
import static io.spine.users.server.signin.given.SignInTestEnv.userId;
import static io.spine.users.signin.SignIn.Status.COMPLETED;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */

@DisplayName("SignInPm should")
@SuppressWarnings("InnerClassMayBeStatic") // JUnit doesn't support static classes.
class FinishSignInTest {

    @Nested
    @DisplayName("when FinishSignIn successfully")
    class Successful extends SignInPmCommandTest<FinishSignIn> {

        Successful() {
            super(userId(), finishSuccessfully());
        }

        @Test
        @DisplayName("produce SignInSuccessful event")
        void generatesEvent() {
            SignInPm procMan = nonEmptyProcMan(COMPLETED);
            expectThat(procMan).producesEvent(SignInSuccessful.class, event -> {
                SignIn state = procMan.state();
                assertEquals(state.getId(), event.getId());
                assertEquals(state.getIdentity(), event.getIdentity());
            });
        }
    }

    @Nested
    @DisplayName("when FinishSignIn unsuccessfully")
    class Failure extends SignInPmCommandTest<FinishSignIn> {

        Failure() {
            super(userId(), finishUnsuccessfully());
        }

        @Test
        @DisplayName("produce SignInFailed event")
        void generatesEvent() {
            SignInPm procMan = nonEmptyProcMan(COMPLETED);
            expectThat(procMan).producesEvent(SignInFailed.class, event -> {
                SignIn state = procMan.state();
                assertEquals(state.getId(), event.getId());
                assertEquals(state.getIdentity(), event.getIdentity());
                assertEquals(failureReason(), event.getFailureReason());
            });
        }
    }

    private static FinishSignIn finishSuccessfully() {
        return finishSignInSuccessfully(userId());
    }

    private static FinishSignIn finishUnsuccessfully() {
        return finishSignInUnsuccessfully(userId());
    }
}
