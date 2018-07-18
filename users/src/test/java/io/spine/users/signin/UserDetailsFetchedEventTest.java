/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin;

import io.spine.protobuf.AnyPacker;
import io.spine.users.signin.identity.UserDetailsFetched;
import io.spine.users.user.CreateUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.signin.RemoteIdentitySignIn.Status.USER_CREATING;
import static io.spine.users.signin.RemoteIdentitySignIn.Status.USER_PROFILE_FETCHING;
import static io.spine.users.signin.given.SignInTestEvents.userDetailsFetched;
import static io.spine.users.signin.given.TestProcManFactory.nonEmptyProcMan;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("RemoteIdentitySignInPM should, when UserDetailsFetched")
public class UserDetailsFetchedEventTest
        extends RemoteIdentitySignInPmEventTest<UserDetailsFetched> {

    @Test
    @DisplayName("change status")
    void changeStatus() {
        RemoteIdentitySignInProcMan emptyProcMan = nonEmptyProcMan(USER_PROFILE_FETCHING);

        expectThat(emptyProcMan).hasState(state -> assertEquals(USER_CREATING, state.getStatus()));
    }

    @Test
    @DisplayName("send CreateUser command")
    void sendCommand() {
        RemoteIdentitySignInProcMan emptyProcMan = nonEmptyProcMan(USER_PROFILE_FETCHING);

        expectThat(emptyProcMan).producesEvent(CommandSent.class, event -> {
            CreateUser command = AnyPacker.unpack(event.getCommand());
            assertEquals(message().getUserId(), command.getId());
            assertEquals(message().getProfile(), command.getProfile());
            assertEquals(message().getAttribute(0), command.getAttribute(0));
            assertEquals(message().getIdentity(), command.getPrimaryIdentity());
        });
    }

    @Override
    protected UserDetailsFetched createMessage() {
        return userDetailsFetched();
    }
}
