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

import io.spine.testing.server.blackbox.MultitenantBlackBoxContext;
import io.spine.users.GroupId;
import io.spine.users.group.GroupMembership;
import io.spine.users.group.command.JoinParentGroup;
import io.spine.users.group.event.JoinedParentGroup;
import io.spine.users.server.group.given.GroupTestEnv;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.group.given.GroupTestCommands.joinParentGroup;

@DisplayName("`JoinParentGroup` command should")
class JoinParentGroupTest extends GroupMembershipCommandTest<JoinParentGroup> {

    private static final GroupId SUPER_GROUP = GroupTestEnv.createGroupId();

    @Test
    @DisplayName("produce `JoinedParentGroup` event and create the group membership")
    void produceEventAndChangeState() {
        JoinParentGroup command = joinParentGroup(GROUP_ID, PARENT_GROUP_ID);
        MultitenantBlackBoxContext afterCommand = context().receivesCommand(command);
        JoinedParentGroup expectedEvent = expectedEvent(command);
        afterCommand.assertEvents()
                    .message(0)
                    .comparingExpectedFieldsOnly()
                    .isEqualTo(expectedEvent);

        GroupMembership expectedState = expectedState(command);
        afterCommand.assertEntity(GroupMembershipPart.class, GROUP_ID)
                    .hasStateThat()
                    .comparingExpectedFieldsOnly()
                    .isEqualTo(expectedState);
    }

    private static GroupMembership expectedState(JoinParentGroup command) {
        return GroupMembership
                .newBuilder()
                .setId(command.getId())
                .addMembership(command.getParentGroupId())
                .build();
    }

    private static JoinedParentGroup expectedEvent(JoinParentGroup command) {
        return JoinedParentGroup
                .newBuilder()
                .setId(command.getId())
                .setParentGroupId(command.getParentGroupId())
                .build();
    }
}
