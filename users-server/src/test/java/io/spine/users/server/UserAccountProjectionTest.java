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

package io.spine.users.server;

import io.spine.core.UserId;
import io.spine.users.AccountStatus;
import io.spine.users.User;
import io.spine.users.UserAccount;
import io.spine.users.event.UserAccountCreated;
import io.spine.users.event.UserAccountResumed;
import io.spine.users.event.UserAccountSuspended;
import io.spine.users.event.UserAccountTerminated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.spine.users.AccountStatus.ACTIVE;
import static io.spine.users.AccountStatus.PENDING;
import static io.spine.users.AccountStatus.SUSPENDED;
import static io.spine.users.AccountStatus.TERMINATED;
import static io.spine.users.server.given.Given.humanUser;
import static io.spine.users.server.given.Given.service;
import static io.spine.users.server.given.Given.userId;

class UserAccountProjectionTest extends UsersContextTest {

    private UserId id;
    private User humanUser;
    private User service;

    @BeforeEach
    void generateData() {
        id = userId();
        humanUser = humanUser();
        service = service();
    }

    @Nested
    @DisplayName("mark the account upon creation")
    class Creation {

        @Test
        @DisplayName("as `ACTIVE` when the status is not specified in the event")
        void withAssumedStatus() {
            UserAccountCreated event = created(id, humanUser);
            context().receivesEvent(event);

            UserAccount expected = account(id, humanUser, ACTIVE);
            assertState(expected);
        }

        @Test
        @DisplayName("using the status passed in the event")
        void withExplicitStatus() {
            UserAccountCreated event = created(id, humanUser, PENDING);
            context().receivesEvent(event);

            UserAccount expected = account(id, humanUser, PENDING);
            assertState(expected);
        }
    }

    @Test
    @DisplayName("set the status to `TERMINATED`")
    void whenTerminated() {
        UserAccountCreated created = created(id, service);
        UserAccountTerminated terminated = UserAccountTerminated
                .newBuilder()
                .setAccount(id)
                .vBuild();

        context().receivesEvent(created)
                 .receivesEvent(terminated);

        UserAccount expected = account(id, service, TERMINATED);
        assertState(expected);
    }

    @Test
    @DisplayName("set the status to `SUSPENDED`")
    void whenSuspended() {
        UserAccountCreated created = created(id, humanUser);
        UserAccountSuspended suspended = UserAccountSuspended
                .newBuilder()
                .setAccount(id)
                .vBuild();

        context().receivesEvent(created)
                 .receivesEvent(suspended);

        UserAccount expected = account(id, humanUser, SUSPENDED);
        assertState(expected);
    }

    @Test
    @DisplayName("set the status back to `ACTIVE` when the account was resumed")
    void whenResumed() {
        UserAccountCreated created = created(id, humanUser, SUSPENDED);
        UserAccountResumed resumed = UserAccountResumed
                .newBuilder()
                .setAccount(id)
                .vBuild();

        context().receivesEvent(created)
                 .receivesEvent(resumed);

        UserAccount expected = account(id, humanUser, ACTIVE);
        assertState(expected);
    }

    private void assertState(UserAccount expected) {
        context().assertState(id, UserAccount.class)
                 .comparingExpectedFieldsOnly()
                 .isEqualTo(expected);
    }

    private static UserAccountCreated created(UserId id, User user) {
        return UserAccountCreated
                .newBuilder()
                .setAccount(id)
                .setUser(user)
                .vBuild();
    }

    private static UserAccountCreated created(UserId id, User user, AccountStatus status) {
        return UserAccountCreated
                .newBuilder()
                .setAccount(id)
                .setUser(user)
                .setStatus(status)
                .vBuild();
    }

    private static UserAccount account(UserId id, User user, AccountStatus status) {
        return UserAccount
                .newBuilder()
                .setId(id)
                .setUser(user)
                .setStatus(status)
                .build();
    }
}
