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

package io.spine.users.server.db.given;

import io.spine.change.Changes;
import io.spine.core.UserId;
import io.spine.net.EmailAddress;
import io.spine.net.NetChange;
import io.spine.users.GroupId;
import io.spine.users.User;
import io.spine.users.db.command.AddGroupToGroup;
import io.spine.users.db.command.AddUserToGroup;
import io.spine.users.db.command.ChangeGroupDescription;
import io.spine.users.db.command.ChangeGroupEmail;
import io.spine.users.db.command.CreateGroup;
import io.spine.users.db.command.CreateUserAccount;
import io.spine.users.db.command.DeleteGroup;
import io.spine.users.db.command.RemoveGroupFromGroup;
import io.spine.users.db.command.RemoveUserFromGroup;
import io.spine.users.db.command.RenameGroup;
import io.spine.users.db.command.TerminateUserAccount;
import io.spine.users.server.given.Given;

import static io.spine.users.Role.MEMBER;
import static io.spine.users.server.db.given.Given.anotherGroupDescription;
import static io.spine.users.server.db.given.Given.anotherGroupName;
import static io.spine.users.server.db.given.Given.groupDescription;
import static io.spine.users.server.db.given.Given.groupEmail;
import static io.spine.users.server.db.given.Given.groupName;
import static io.spine.users.server.db.given.Given.newGroupEmail;

/**
 * Test commands for DB-based extensions of the Users context.
 */
public final class Command {

    /**
     * Prevents instantiation.
     */
    private Command() {
    }

    public static CreateGroup createGroup(GroupId id) {
        return CreateGroup
                .newBuilder()
                .setGroup(id)
                .setDisplayName(groupName())
                .setEmail(groupEmail())
                .setDescription(groupDescription())
                .vBuild();
    }

    public static DeleteGroup deleteGroup(GroupId groupId) {
        return DeleteGroup
                .newBuilder()
                .setGroup(groupId)
                .vBuild();
    }

    public static AddGroupToGroup addGroup(GroupId member, GroupId parent) {
        return AddGroupToGroup
                .newBuilder()
                .setGroup(member)
                .setParentGroup(parent)
                .vBuild();
    }

    public static RemoveGroupFromGroup removeGroup(GroupId member, GroupId parent) {
        return RemoveGroupFromGroup
                .newBuilder()
                .setGroup(member)
                .setParentGroup(parent)
                .vBuild();
    }

    public static RenameGroup renameGroup(GroupId group) {
        return RenameGroup
                .newBuilder()
                .setGroup(group)
                .setName(Changes.of("", anotherGroupName()))
                .vBuild();
    }

    public static ChangeGroupEmail changeGroupEmail(GroupId id) {
        return ChangeGroupEmail
                .newBuilder()
                .setGroup(id)
                .setEmail(NetChange.of(EmailAddress.getDefaultInstance(), newGroupEmail()))
                .vBuild();
    }

    public static ChangeGroupDescription changeGroupDescription(GroupId id) {
        return ChangeGroupDescription
                .newBuilder()
                .setGroup(id)
                .setDescription(Changes.of("", anotherGroupDescription()))
                .vBuild();
    }

    public static CreateUserAccount createUserAccount(UserId id) {
        return CreateUserAccount
                .newBuilder()
                .setAccount(id)
                .setUser(User.newBuilder()
                             .setPerson(Given.person())
                             .vBuild())
                .vBuild();
    }

    public static AddUserToGroup joinGroup(UserId id) {
        return AddUserToGroup
                .newBuilder()
                .setUser(id)
                .setGroup(Given.groupId())
                .setRole(MEMBER)
                .build();
    }

    public static RemoveUserFromGroup leaveGroup(UserId id) {
        return RemoveUserFromGroup
                .newBuilder()
                .setUser(id)
                .setGroup(Given.groupId())
                .build();
    }

    public static TerminateUserAccount terminateUserAccount(UserId id) {
        return TerminateUserAccount
                .newBuilder()
                .setAccount(id)
                .build();
    }
}
