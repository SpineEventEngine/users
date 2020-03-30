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
import io.spine.users.group.UserMembershipRecord;
import io.spine.users.group.command.JoinGroup;
import io.spine.users.group.event.UserJoinedGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.user.given.UserTestCommands.joinGroup;

@DisplayName("`JoinGroup` command should")
class JoinGroupTest extends UserMembershipCommandTest<JoinGroup, UserJoinedGroup> {

    @Test
    @DisplayName("produce `UserJoinedGroup` event and update the user group membership")
    @Override
    protected void produceEventAndChangeState() {
        super.produceEventAndChangeState();
    }

    @Override
    protected JoinGroup command(UserId id) {
        return joinGroup(id);
    }

    @Override
    protected UserJoinedGroup expectedEventAfter(JoinGroup command) {
        return UserJoinedGroup
                .newBuilder()
                .setId(command.getId())
                .setGroupId(command.getGroupId())
                .setRole(command.getRole())
                .buildPartial();
    }

    @Override
    protected UserMembership expectedStateAfter(JoinGroup command) {
        return UserMembership
                .newBuilder()
                .setId(command.getId())
                .addMembership(membershipAfter(command))
                .buildPartial();
    }

    private static UserMembershipRecord membershipAfter(JoinGroup command) {
        return UserMembershipRecord
                .newBuilder()
                .setGroupId(command.getGroupId())
                .setRole(command.getRole())
                .buildPartial();
    }
}
