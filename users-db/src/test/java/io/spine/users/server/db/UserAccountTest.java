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

package io.spine.users.server.db;

import io.spine.core.UserId;
import io.spine.testing.server.blackbox.BlackBoxContext;
import io.spine.users.db.UserAccount;
import io.spine.users.db.command.CreateUserAccount;
import io.spine.users.db.command.TerminateUserAccount;
import io.spine.users.db.rejection.Rejections.UnavailableForTerminatedAccount;
import io.spine.users.event.UserAccountCreated;
import io.spine.users.db.rejection.Rejections.UserAccountAlreadyExists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.db.given.Command.createUserAccount;
import static io.spine.users.server.db.given.Command.terminateUserAccount;
import static io.spine.users.server.given.Given.userId;

@DisplayName("A User Account should")
class UserAccountTest extends DbExtensionsTest {

    private UserId user;
    private BlackBoxContext context;

    @BeforeEach
    void initFields() {
        user = userId();
        context = context();
    }

    @Nested
    @DisplayName("be created on a command")
    class Creation {

        private CreateUserAccount cmd;

        @BeforeEach
        void createAccount() {
            cmd = createUserAccount(user);
            context.receivesCommand(cmd);
        }

        @Test
        @DisplayName("emitting event")
        void event() {
            assertEvent(UserAccountCreated
                                .newBuilder()
                                .setAccount(user)
                                .setUser(cmd.getUser())
                                .build());
        }

        @Test
        @DisplayName("creating entity with state")
        void entityState() {
            assertEntity(user,
                         UserAccount
                                 .newBuilder()
                                 .setId(user)
                                 .setUser(cmd.getUser())
                                 .build());
        }

        @Nested
        @DisplayName("rejecting if")
        class Rejecting {

            @Test
            @DisplayName("an account with ID already exists")
            void ifDuplicate() {
                // Attempt to create the account with the same ID.
                context.receivesCommand(createUserAccount(user));

                assertEvent(UserAccountAlreadyExists
                                    .newBuilder()
                                    .setAccount(user)
                                    .vBuild());
            }

            @Test
            @DisplayName("an account with such ID was previously deleted")
            void ifDeletedBefore() {
                // Delete the account which was previously created.
                context.receivesCommand(terminateUserAccount(user));

                context.receivesCommand(createUserAccount(user));

                assertEvent(UnavailableForTerminatedAccount
                                    .newBuilder()
                                    .setAccount(user)
                                    .build());
            }
        }

    }

    @Test
    @DisplayName("be deleted on a command")
    void deletion() {
        context.receivesCommand(createUserAccount(user));

        TerminateUserAccount cmd = terminateUserAccount(user);
        context.receivesCommand(cmd);

        context.assertEntityWithState(user, UserAccount.class)
               .deletedFlag()
               .isTrue();
    }
}
