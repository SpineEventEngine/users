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
import io.spine.testing.server.blackbox.MultitenantBlackBoxContext;
import io.spine.users.server.UserPartCommandTest;
import io.spine.users.user.User;
import io.spine.users.user.command.AddSecondaryIdentity;
import io.spine.users.user.event.SecondaryIdentityAdded;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.user.given.UserTestCommands.addAuthIdentity;

@DisplayName("`AddAuthIdentity` command should")
class AddAuthIdentityTest
        extends UserPartCommandTest<AddSecondaryIdentity, SecondaryIdentityAdded> {

    @Test
    @DisplayName("produce `SecondaryIdentityAdded` event and add the second identity for the user")
    @Override
    protected void produceEventAndChangeState() {
        preCreateUser();
        super.produceEventAndChangeState();
    }

    @Override
    protected void assertEvent(MultitenantBlackBoxContext afterCommand,
                               SecondaryIdentityAdded expectedEvent) {
        /*
         The second event of {@code SecondaryIdentityAdded} type is checked,
         as the {@link #preCreateUser} also leads to the {@code SecondaryIdentityAdded} event.
         */
        afterCommand.assertEvents()
                    .withType(expectedEvent.getClass())
                    .message(1)
                    .comparingExpectedFieldsOnly()
                    .isEqualTo(expectedEvent);
    }

    @Override
    protected AddSecondaryIdentity command(UserId id) {
        return addAuthIdentity(id);
    }

    @Override
    protected SecondaryIdentityAdded expectedEventAfter(AddSecondaryIdentity command) {
        return SecondaryIdentityAdded
                .newBuilder()
                .setId(command.getId())
                .setIdentity(command.getIdentity())
                .buildPartial();
    }

    @Override
    protected User expectedStateAfter(AddSecondaryIdentity command) {
        return User
                .newBuilder()
                .setId(command.getId())

                // The one which was there previously.
                .addSecondaryIdentity(originalSecondaryIdentity())

                // The one that should have been added as the result of command dispatching.
                .addSecondaryIdentity(command.getIdentity())
                .buildPartial();
    }
}
