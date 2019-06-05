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

package io.spine.users.server.given;

import io.spine.core.UserId;
import io.spine.users.GroupId;
import io.spine.users.RoleId;
import io.spine.users.group.command.AssignRoleToGroup;
import io.spine.users.group.command.UnassignRoleFromGroup;
import io.spine.users.user.command.AssignRoleToUser;
import io.spine.users.user.command.UnassignRoleFromUser;

/**
 * Factory methods for creating of test commands.
 */
public class GivenCommand {

    /** Prevents instantiation of this utility class. */
    private GivenCommand() {
    }

    public static AssignRoleToUser assignRoleToUser(UserId userId, RoleId roleId) {
        return AssignRoleToUser
                .newBuilder()
                .setId(userId)
                .setRoleId(roleId)
                .vBuild();
    }

    public static UnassignRoleFromUser unassignRoleFromUser(UserId userId, RoleId roleId) {
        return UnassignRoleFromUser
                .newBuilder()
                .setId(userId)
                .setRoleId(roleId)
                .vBuild();
    }

    public static AssignRoleToGroup assignRoleToGroup(GroupId groupId, RoleId roleId) {
        return AssignRoleToGroup
                .newBuilder()
                .setId(groupId)
                .setRoleId(roleId)
                .vBuild();
    }

    public static UnassignRoleFromGroup unassignRoleFromGroup(GroupId groupId, RoleId roleId) {
        return UnassignRoleFromGroup
                .newBuilder()
                .setId(groupId)
                .setRoleId(roleId)
                .vBuild();
    }
}
