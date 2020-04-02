/*
 * Copyright 2020, TeamDev. All rights reserved.
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

package io.spine.roles.server.given;

import io.spine.core.UserId;
import io.spine.roles.RoleId;
import io.spine.roles.command.AssignRoleToGroup;
import io.spine.roles.command.AssignRoleToUser;
import io.spine.roles.command.CreateRole;
import io.spine.roles.command.DeleteRole;
import io.spine.roles.command.RemoveRoleAssignmentFromGroup;
import io.spine.roles.command.RemoveRoleAssignmentFromUser;
import io.spine.users.GroupId;
import io.spine.users.group.RoleInGroup;
import io.spine.users.group.command.AddUserToGroup;
import io.spine.users.group.command.CreateGroup;
import io.spine.users.group.command.RemoveUserFromGroup;
import io.spine.users.server.group.given.GroupTestEnv;
import io.spine.users.user.command.CreateUserAccount;

import static io.spine.users.server.given.Given.personName;
import static io.spine.users.server.given.Given.user;

public class GivenCommand {

    /** Prevents instantiation of this utility class. */
    private GivenCommand() {
    }

    public static CreateUserAccount createUser(UserId userId) {
        return CreateUserAccount
                .newBuilder()
                .setAccount(userId)
                .setUser(user(personName("John", "Doe")))
                .vBuild();
    }

    public static CreateGroup createGroup(GroupId group) {
        return CreateGroup
                .newBuilder()
                .setId(group)
                .setDisplayName("group " + group.getValue())
                .setEmail(GroupTestEnv.groupEmail())
                .setDescription(GroupTestEnv.groupDescription())
                .build();
    }

    public static AddUserToGroup joinGroup(UserId user, GroupId groupId) {
        return AddUserToGroup
                .newBuilder()
                .setId(user)
                .setGroupId(groupId)
                .setRole(RoleInGroup.MEMBER)
                .build();
    }

    public static RemoveUserFromGroup leaveGroup(UserId userId, GroupId group) {
        return RemoveUserFromGroup
                .newBuilder()
                .setId(userId)
                .setGroupId(group)
                .build();
    }

    public static CreateRole createRole(RoleId r, String displayName) {
        return CreateRole
                .newBuilder()
                .setRole(r)
                .setDisplayName(displayName)
                .build();
    }

    public static DeleteRole deleteRole(RoleId r) {
        return DeleteRole
                .newBuilder()
                .setRole(r)
                .vBuild();
    }

    public static AssignRoleToUser assignRoleToUser(RoleId r, UserId u) {
        return AssignRoleToUser
                .newBuilder()
                .setUser(u)
                .setRole(r)
                .vBuild();
    }

    public static RemoveRoleAssignmentFromUser removeRoleFromUser(RoleId r, UserId u) {
        return RemoveRoleAssignmentFromUser
                .newBuilder()
                .setUser(u)
                .setRole(r)
                .vBuild();
    }

    public static AssignRoleToGroup assignRoleToGroup(RoleId r, GroupId g) {
        return AssignRoleToGroup
                .newBuilder()
                .setGroup(g)
                .setRole(r)
                .vBuild();
    }

    public static RemoveRoleAssignmentFromGroup removeRoleFromGroup(RoleId r, GroupId g) {
        return RemoveRoleAssignmentFromGroup
                .newBuilder()
                .setGroup(g)
                .setRole(r)
                .vBuild();
    }
}
