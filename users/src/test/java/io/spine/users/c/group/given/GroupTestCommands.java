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

package io.spine.users.c.group.given;

import io.spine.users.GroupId;
import io.spine.users.OrganizationOrUnit;
import io.spine.users.RoleId;
import io.spine.users.c.group.AssignRoleToGroup;
import io.spine.users.c.group.AssignRoleToGroupVBuilder;
import io.spine.users.c.group.ChangeGroupEmail;
import io.spine.users.c.group.ChangeGroupEmailVBuilder;
import io.spine.users.c.group.CreateGroup;
import io.spine.users.c.group.CreateGroupVBuilder;
import io.spine.users.c.group.DeleteGroup;
import io.spine.users.c.group.DeleteGroupVBuilder;
import io.spine.users.c.group.GroupPart;
import io.spine.users.c.group.JoinParentGroup;
import io.spine.users.c.group.LeaveParentGroup;
import io.spine.users.c.group.LeaveParentGroupVBuilder;
import io.spine.users.c.group.MoveGroup;
import io.spine.users.c.group.MoveGroupVBuilder;
import io.spine.users.c.group.RenameGroup;
import io.spine.users.c.group.RenameGroupVBuilder;
import io.spine.users.c.group.UnassignRoleFromGroup;
import io.spine.users.c.group.UnassignRoleFromGroupVBuilder;

import static io.spine.users.c.group.given.GroupTestEnv.groupEmail;
import static io.spine.users.c.group.given.GroupTestEnv.groupName;
import static io.spine.users.c.group.given.GroupTestEnv.groupOrgEntityOrganization;
import static io.spine.users.c.group.given.GroupTestEnv.groupRole;
import static io.spine.users.c.group.given.GroupTestEnv.newGroupEmail;
import static io.spine.users.c.group.given.GroupTestEnv.newGroupName;

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
                                  .setDisplayName(groupName())
                                  .setEmail(groupEmail())
                                  .setOrgEntity(groupOrgEntityOrganization())
                                  .addRole(groupRole())
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

    public static AssignRoleToGroup assignRoleToGroup(GroupId groupId, RoleId roleId) {
        return AssignRoleToGroupVBuilder.newBuilder()
                                        .setId(groupId)
                                        .setRoleId(roleId)
                                        .build();
    }

    public static UnassignRoleFromGroup unassignRoleFromGroup(GroupId groupId, RoleId roleId) {
        return UnassignRoleFromGroupVBuilder.newBuilder()
                                            .setId(groupId)
                                            .setRoleId(roleId)
                                            .build();
    }

    public static RenameGroup renameGroup(GroupId id) {
        return RenameGroupVBuilder.newBuilder()
                                  .setId(id)
                                  .setNewName(newGroupName())
                                  .build();
    }

    public static ChangeGroupEmail changeGroupEmail(GroupId id) {
        return ChangeGroupEmailVBuilder.newBuilder()
                                       .setId(id)
                                       .setNewEmail(newGroupEmail())
                                       .build();
    }
}
