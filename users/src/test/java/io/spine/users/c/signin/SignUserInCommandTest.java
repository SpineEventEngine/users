/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.signin;

import io.spine.users.c.signin.given.SignInTestEnv;
import io.spine.users.c.user.CreateUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.signin.SignIn.Status.AWAITING_USER_AGGREGATE_CREATION;
import static io.spine.users.c.signin.SignIn.Status.COMPLETED;
import static io.spine.users.c.signin.SignInFailureReason.SIGN_IN_NOT_AUTHORIZED;
import static io.spine.users.c.signin.TestProcManFactory.createEmptyProcMan;
import static io.spine.users.c.signin.given.SignInTestCommands.signInCommand;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("SignInPm should, when SignUserIn")
class SignUserInCommandTest extends SignInPmCommandOnCommandTest<SignUserIn> {

    SignUserInCommandTest() {
        super(SignInTestEnv.userId(), command());
    }

    @Test
    @DisplayName("initialize")
    void initialize() {
        SignInPm emptyProcMan = createEmptyProcMan(entityId());
        emptyProcMan.setUserRepository(SignInTestEnv.emptyUserRepo());
        emptyProcMan.setIdentityProviderFactory(SignInTestEnv.mockActiveIdentityProvider());
        expectThat(emptyProcMan).hasState(state -> {
            assertEquals(message().getId(), state.getId());
            assertEquals(message().getIdentity(), state.getIdentity());
        });
    }

    @Test
    @DisplayName("create user if the user doesn't exist")
    void createUser() {
        SignInPm emptyProcMan = createEmptyProcMan(entityId());
        emptyProcMan.setUserRepository(SignInTestEnv.emptyUserRepo());
        emptyProcMan.setIdentityProviderFactory(SignInTestEnv.mockActiveIdentityProvider());
        expectThat(emptyProcMan)
                .producesCommand(CreateUser.class, command -> {
                    assertEquals(message().getId(), command.getId());
                    assertEquals(message().getIdentity(), command.getPrimaryIdentity());
                })
                .hasState(
                        state -> assertEquals(AWAITING_USER_AGGREGATE_CREATION, state.getStatus()));
    }

    @Test
    @DisplayName("finish the process if the user exists and is active")
    void finishProcess() {
        SignInPm emptyProcMan = createEmptyProcMan(entityId());
        emptyProcMan.setUserRepository(SignInTestEnv.nonEmptyUserRepo());
        emptyProcMan.setIdentityProviderFactory(SignInTestEnv.mockActiveIdentityProvider());

        expectThat(emptyProcMan).producesCommand(FinishSignIn.class, command -> {
            assertEquals(message().getId(), command.getId());
        })
                                .hasState(state -> assertEquals(COMPLETED, state.getStatus()));
    }

    @Test
    @DisplayName("fail the process if the user exists and is NOT active")
    void failProcess() {
        SignInPm emptyProcMan = createEmptyProcMan(entityId());
        emptyProcMan.setUserRepository(SignInTestEnv.nonEmptyUserRepo());
        emptyProcMan.setIdentityProviderFactory(SignInTestEnv.mockSuspendedIdentityProvider());

        expectThat(emptyProcMan).producesCommand(FinishSignIn.class, command -> {
            assertEquals(message().getId(), command.getId());
            assertEquals(SIGN_IN_NOT_AUTHORIZED, command.getFailureReason());
        });
    }

    private static SignUserIn command() {
        return signInCommand(SignInTestEnv.userId(), SignInTestEnv.identity());
    }
}
