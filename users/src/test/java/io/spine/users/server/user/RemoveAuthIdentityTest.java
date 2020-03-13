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
import io.spine.users.user.User;
import io.spine.users.user.command.RemoveSecondaryIdentity;
import io.spine.users.user.event.SecondaryIdentityRemoved;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.user.given.UserTestCommands.removeAuthIdentity;

@DisplayName("`RemoveSecondaryIdentity` command should")
class RemoveAuthIdentityTest
        extends UserPartCommandTest<RemoveSecondaryIdentity, SecondaryIdentityRemoved> {

    @Test
    @DisplayName("produce `SecondaryIdentityRemoved` event and add the second identity for the user")
    @Override
    protected void produceEventAndChangeState() {
        preCreateUser();
        super.produceEventAndChangeState();
    }

    @Override
    protected RemoveSecondaryIdentity command(UserId id) {
        return removeAuthIdentity(id);
    }

    @Override
    protected SecondaryIdentityRemoved expectedEventAfter(RemoveSecondaryIdentity command) {
        return SecondaryIdentityRemoved
                .newBuilder()
                .setId(command.getId())
                .setIdentity(originalSecondaryIdentity())
                .buildPartial();
    }

    //TODO:2019-08-18:alex.tymchenko:  find out how to check that the identity has been removed
    @Override
    protected User expectedStateAfter(RemoveSecondaryIdentity command) {
        return User
                .newBuilder()
                .setId(command.getId())
                .buildPartial();
    }
}
