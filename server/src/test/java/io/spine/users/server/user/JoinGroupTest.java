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
import io.spine.users.group.Membership;
import io.spine.users.group.UserMembership;
import io.spine.users.group.command.AddUserToGroup;
import io.spine.users.group.event.UserAddedToGroup;
import io.spine.users.server.UserMembershipCommandTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.user.given.UserTestCommands.joinGroup;

@DisplayName("`JoinGroup` command should")
class JoinGroupTest extends UserMembershipCommandTest<AddUserToGroup, UserAddedToGroup> {

    @Test
    @DisplayName("produce `UserJoinedGroup` event and update the user group membership")
    @Override
    protected void produceEventAndChangeState() {
        super.produceEventAndChangeState();
    }

    @Override
    protected AddUserToGroup command(UserId id) {
        return joinGroup(id);
    }

    @Override
    protected UserAddedToGroup expectedEventAfter(AddUserToGroup command) {
        return UserAddedToGroup
                .newBuilder()
                .setUser(command.getUser())
                .setGroup(command.getGroup())
                .setRole(command.getRole())
                .buildPartial();
    }

    @Override
    protected UserMembership expectedStateAfter(AddUserToGroup command) {
        return UserMembership
                .newBuilder()
                .setUser(command.getUser())
                .addMembership(membershipAfter(command))
                .buildPartial();
    }

    private static Membership membershipAfter(AddUserToGroup command) {
        return Membership
                .newBuilder()
                .setGroup(command.getGroup())
                .setRole(command.getRole())
                .buildPartial();
    }
}
