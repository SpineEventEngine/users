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

import io.spine.users.GroupId;
import io.spine.users.group.GroupMembership;
import io.spine.users.group.command.LeaveParentGroup;
import io.spine.users.group.event.GroupRemovedFromGroup;
import io.spine.users.server.GroupMembershipCommandTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.group.given.GroupTestCommands.leaveParentGroup;

@DisplayName("`LeaveParentGroup` command should")
class LeaveParentGroupTest extends GroupMembershipCommandTest<LeaveParentGroup, GroupRemovedFromGroup> {

    @Test
    @DisplayName("produce `LeftParentGroup` event and clear the respective membership")
    @Override
    protected void produceEventAndChangeState() {
        preCreateGroupMembership();
        super.produceEventAndChangeState();
    }

    @Override
    protected LeaveParentGroup command(GroupId id) {
        return leaveParentGroup(id, PARENT_GROUP_ID);
    }

    @Override
    protected GroupRemovedFromGroup expectedEventAfter(LeaveParentGroup command) {
        return GroupRemovedFromGroup
                .newBuilder()
                .setGroup(command.getGroup())
                .setParentGroup(command.getParentGroup())
                .build();
    }

    @Override
    protected GroupMembership expectedStateAfter(LeaveParentGroup command) {
        return GroupMembership
                .newBuilder()
                .setGroup(command.getGroup())
                .build();
    }
}
