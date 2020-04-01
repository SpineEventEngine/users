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

package io.spine.users.server.user.given;

import io.spine.core.UserId;
import io.spine.users.group.command.JoinGroup;
import io.spine.users.group.command.LeaveGroup;
import io.spine.users.user.User;
import io.spine.users.user.command.CreateUserAccount;
import io.spine.users.user.command.DeleteUserAccount;

import static io.spine.users.group.RoleInGroup.MEMBER;
import static io.spine.users.server.user.given.UserTestEnv.firstGroupId;
import static io.spine.users.server.user.given.UserTestEnv.profile;

/**
 * Test commands for {@code UserPart}.
 */
public class UserTestCommands {

    /** Prevents instantiation of this utility class. */
    private UserTestCommands() {
    }

    public static CreateUserAccount createUser(UserId id) {
        return CreateUserAccount
                .newBuilder()
                .setId(id)
                .setUser(User.newBuilder()
                             .setPerson(profile())
                             .vBuild())
                .vBuild();
    }

    public static JoinGroup joinGroup(UserId id) {
        return JoinGroup
                .newBuilder()
                .setId(id)
                .setGroupId(firstGroupId())
                .setRole(MEMBER)
                .build();
    }

    public static LeaveGroup leaveGroup(UserId id) {
        return LeaveGroup
                .newBuilder()
                .setId(id)
                .setGroupId(firstGroupId())
                .build();
    }

    public static DeleteUserAccount deleteUser(UserId id) {
        return DeleteUserAccount
                .newBuilder()
                .setId(id)
                .build();
    }
}
