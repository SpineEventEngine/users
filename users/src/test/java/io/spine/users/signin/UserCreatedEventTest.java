/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin;

import io.spine.users.UserAuthIdentity;
import io.spine.users.signin.identity.CheckUserStatus;
import io.spine.users.user.UserCreated;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.signin.RemoteIdentitySignIn.Status.USER_CREATING;
import static io.spine.users.signin.RemoteIdentitySignIn.Status.USER_PROFILE_FETCHING;
import static io.spine.users.signin.given.SignInTestEvents.userCreated;
import static io.spine.users.signin.given.TestProcManFactory.nonEmptyProcMan;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("RemoteIdentitySignInPM should, when UserCreated")
public class UserCreatedEventTest
        extends RemoteIdentitySignInPmEventTest<UserCreated> {

    @Test
    @DisplayName("do nothing in wrong status")
    void ignoreMessage() {
        RemoteIdentitySignInProcMan emptyProcMan = nonEmptyProcMan(USER_PROFILE_FETCHING);
        expectThat(emptyProcMan).ignoresMessage();
    }

    @Test
    @DisplayName("send CheckUserStatus command")
    void checkStatus() {
        RemoteIdentitySignInProcMan emptyProcMan = nonEmptyProcMan(USER_CREATING);
        expectThat(emptyProcMan).routesCommand(CheckUserStatus.class, command -> {
            assertEquals(message().getId(), command.getUserId());
            UserAuthIdentity identity = emptyProcMan.getState()
                    .getIdentity();
            assertEquals(identity.getProviderId(), command.getId());
            assertEquals(identity, command.getIdentity());
        });
    }

    @Override
    protected UserCreated createMessage() {
        return userCreated();
    }
}
