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

package io.spine.users.google.server;

import io.spine.users.GroupId;
import io.spine.users.google.event.GoogleGroupDescriptionChanged;
import io.spine.users.db.command.ChangeGroupDescription;
import io.spine.users.db.command.CreateGroup;
import io.spine.users.server.UsersContextTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.google.server.given.GoogleGroupTestEnv.newGroupId;
import static io.spine.users.google.server.given.GoogleGroupTestEvents.googleGroupDescriptionChanged;
import static io.spine.users.server.db.given.GroupTestCommands.createGroup;

@DisplayName("`GoogleGroupPm` should, when `GoogleGroupDescriptionChanged`")
@Disabled("Until new API is introduced")
class GroupDescriptionChangedTest extends UsersContextTest {

    @Test
    @DisplayName("translate it to `ChangeGroupDescription` command")
    void testBeTranslated() {
        GroupId groupId = newGroupId();
        CreateGroup createGroup = createGroup(groupId);
        GoogleGroupDescriptionChanged event = googleGroupDescriptionChanged(groupId);
        ChangeGroupDescription expectedCmd = ChangeGroupDescription
                .newBuilder()
                .setGroup(groupId)
                .setDescription(event.getNewDescription())
                .build();
        context().receivesCommand(createGroup)
                 .receivesEvent(event)
                 .assertCommands()
                 .withType(ChangeGroupDescription.class)
                 .message(0)
                 .comparingExpectedFieldsOnly()
                 .isEqualTo(expectedCmd);
    }
}
