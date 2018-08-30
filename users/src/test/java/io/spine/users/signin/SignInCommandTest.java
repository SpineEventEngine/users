/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin;

import io.spine.users.c.signin.FailureReason;
import io.spine.users.user.CreateUser;
import io.spine.users.user.UserAggregateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.signin.FailureReason.SIGN_IN_NOT_ALLOWED;
import static io.spine.users.signin.SignIn.Status.AWAITING_USER_CREATION;
import static io.spine.users.signin.SignIn.Status.COMPLETED;
import static io.spine.users.signin.given.SignInTestCommands.signInCommand;
import static io.spine.users.signin.given.SignInTestEnv.emptyUserRepo;
import static io.spine.users.signin.given.SignInTestEnv.identity;
import static io.spine.users.signin.given.SignInTestEnv.mockActiveIdentityProvider;
import static io.spine.users.signin.given.SignInTestEnv.mockSuspendedIdentityProvider;
import static io.spine.users.signin.given.SignInTestEnv.nonEmptyUserRepo;
import static io.spine.users.signin.given.SignInTestEnv.userId;
import static io.spine.users.signin.given.TestProcManFactory.createEmptyProcMan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("SignInPm should, when SignIn")
class SignInCommandTest extends SignInPmCommandTest<SignUserIn> {

    SignInCommandTest() {
        super(userId(), command());
    }

    @Test
    @DisplayName("initialize")
    void initialize() {
        SignInPm emptyProcMan = createEmptyProcMan(entityId());
        emptyProcMan.setUserRepository(emptyUserRepo());
        emptyProcMan.setIdentityProviderFactory(mockActiveIdentityProvider());
        expectThat(emptyProcMan).hasState(state -> {
            assertEquals(message().getId(), state.getId());
            assertEquals(message().getIdentity(), state.getIdentity());
        });
    }

    @Test
    @DisplayName("create user if the user doesn't exist")
    void createUser() {
        SignInPm emptyProcMan = createEmptyProcMan(entityId());
        emptyProcMan.setUserRepository(emptyUserRepo());
        emptyProcMan.setIdentityProviderFactory(mockActiveIdentityProvider());
        expectThat(emptyProcMan)
                .producesCommand(CreateUser.class, command -> {
                    assertEquals(message().getId(), command.getId());
                    assertEquals(message().getIdentity(), command.getPrimaryIdentity());
                })
                .hasState(state -> assertEquals(AWAITING_USER_CREATION, state.getStatus()));
    }

    @Test
    @DisplayName("finish the process if the user exists and is active")
    void finishProcess() {
        SignInPm emptyProcMan = createEmptyProcMan(entityId());
        emptyProcMan.setUserRepository(nonEmptyUserRepo());
        emptyProcMan.setIdentityProviderFactory(mockActiveIdentityProvider());

        expectThat(emptyProcMan).producesCommand(FinishSignIn.class, command -> {
            assertEquals(message().getId(), command.getId());
        })
                                .hasState(state -> assertEquals(COMPLETED, state.getStatus()));
    }

    @Test
    @DisplayName("fail the process if the user exists and is NOT active")
    void failProcess() {
        SignInPm emptyProcMan = createEmptyProcMan(entityId());
        emptyProcMan.setUserRepository(nonEmptyUserRepo());
        emptyProcMan.setIdentityProviderFactory(mockSuspendedIdentityProvider());

        expectThat(emptyProcMan).producesCommand(FinishSignIn.class, command -> {
            assertEquals(message().getId(), command.getId());
            assertEquals(SIGN_IN_NOT_ALLOWED, command.getFailureReason());
        });
    }

    private static SignUserIn command() {
        return signInCommand(userId(), identity());
    }
}
