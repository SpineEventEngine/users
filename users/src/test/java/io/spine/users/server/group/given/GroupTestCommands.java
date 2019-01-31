/*
 * Copyright 2018, TeamDev. All rights reserved.
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
import io.spine.users.OrganizationOrUnit;
import io.spine.users.group.command.ChangeGroupDescription;
import io.spine.users.group.command.ChangeGroupDescriptionVBuilder;
import io.spine.users.group.command.ChangeGroupEmail;
import io.spine.users.group.command.ChangeGroupEmailVBuilder;
import io.spine.users.group.command.CreateGroup;
import io.spine.users.group.command.CreateGroupVBuilder;
import io.spine.users.group.command.DeleteGroup;
import io.spine.users.group.command.DeleteGroupVBuilder;
import io.spine.users.group.command.JoinParentGroup;
import io.spine.users.group.command.LeaveParentGroup;
import io.spine.users.group.command.LeaveParentGroupVBuilder;
import io.spine.users.group.command.MoveGroup;
import io.spine.users.group.command.MoveGroupVBuilder;
import io.spine.users.group.command.RenameGroup;
import io.spine.users.group.command.RenameGroupVBuilder;
import io.spine.users.server.group.GroupPart;

/**
 * Test commands for {@link GroupPart}.
 *
 * @author Vladyslav Lubenskyi
 */
public class GroupTestCommands {

    /**
     * Prevents instantiation.
     */
    private GroupTestCommands() {
    }

    public static CreateGroup createGroup(GroupId id) {
        return CreateGroupVBuilder.newBuilder()
                                  .setId(id)
                                  .setDisplayName(GroupTestEnv.groupName())
                                  .setEmail(GroupTestEnv.groupEmail())
                                  .setOrgEntity(GroupTestEnv.groupOrgEntityOrganization())
                                  .setDescription(GroupTestEnv.groupDescription())
                                  .build();
    }

    public static MoveGroup moveGroup(GroupId groupId, OrganizationOrUnit newParent) {
        return MoveGroupVBuilder.newBuilder()
                                .setId(groupId)
                                .setNewOrgEntity(newParent)
                                .build();
    }

    public static DeleteGroup deleteGroup(GroupId groupId) {
        return DeleteGroupVBuilder.newBuilder()
                                  .setId(groupId)
                                  .build();
    }

    public static JoinParentGroup joinParentGroup(GroupId groupId,
                                                  GroupId upperGroupId) {
        return JoinParentGroup.newBuilder()
                              .setId(groupId)
                              .setParentGroupId(upperGroupId)
                              .build();
    }

    public static LeaveParentGroup leaveParentGroup(GroupId groupId,
                                                    GroupId upperGroupId) {
        return LeaveParentGroupVBuilder.newBuilder()
                                       .setId(groupId)
                                       .setParentGroupId(upperGroupId)
                                       .build();
    }

    public static RenameGroup renameGroup(GroupId id) {
        return RenameGroupVBuilder.newBuilder()
                                  .setId(id)
                                  .setNewName(GroupTestEnv.newGroupName())
                                  .build();
    }

    public static ChangeGroupEmail changeGroupEmail(GroupId id) {
        return ChangeGroupEmailVBuilder.newBuilder()
                                       .setId(id)
                                       .setNewEmail(GroupTestEnv.newGroupEmail())
                                       .build();
    }

    public static ChangeGroupDescription changeGroupDescription(GroupId id) {
        return ChangeGroupDescriptionVBuilder.newBuilder()
                                             .setId(id)
                                             .setDescription(GroupTestEnv.newGroupDescription())
                                             .build();
    }
}
