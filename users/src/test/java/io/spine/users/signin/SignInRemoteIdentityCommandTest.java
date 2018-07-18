/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin;

import io.spine.protobuf.AnyPacker;
import io.spine.users.signin.identity.CheckUserStatus;
import io.spine.users.signin.identity.FetchUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.signin.RemoteIdentitySignIn.Status.USER_PROFILE_FETCHING;
import static io.spine.users.signin.RemoteIdentitySignIn.Status.USER_STATUS_CHECK;
import static io.spine.users.signin.given.SignInTestCommands.signInRemoteIdentity;
import static io.spine.users.signin.given.SignInTestEnv.emptyUserRepo;
import static io.spine.users.signin.given.SignInTestEnv.nonEmptyUserRepo;
import static io.spine.users.signin.given.TestProcManFactory.createEmptyProcMan;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("RemoteIdentitySignInPM should, when SignInRemoteIdentity")
public class SignInRemoteIdentityCommandTest
        extends RemoteIdentitySignInPmCommandTest<SignInRemoteIdentity> {

    @Test
    @DisplayName("initialize")
    void initialize() {
        final RemoteIdentitySignInProcMan emptyProcMan = createEmptyProcMan();
        emptyProcMan.setUserRepository(emptyUserRepo());

        expectThat(emptyProcMan).hasState(state -> {
            assertEquals(message().getId(), state.getId());
            assertEquals(message().getIdentity(), state.getIdentity());
        });
    }

    @Test
    @DisplayName("fetch user profile if the user doesn't exist")
    void createUser() {
        final RemoteIdentitySignInProcMan emptyProcMan = createEmptyProcMan();
        emptyProcMan.setUserRepository(emptyUserRepo());

        expectThat(emptyProcMan).producesEvent(CommandSent.class, event -> {
            FetchUserDetails command = AnyPacker.unpack(event.getCommand());

            assertEquals(message().getId(), command.getUserId());
            assertEquals(message().getIdentity(), command.getIdentity());
            assertEquals(message().getIdentity()
                    .getProviderId(), command.getId());
        }).hasState(state -> assertEquals(USER_PROFILE_FETCHING, state.getStatus()));
    }

    @Test
    @DisplayName("check user status if the user exists")
    void checkStatus() {
        final RemoteIdentitySignInProcMan emptyProcMan = createEmptyProcMan();
        emptyProcMan.setUserRepository(nonEmptyUserRepo());

        expectThat(emptyProcMan).producesEvent(CommandSent.class, event -> {
            CheckUserStatus command = AnyPacker.unpack(event.getCommand());

            assertEquals(message().getId(), command.getUserId());
            assertEquals(message().getIdentity(), command.getIdentity());
            assertEquals(message().getIdentity()
                    .getProviderId(), command.getId());
        }).hasState(state -> assertEquals(USER_STATUS_CHECK, state.getStatus()));
    }

    @Override
    protected SignInRemoteIdentity createMessage() {
        return signInRemoteIdentity();
    }
}
