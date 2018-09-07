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

import io.spine.core.UserId;
import io.spine.users.GroupId;
import io.spine.users.ParentEntity;
import io.spine.users.RoleId;
import io.spine.users.c.group.AddGroupAttribute;
import io.spine.users.c.group.AddGroupAttributeVBuilder;
import io.spine.users.c.group.AddSuperGroup;
import io.spine.users.c.group.AddSuperGroupVBuilder;
import io.spine.users.c.group.AssignRoleToGroup;
import io.spine.users.c.group.AssignRoleToGroupVBuilder;
import io.spine.users.c.group.ChangeGroupOwner;
import io.spine.users.c.group.ChangeGroupOwnerVBuilder;
import io.spine.users.c.group.CreateGroup;
import io.spine.users.c.group.CreateGroupVBuilder;
import io.spine.users.c.group.DeleteGroup;
import io.spine.users.c.group.DeleteGroupVBuilder;
import io.spine.users.c.group.GroupAggregate;
import io.spine.users.c.group.MoveGroup;
import io.spine.users.c.group.MoveGroupVBuilder;
import io.spine.users.c.group.RemoveGroupAttribute;
import io.spine.users.c.group.RemoveGroupAttributeVBuilder;
import io.spine.users.c.group.RemoveSuperGroup;
import io.spine.users.c.group.RemoveSuperGroupVBuilder;
import io.spine.users.c.group.UnassignRoleFromGroup;
import io.spine.users.c.group.UnassignRoleFromGroupVBuilder;
import io.spine.users.c.group.UpdateGroupAttribute;
import io.spine.users.c.group.UpdateGroupAttributeVBuilder;

import static io.spine.users.c.group.given.GroupTestEnv.displayName;
import static io.spine.users.c.group.given.GroupTestEnv.groupAttributeName;
import static io.spine.users.c.group.given.GroupTestEnv.groupAttributeValue;
import static io.spine.users.c.group.given.GroupTestEnv.groupEmail;
import static io.spine.users.c.group.given.GroupTestEnv.groupOwner;
import static io.spine.users.c.group.given.GroupTestEnv.groupParentOrganization;
import static io.spine.users.c.group.given.GroupTestEnv.groupRole;
import static io.spine.users.c.group.given.GroupTestEnv.newGroupAttributeName;
import static io.spine.users.c.group.given.GroupTestEnv.newGroupAttributeValue;

/**
 * Test commands for {@link GroupAggregate}.
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
                                  .setDisplayName(displayName())
                                  .setEmail(groupEmail())
                                  .setOwner(groupOwner())
                                  .setParentEntity(groupParentOrganization())
                                  .addRole(groupRole())
                                  .putAttributes(groupAttributeName(), groupAttributeValue())
                                  .build();
    }

    public static ChangeGroupOwner changeOwner(GroupId id, UserId newOwner) {
        return ChangeGroupOwnerVBuilder.newBuilder()
                                       .setId(id)
                                       .setNewOwner(newOwner)
                                       .build();
    }

    public static MoveGroup moveGroup(GroupId groupId, ParentEntity newParent) {
        return MoveGroupVBuilder.newBuilder()
                                .setId(groupId)
                                .setNewParentEntity(newParent)
                                .build();
    }

    public static DeleteGroup deleteGroup(GroupId groupId) {
        return DeleteGroupVBuilder.newBuilder()
                                  .setId(groupId)
                                  .build();
    }

    public static AddSuperGroup addSuperGroup(GroupId groupId,
                                              GroupId upperGroupId) {
        return AddSuperGroupVBuilder.newBuilder()
                                    .setId(groupId)
                                    .setSuperGroupId(upperGroupId)
                                    .build();
    }

    public static RemoveSuperGroup removeSuperGroup(GroupId groupId,
                                                    GroupId upperGroupId) {
        return RemoveSuperGroupVBuilder.newBuilder()
                                       .setId(groupId)
                                       .setSuperGroupId(upperGroupId)
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

    public static AddGroupAttribute addGroupAttribute(GroupId groupId) {
        return AddGroupAttributeVBuilder.newBuilder()
                                        .setId(groupId)
                                        .setName(newGroupAttributeName())
                                        .setValue(newGroupAttributeValue())
                                        .build();
    }

    public static RemoveGroupAttribute removeGroupAttribute(GroupId groupId) {
        return RemoveGroupAttributeVBuilder.newBuilder()
                                           .setId(groupId)
                                           .setName(groupAttributeName())
                                           .build();
    }

    public static UpdateGroupAttribute updateGroupAttribute(GroupId groupId) {
        return UpdateGroupAttributeVBuilder.newBuilder()
                                           .setId(groupId)
                                           .setName(groupAttributeName())
                                           .setNewValue(newGroupAttributeValue())
                                           .build();
    }
}
