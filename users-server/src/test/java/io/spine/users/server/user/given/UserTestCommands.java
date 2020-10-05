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
import io.spine.users.command.AddUserToGroup;
import io.spine.users.command.RemoveUserFromGroup;
import io.spine.users.server.given.TestIdentifiers;
import io.spine.users.User;
import io.spine.users.command.CreateUserAccount;
import io.spine.users.command.DeleteUserAccount;

import static io.spine.users.Role.MEMBER;
import static io.spine.users.server.user.given.UserTestEnv.profile;

/**
 * Test commands for {@code UserPart}.
 */
public class UserTestCommands {

    /** Prevents instantiation of this utility class. */
    private UserTestCommands() {
    }

    public static CreateUserAccount createUserAccount(UserId id) {
        return CreateUserAccount
                .newBuilder()
                .setAccount(id)
                .setUser(User.newBuilder()
                             .setPerson(profile())
                             .vBuild())
                .vBuild();
    }

    public static AddUserToGroup joinGroup(UserId id) {
        return AddUserToGroup
                .newBuilder()
                .setUser(id)
                .setGroup(TestIdentifiers.groupId())
                .setRole(MEMBER)
                .build();
    }

    public static RemoveUserFromGroup leaveGroup(UserId id) {
        return RemoveUserFromGroup
                .newBuilder()
                .setUser(id)
                .setGroup(TestIdentifiers.groupId())
                .build();
    }

    public static DeleteUserAccount deleteUserAccount(UserId id) {
        return DeleteUserAccount
                .newBuilder()
                .setAccount(id)
                .build();
    }
}
