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
import io.spine.users.server.UserMembershipCommandTest;
import io.spine.users.group.UserMembership;
import io.spine.users.group.command.LeaveGroup;
import io.spine.users.group.event.UserLeftGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.user.given.UserTestCommands.joinGroup;
import static io.spine.users.server.user.given.UserTestCommands.leaveGroup;

@DisplayName("`LeaveGroup` command should")
class LeaveGroupTest extends UserMembershipCommandTest<LeaveGroup, UserLeftGroup> {

    @Test
    @DisplayName("produce `UserLeftGroup` event and update the user group membership")
    @Override
    protected void produceEventAndChangeState() {
        context().receivesCommand(joinGroup(entityId()));
        super.produceEventAndChangeState();
    }

    @Override
    protected LeaveGroup command(UserId id) {
        return leaveGroup(id);
    }

    @Override
    protected UserLeftGroup expectedEventAfter(LeaveGroup command) {
        return UserLeftGroup
                .newBuilder()
                .setId(command.getId())
                .setGroupId(command.getGroupId())
                .buildPartial();
    }

    @Override
    protected UserMembership expectedStateAfter(LeaveGroup command) {
        return UserMembership
                .newBuilder()
                .setId(command.getId())
                .buildPartial();
    }
}
