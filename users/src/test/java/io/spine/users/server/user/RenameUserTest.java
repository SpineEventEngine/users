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
import io.spine.users.user.User;
import io.spine.users.user.command.RenameUser;
import io.spine.users.user.event.UserRenamed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.user.given.UserTestCommands.renameUser;

@DisplayName("`RenameUser` command should")
class RenameUserTest extends UserPartCommandTest<RenameUser, UserRenamed> {

    @Test
    @DisplayName("produce `UserRenamed` event and change the name of the user")
    @Override
    protected void produceEventAndChangeState() {
        preCreateUser();
        super.produceEventAndChangeState();
    }

    @Override
    protected RenameUser command(UserId id) {
        return renameUser(id);
    }

    @Override
    protected UserRenamed expectedEventAfter(RenameUser command) {
        return UserRenamed
                .newBuilder()
                .setId(command.getId())
                .setNewName(command.getNewName())
                .buildPartial();
    }

    @Override
    protected User expectedStateAfter(RenameUser command) {
        return User
                .newBuilder()
                .setId(command.getId())
                .setDisplayName(command.getNewName())
                .buildPartial();
    }
}
