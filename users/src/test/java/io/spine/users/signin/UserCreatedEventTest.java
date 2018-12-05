/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin;

import io.spine.users.signin.command.SignUserIn;
import io.spine.users.signin.given.SignInTestEnv;
import io.spine.users.user.event.UserCreated;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.signin.SignIn.Status.AWAITING_USER_AGGREGATE_CREATION;
import static io.spine.users.signin.SignIn.Status.COMPLETED;
import static io.spine.users.signin.TestProcManFactory.nonEmptyProcMan;
import static io.spine.users.signin.given.SignInTestEvents.userCreated;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
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
