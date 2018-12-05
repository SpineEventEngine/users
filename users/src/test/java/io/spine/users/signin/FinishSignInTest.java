/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin;

import io.spine.users.signin.command.FinishSignIn;
import io.spine.users.signin.event.SignInFailed;
import io.spine.users.signin.event.SignInSuccessful;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.spine.users.signin.SignIn.Status.COMPLETED;
import static io.spine.users.signin.TestProcManFactory.nonEmptyProcMan;
import static io.spine.users.signin.given.SignInTestCommands.finishSignInSuccessfully;
import static io.spine.users.signin.given.SignInTestCommands.finishSignInUnsuccessfully;
import static io.spine.users.signin.given.SignInTestEnv.failureReason;
import static io.spine.users.signin.given.SignInTestEnv.userId;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */

@DisplayName("SignInPm should")
@SuppressWarnings("InnerClassMayBeStatic")
        // JUnit doesn't support static classes.
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
                SignIn state = procMan.getState();
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
                SignIn state = procMan.getState();
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
