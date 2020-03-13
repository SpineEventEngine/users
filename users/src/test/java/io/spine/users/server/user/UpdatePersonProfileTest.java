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
import io.spine.users.user.command.UpdatePersonProfile;
import io.spine.users.user.event.PersonProfileUpdated;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.user.given.UserTestCommands.updatePersonProfile;

@DisplayName("`UpdatePersonProfile` command should")
class UpdatePersonProfileTest
        extends UserPartCommandTest<UpdatePersonProfile, PersonProfileUpdated> {

    @Override
    @Test
    @DisplayName("generate `PersonProfileUpdated` event and update the person profile")
    protected void produceEventAndChangeState() {
        preCreateUser();
        super.produceEventAndChangeState();
    }

    @Override
    protected UpdatePersonProfile command(UserId id) {
        return updatePersonProfile(id);
    }

    @Override
    protected PersonProfileUpdated expectedEventAfter(UpdatePersonProfile command) {
        return PersonProfileUpdated
                .newBuilder()
                .setId(command.getId())
                .setUpdatedProfile(command.getUpdatedProfile())
                .buildPartial();
    }

    @Override
    protected User expectedStateAfter(UpdatePersonProfile command) {
        return User
                .newBuilder()
                .setId(command.getId())
                .setProfile(command.getUpdatedProfile())
                .buildPartial();
    }
}
