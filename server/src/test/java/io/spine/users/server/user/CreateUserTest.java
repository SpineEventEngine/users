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

package io.spine.users.server.user;

import io.spine.core.UserId;
import io.spine.users.server.UserPartCommandTest;
import io.spine.users.user.UserAccount;
import io.spine.users.user.command.CreateUserAccount;
import io.spine.users.user.event.UserAccountCreated;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.user.given.UserTestCommands.createUser;

@DisplayName("`CreateUser` command should")
class CreateUserTest extends UserPartCommandTest<CreateUserAccount, UserAccountCreated> {

    @Override
    @Test
    @DisplayName("generate `UserCreated` event and create the `User`")
    protected void produceEventAndChangeState() {
        super.produceEventAndChangeState();
    }

    @Override
    protected CreateUserAccount command(UserId id) {
        return createUser(id);
    }

    @Override
    protected UserAccountCreated expectedEventAfter(CreateUserAccount command) {
        return UserAccountCreated
                .newBuilder()
                .setId(command.getId())
                .setUser(command.getUser())
                .build();
    }

    @Override
    protected UserAccount expectedStateAfter(CreateUserAccount command) {
        return UserAccount
                .newBuilder()
                .setId(command.getId())
                .setUser(command.getUser())
                .build();
    }
}
