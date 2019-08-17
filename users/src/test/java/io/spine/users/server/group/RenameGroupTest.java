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

import io.spine.testing.server.blackbox.SingleTenantBlackBoxContext;
import io.spine.users.group.Group;
import io.spine.users.group.command.RenameGroup;
import io.spine.users.group.event.GroupRenamed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.group.given.GroupTestCommands.renameGroup;

@DisplayName("`RenameGroup` command should")
class RenameGroupTest extends GroupCommandTest<RenameGroup> {

    @Test
    @DisplayName("produce `GroupRenamed` event and change the display name")
    void produceEventAndChangeState() {
        createPartWithState();
        RenameGroup command = renameGroup(GROUP_ID);
        SingleTenantBlackBoxContext afterCommand = context().receivesCommand(command);
        GroupRenamed expectedEvent = expectedEvent(command);
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

    private static Group expectedState(RenameGroup command) {
        return Group
                .newBuilder()
                .setId(command.getId())
                .setDisplayName(command.getNewName())
                .build();
    }

    private static GroupRenamed expectedEvent(RenameGroup command) {
        return GroupRenamed
                .newBuilder()
                .setId(command.getId())
                .setNewName(command.getNewName())
                .build();
    }
}
