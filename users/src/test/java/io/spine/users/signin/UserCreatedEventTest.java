/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin;

import io.spine.users.user.UserAggregateRepository;
import io.spine.users.user.UserCreated;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.signin.SignIn.Status.AWAITING_USER_CREATION;
import static io.spine.users.signin.SignIn.Status.COMPLETED;
import static io.spine.users.signin.given.SignInTestEnv.userId;
import static io.spine.users.signin.given.SignInTestEvents.userCreated;
import static io.spine.users.signin.given.TestProcManFactory.nonEmptyProcMan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("SignInPM should, when UserCreated")
public class UserCreatedEventTest
        extends SignInPmEventTest<UserCreated> {

    protected UserCreatedEventTest() {
        super(userId(), event());
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
        return userCreated(userId());
    }

    @Override
    IdentityProvider identityProvider() {
        return mock(IdentityProvider.class);
    }

    @Override
    UserAggregateRepository userRepository() {
        return mock(UserAggregateRepository.class);
    }
}
