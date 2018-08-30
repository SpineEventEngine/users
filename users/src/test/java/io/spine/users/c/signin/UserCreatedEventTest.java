/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.signin;

import io.spine.users.c.signin.given.SignInTestEnv;
import io.spine.users.c.user.UserAggregateRepository;
import io.spine.users.c.user.UserCreated;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.signin.SignIn.Status.AWAITING_USER_CREATION;
import static io.spine.users.c.signin.SignIn.Status.COMPLETED;
import static io.spine.users.c.signin.given.SignInTestEvents.userCreated;
import static io.spine.users.c.signin.given.TestProcManFactory.nonEmptyProcMan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

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
        SignInPm emptyProcMan = nonEmptyProcMan(AWAITING_USER_CREATION);
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
