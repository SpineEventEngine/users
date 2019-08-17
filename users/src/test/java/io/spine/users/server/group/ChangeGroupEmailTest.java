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

package io.spine.users.server.group;

import io.spine.net.EmailAddress;
import io.spine.testing.server.blackbox.SingleTenantBlackBoxContext;
import io.spine.users.group.Group;
import io.spine.users.group.command.ChangeGroupDescription;
import io.spine.users.group.command.ChangeGroupEmail;
import io.spine.users.group.event.GroupDescriptionChanged;
import io.spine.users.group.event.GroupEmailChanged;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.group.given.GroupTestCommands.changeGroupDescription;
import static io.spine.users.server.group.given.GroupTestCommands.changeGroupEmail;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("`ChangeGroupEmail` command should")
class ChangeGroupEmailTest extends GroupCommandTest<ChangeGroupEmail> {

    @Test
    @DisplayName("produce `GroupEmailChanged` event and set the updated email")
    void produceEventAndChangeState() {
        createPartWithState();
        ChangeGroupEmail command = changeGroupEmail(GROUP_ID);
        SingleTenantBlackBoxContext afterCommand = context().receivesCommand(command);
        GroupEmailChanged expectedEvent = expectedEvent(command);
        afterCommand.assertEvents()
                    .message(0)
                    .comparingExpectedFieldsOnly()
                    .isEqualTo(expectedEvent);

        Group expectedState = expectedState(command);
        afterCommand.assertEntity(GroupPart.class, GROUP_ID)
                    .hasStateThat()
                    .comparingExpectedFieldsOnly()
                    .isEqualTo(expectedState);
    }

    private static Group expectedState(ChangeGroupEmail command) {
        return Group
                .newBuilder()
                .setId(command.getId())
                .setEmail(command.getNewEmail())
                .build();
    }

    private static GroupEmailChanged expectedEvent(ChangeGroupEmail command) {
        return GroupEmailChanged
                .newBuilder()
                .setId(command.getId())
                .setNewEmail(command.getNewEmail())
                .build();
    }
}
