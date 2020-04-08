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

package io.spine.users.server.group.given;

import io.spine.users.GroupId;
import io.spine.users.group.command.AddGroupToGroup;
import io.spine.users.group.command.ChangeGroupDescription;
import io.spine.users.group.command.ChangeGroupEmail;
import io.spine.users.group.command.CreateGroup;
import io.spine.users.group.command.DeleteGroup;
import io.spine.users.group.command.RemoveGroupFromGroup;
import io.spine.users.group.command.RenameGroup;

import static io.spine.users.server.group.given.GroupTestEnv.anotherGroupDescription;
import static io.spine.users.server.group.given.GroupTestEnv.anotherGroupName;
import static io.spine.users.server.group.given.GroupTestEnv.groupDescription;
import static io.spine.users.server.group.given.GroupTestEnv.groupEmail;
import static io.spine.users.server.group.given.GroupTestEnv.groupName;
import static io.spine.users.server.group.given.GroupTestEnv.newGroupEmail;

/**
 * Test commands for {@code GroupAccount}.
 */
public class GroupTestCommands {

    /**
     * Prevents instantiation.
     */
    private GroupTestCommands() {
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
                .setNewName(anotherGroupName())
                .vBuild();
    }

    public static ChangeGroupEmail changeGroupEmail(GroupId id) {
        return ChangeGroupEmail
                .newBuilder()
                .setGroup(id)
                .setNewEmail(newGroupEmail())
                .vBuild();
    }

    public static ChangeGroupDescription changeGroupDescription(GroupId id) {
        return ChangeGroupDescription
                .newBuilder()
                .setGroup(id)
                .setDescription(anotherGroupDescription())
                .vBuild();
    }
}
