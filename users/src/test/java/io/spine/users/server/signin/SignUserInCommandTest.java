/*
 * Copyright 2018, TeamDev. All rights reserved.
 *
 * Redistribution and use in source and/or binary forms, with or without
 * modification, must retain the above copyright notice and the following
 * disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.spine.users.server.signin;

import io.spine.users.server.signin.given.SignInTestEnv;
import io.spine.users.signin.command.FinishSignIn;
import io.spine.users.signin.command.SignUserIn;
import io.spine.users.user.command.CreateUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.signin.TestProcManFactory.createEmptyProcMan;
import static io.spine.users.server.signin.TestProcManFactory.identityProviderId;
import static io.spine.users.server.signin.TestProcManFactory.withIdentity;
import static io.spine.users.server.signin.given.SignInTestCommands.signInCommand;
import static io.spine.users.server.signin.given.SignInTestEnv.mockActiveIdentityProvider;
import static io.spine.users.server.signin.given.SignInTestEnv.mockEmptyIdentityProvider;
import static io.spine.users.server.signin.given.SignInTestEnv.mockEmptyProviderFactory;
import static io.spine.users.server.signin.given.SignInTestEnv.mockSuspendedIdentityProvider;
import static io.spine.users.server.signin.given.SignInTestEnv.noIdentityUserRepo;
import static io.spine.users.server.signin.given.SignInTestEnv.nonEmptyUserRepo;
import static io.spine.users.signin.SignIn.Status.AWAITING_USER_AGGREGATE_CREATION;
import static io.spine.users.signin.SignIn.Status.COMPLETED;
import static io.spine.users.signin.SignInFailureReason.SIGN_IN_NOT_AUTHORIZED;
import static io.spine.users.signin.SignInFailureReason.UNKNOWN_IDENTITY;
import static io.spine.users.signin.SignInFailureReason.UNSUPPORTED_IDENTITY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
                .hasState(state -> assertEquals(AWAITING_USER_AGGREGATE_CREATION,
                                                state.getStatus()));
    }

    @Test
    @DisplayName("finish the process if the user exists and is active")
    void finishProcess() {
        SignInPm emptyProcMan = createEmptyProcMan(entityId());
        emptyProcMan.setUserRepository(nonEmptyUserRepo());
        emptyProcMan.setIdentityProviderFactory(mockActiveIdentityProvider());

        expectThat(emptyProcMan)
                .producesCommand(FinishSignIn.class,
                                 command -> assertEquals(message().getId(), command.getId()))
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
        SignInPm emptyProcMan = withIdentity(entityId(), identityProviderId("invalid"));
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
