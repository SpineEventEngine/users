/*
 * Copyright 2019, TeamDev. All rights reserved.
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

import io.spine.core.UserId;
import io.spine.testing.server.blackbox.SingleTenantBlackBoxContext;
import io.spine.users.server.signin.given.SignInTestEnv;
import io.spine.users.signin.SignIn;
import io.spine.users.signin.command.FinishSignIn;
import io.spine.users.signin.command.SignUserIn;
import io.spine.users.user.User;
import io.spine.users.user.command.CreateUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.signin.TestProcManFactory.createEmptyProcMan;
import static io.spine.users.server.signin.TestProcManFactory.directoryId;
import static io.spine.users.server.signin.TestProcManFactory.withIdentity;
import static io.spine.users.server.signin.given.SignInTestCommands.signInCommand;
import static io.spine.users.server.signin.given.SignInTestEnv.mockActiveDirectory;
import static io.spine.users.server.signin.given.SignInTestEnv.mockEmptyDirectory;
import static io.spine.users.server.signin.given.SignInTestEnv.mockEmptyDirectoryFactory;
import static io.spine.users.server.signin.given.SignInTestEnv.mockSuspendedDirectory;
import static io.spine.users.server.signin.given.SignInTestEnv.noIdentityUserRepo;
import static io.spine.users.server.signin.given.SignInTestEnv.nonEmptyUserRepo;
import static io.spine.users.server.user.given.UserTestEnv.googleIdentity;
import static io.spine.users.server.user.given.UserTestEnv.profile;
import static io.spine.users.server.user.given.UserTestEnv.userDisplayName;
import static io.spine.users.server.user.given.UserTestEnv.userOrgEntity;
import static io.spine.users.signin.SignIn.Status.COMPLETED;
import static io.spine.users.signin.SignInFailureReason.SIGN_IN_NOT_AUTHORIZED;
import static io.spine.users.signin.SignInFailureReason.UNKNOWN_IDENTITY;
import static io.spine.users.signin.SignInFailureReason.UNSUPPORTED_IDENTITY;
import static io.spine.users.user.User.Status.NOT_READY;
import static io.spine.users.user.UserNature.PERSON;

@DisplayName("`SignInPm` should, when `SignUserIn` is dispatched to it,")
class SignUserInCommandTest extends SignInPmTest {

    @Test
    @DisplayName("create `User` and set self into `COMPLETED` state")
    void initialize() {
        SignUserIn command = command();
        SingleTenantBlackBoxContext afterCommand = context().receivesCommand(command);
        afterCommand
                .assertCommands()
                .message(0)
                .comparingExpectedFieldsOnly()
                .isEqualTo(createUserInResponseTo(command));

        afterCommand
                .assertEntityWithState(SignIn.class, command.getId())
                .hasStateThat()
                .comparingExpectedFieldsOnly()
                .isEqualTo(completedAfter(command));

        afterCommand
                .assertEntityWithState(User.class, command.getId())
                .hasStateThat()
                .comparingExpectedFieldsOnly()
                .isEqualTo(createdUser(command));
    }

    private static SignIn completedAfter(SignUserIn command) {
        return SignIn.newBuilder()
                     .setId(command.getId())
                     .setIdentity(command.getIdentity())
                     .setStatus(COMPLETED)
                     .build();
    }

    private static User createdUser(SignUserIn command) {
        return User.newBuilder()
                   .setId(command.getId())
                   .setPrimaryIdentity(command.getIdentity())
                   .setStatus(User.Status.ACTIVE)
                   .build();
    }

    private static CreateUser createUserInResponseTo(SignUserIn command) {
        return CreateUser.newBuilder()
                         .setId(command.getId())
                         .setPrimaryIdentity(command.getIdentity())
                         .build();
    }

    private static CreateUser createUser(UserId id) {
        return CreateUser
                .newBuilder()
                .setId(id)
                .setDisplayName(userDisplayName())
                .setOrgEntity(userOrgEntity())
                .setPrimaryIdentity(googleIdentity())
                .setProfile(profile())
                .setStatus(NOT_READY)
                .setNature(PERSON)
                .vBuild();
    }

    @Test
    @DisplayName("finish the process if the user exists and is active")
    void finishProcess() {
        SignUserIn signUserIn = command();
        UserId id = signUserIn.getId();
        CreateUser createUser = createUser(id);
        SingleTenantBlackBoxContext afterCommands = context().receivesCommands(createUser,
                                                                               signUserIn);
        afterCommands
                .assertEntityWithState(SignIn.class, id)
                .hasStateThat()
                .comparingExpectedFieldsOnly()
                .isEqualTo(completedAfter(signUserIn));

        //TODO:2019-08-17:alex.tymchenko: clarify this one!
        afterCommands.assertCommands()
                     .message(0)
                     .comparingExpectedFieldsOnly()
                     .isEqualTo(finishSignIn(id));
    }

    private static FinishSignIn finishSignIn(UserId id) {
        return FinishSignIn.newBuilder()
                           .setId(id)
                           .build();
    }

    @Test
    @DisplayName("fail the process if the user exists and is NOT active")
    void failProcess() {
        SignInPm emptyProcMan = createEmptyProcMan(entityId());
        emptyProcMan.setUserRepository(nonEmptyUserRepo());
        emptyProcMan.setDirectoryFactory(mockSuspendedDirectory());

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
        emptyProcMan.setDirectoryFactory(mockActiveDirectory());
        expectThat(emptyProcMan).producesCommand(FinishSignIn.class, command -> {
            assertEquals(message().getId(), command.getId());
            assertFalse(command.getSuccessful());
            assertEquals(UNKNOWN_IDENTITY, command.getFailureReason());
        });
    }

    @Test
    @DisplayName("fail if directory is not aware of given identity")
    void failIfUnknownIdentity() {
        SignInPm emptyProcMan = createEmptyProcMan(entityId());
        emptyProcMan.setUserRepository(noIdentityUserRepo());
        emptyProcMan.setDirectoryFactory(mockEmptyDirectory());
        expectThat(emptyProcMan).producesCommand(FinishSignIn.class, command -> {
            assertEquals(message().getId(), command.getId());
            assertFalse(command.getSuccessful());
            assertEquals(UNKNOWN_IDENTITY, command.getFailureReason());
        });
    }

    @Test
    @DisplayName("fail if there is no directory")
    void failIfNoDirectory() {
        SignInPm emptyProcMan = withIdentity(entityId(), directoryId("invalid"));
        emptyProcMan.setUserRepository(noIdentityUserRepo());
        emptyProcMan.setDirectoryFactory(mockEmptyDirectoryFactory());
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
