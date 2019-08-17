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
import io.spine.users.group.GroupMembership;
import io.spine.users.group.command.LeaveParentGroup;
import io.spine.users.group.event.LeftParentGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.group.given.GroupTestCommands.leaveParentGroup;

@DisplayName("`LeaveParentGroup` command should")
class LeaveParentGroupTest extends GroupMembershipCommandTest<LeaveParentGroup> {

    @Test
    @DisplayName("produce `LeftParentGroup` event and clear the respective membership")
    void produceEventAndChangeState() {
        createPartWithState();
        LeaveParentGroup command = leaveParentGroup(GROUP_ID, PARENT_GROUP_ID);
        SingleTenantBlackBoxContext afterCommand = context().receivesCommand(command);
        LeftParentGroup expectedEvent = expectedEvent(command);
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

    private static GroupMembership expectedState(LeaveParentGroup command) {
        return GroupMembership
                .newBuilder()
                .setId(command.getId())
                .build();
    }

    private static LeftParentGroup expectedEvent(LeaveParentGroup command) {
        return LeftParentGroup
                .newBuilder()
                .setId(command.getId())
                .setParentGroupId(command.getParentGroupId())
                .build();
    }
}
