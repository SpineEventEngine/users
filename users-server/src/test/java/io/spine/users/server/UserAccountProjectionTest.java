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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.given.Given.humanUser;
import static io.spine.users.server.given.Given.userId;

class UserAccountProjectionTest extends UsersContextTest {

    @Test
    @DisplayName("mark the account as `ACTIVE` upon creation")
    void whenCreated() {
        UserId id = userId();
        User user = humanUser();
        UserAccountCreated event = UserAccountCreated
                .newBuilder()
                .setAccount(id)
                .setUser(user)
                .vBuild();
        context().receivesEvent(event);

        UserAccount expected = UserAccount
                .newBuilder()
                .setId(id)
                .setUser(user)
                .setStatus(AccountStatus.ACTIVE)
                .build();

        context().assertState(id, UserAccount.class)
                 .comparingExpectedFieldsOnly()
                 .isEqualTo(expected);
    }
}
