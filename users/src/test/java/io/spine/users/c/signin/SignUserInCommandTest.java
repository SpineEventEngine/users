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
import static io.spine.users.c.signin.SignInFailureReason.UNKNOWN_IDENTITY;
import static io.spine.users.c.signin.SignInFailureReason.UNSUPPORTED_IDENTITY;
import static io.spine.users.c.signin.TestProcManFactory.createEmptyProcMan;
import static io.spine.users.c.signin.given.SignInTestCommands.signInCommand;
import static io.spine.users.c.signin.given.SignInTestEnv.mockActiveIdentityProvider;
import static io.spine.users.c.signin.given.SignInTestEnv.mockEmptyIdentityProvider;
import static io.spine.users.c.signin.given.SignInTestEnv.mockEmptyProviderFactory;
import static io.spine.users.c.signin.given.SignInTestEnv.mockSuspendedIdentityProvider;
import static io.spine.users.c.signin.given.SignInTestEnv.noIdentityUserRepo;
import static io.spine.users.c.signin.given.SignInTestEnv.nonEmptyUserRepo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
        emptyProcMan.setUserRepository(SignInTestEnv.emptyUserRepo());
        emptyProcMan.setIdentityProviderFactory(mockActiveIdentityProvider());
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
        emptyProcMan.setUserRepository(nonEmptyUserRepo());
        emptyProcMan.setIdentityProviderFactory(mockActiveIdentityProvider());

        expectThat(emptyProcMan).producesCommand(FinishSignIn.class, command -> {
            assertEquals(message().getId(), command.getId());
        }).hasState(state -> assertEquals(COMPLETED, state.getStatus()));
    }

    @Test
    @DisplayName("fail the process if the user exists and is NOT active")
    void failProcess() {
        SignInPm emptyProcMan = createEmptyProcMan(entityId());
        emptyProcMan.setUserRepository(nonEmptyUserRepo());
        emptyProcMan.setIdentityProviderFactory(mockSuspendedIdentityProvider());

        expectThat(emptyProcMan).producesCommand(FinishSignIn.class, command -> {
            assertEquals(message().getId(), command.getId());
            assertFalse(command.getSuccessful());
            assertEquals(SIGN_IN_NOT_AUTHORIZED, command.getFailureReason());
        });
    }

    @Test
    @DisplayName("fail if unsupported identity given")
    void failIfNoIdentity() {
        SignInPm emptyProcMan = createEmptyProcMan(entityId());
        emptyProcMan.setUserRepository(noIdentityUserRepo());
        emptyProcMan.setIdentityProviderFactory(mockActiveIdentityProvider());
        expectThat(emptyProcMan).producesCommand(FinishSignIn.class, command -> {
            assertEquals(message().getId(), command.getId());
            assertFalse(command.getSuccessful());
            assertEquals(UNKNOWN_IDENTITY, command.getFailureReason());
        });
    }

    @Test
    @DisplayName("fail if identity provider is not aware of given identity")
    void failIfUnknownIdentity() {
        SignInPm emptyProcMan = createEmptyProcMan(entityId());
        emptyProcMan.setUserRepository(noIdentityUserRepo());
        emptyProcMan.setIdentityProviderFactory(mockEmptyIdentityProvider());
        expectThat(emptyProcMan).producesCommand(FinishSignIn.class, command -> {
            assertEquals(message().getId(), command.getId());
            assertFalse(command.getSuccessful());
            assertEquals(UNKNOWN_IDENTITY, command.getFailureReason());
        });
    }

    @Test
    @DisplayName("fail if there is no identity provider")
    void failIfNoProvider() {
        SignInPm emptyProcMan = createEmptyProcMan(entityId());
        emptyProcMan.setUserRepository(noIdentityUserRepo());
        emptyProcMan.setIdentityProviderFactory(mockEmptyProviderFactory());
        expectThat(emptyProcMan).producesCommand(FinishSignIn.class, command -> {
            assertEquals(message().getId(), command.getId());
            assertFalse(command.getSuccessful());
            assertEquals(UNSUPPORTED_IDENTITY, command.getFailureReason());
        });
    }

    private static SignUserIn command() {
        return signInCommand(SignInTestEnv.userId(), SignInTestEnv.identity());
    }
}
