/*
 * Copyright 2020, TeamDev. All rights reserved.
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

package io.spine.users.server.user;

import io.spine.core.UserId;
import io.spine.testing.server.blackbox.BlackBoxContext;
import io.spine.users.server.UsersContextTest;
import io.spine.users.user.UserAccount;
import io.spine.users.user.command.CreateUserAccount;
import io.spine.users.user.command.DeleteUserAccount;
import io.spine.users.user.event.UserAccountCreated;
import io.spine.users.user.rejection.Rejections.UnavalableForPreviouslyDeletedAccount;
import io.spine.users.user.rejection.Rejections.UserAccountAlreadyExists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.given.Given.userId;
import static io.spine.users.server.user.given.UserTestCommands.createUserAccount;
import static io.spine.users.server.user.given.UserTestCommands.deleteUserAccount;

@DisplayName("A User Account should")
class UserAccountTest extends UsersContextTest {

    private UserId user;
    private BlackBoxContext context;

    @BeforeEach
    void createAccount() {
        user = userId();
        context = context();
    }

    @Test
    @DisplayName("be created on a command")
    void creation() {
        CreateUserAccount cmd = createUserAccount(user);
        context.receivesCommand(cmd);

        assertEvent(UserAccountCreated.class)
                .isEqualTo(
                        UserAccountCreated
                                .newBuilder()
                                .setAccount(user)
                                .setUser(cmd.getUser())
                                .build()
                );

        assertEntityState(UserAccount.class, user)
                .isEqualTo(
                        UserAccount
                                .newBuilder()
                                .setId(user)
                                .setUser(cmd.getUser())
                                .build()
                );
    }

    @Test
    @DisplayName("be deleted on a command")
    void deletion() {
        context.receivesCommand(createUserAccount(user));

        DeleteUserAccount cmd = deleteUserAccount(user);
        context.receivesCommand(cmd);

        context.assertEntityWithState(UserAccount.class, user)
               .deletedFlag()
               .isTrue();
    }

    @Nested
    @DisplayName("reject creation if an account")
    class RejectCreation {

        @BeforeEach
        void createAccount() {
            context.receivesCommand(createUserAccount(user));
        }

        @Test
        @DisplayName("already exists")
        void ifDuplicate() {
            // Attempt to create the account with the same ID.
            context.receivesCommand(createUserAccount(user));

            assertEvent(UserAccountAlreadyExists.class)
                    .isEqualTo(
                            UserAccountAlreadyExists
                                    .newBuilder()
                                    .setAccount(user)
                                    .vBuild()
                    );
        }

        @Test
        @DisplayName("was previously deleted")
        void ifDeleted() {
            // Delete the account which was previously created.
            context.receivesCommand(deleteUserAccount(user));

            context.receivesCommand(createUserAccount(user));

            assertEvent(UnavalableForPreviouslyDeletedAccount.class)
                    .isEqualTo(
                            UnavalableForPreviouslyDeletedAccount
                                    .newBuilder()
                                    .setAccount(user)
                                    .build()
                    );
        }
    }
}
