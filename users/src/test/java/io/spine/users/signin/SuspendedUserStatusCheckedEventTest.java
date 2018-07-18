/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin;

import io.spine.users.signin.identity.UserStatusChecked;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.signin.RemoteIdentitySignIn.Status.FAILED;
import static io.spine.users.signin.RemoteIdentitySignIn.Status.USER_STATUS_CHECK;
import static io.spine.users.signin.given.SignInTestEvents.userStatusChecked;
import static io.spine.users.signin.given.TestProcManFactory.nonEmptyProcMan;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("RemoteIdentitySignInPM should, when user status is suspended")
public class SuspendedUserStatusCheckedEventTest
        extends RemoteIdentitySignInPmEventTest<UserStatusChecked> {

    private static final boolean SUSPENDED = false;

    @Test
    @DisplayName("change status to FAILED")
    void changeStatus() {
        RemoteIdentitySignInProcMan emptyProcMan = nonEmptyProcMan(USER_STATUS_CHECK);

        expectThat(emptyProcMan).hasState(state -> assertEquals(FAILED, state.getStatus()));
    }

    @Test
    @DisplayName("finished sign-in if user is suspended")
    void sendCommand() {
        RemoteIdentitySignInProcMan emptyProcMan = nonEmptyProcMan(USER_STATUS_CHECK);
        expectThat(emptyProcMan).producesEvent(RemoteIdentitySignInFailed.class, event -> {
            assertEquals(message().getUserId(), event.getId());
            assertEquals(message().getIdentity(), event.getIdentity());
        });
    }

    @Override
    protected UserStatusChecked createMessage() {
        return userStatusChecked(SUSPENDED);
    }
}
