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
import io.spine.users.GroupId;
import io.spine.users.OrganizationId;
import io.spine.users.OrganizationOrUnit;
import io.spine.roles.RoleId;
import io.spine.users.group.command.CreateGroup;
import io.spine.roles.command.CreateRole;
import io.spine.users.server.group.given.GroupTestEnv;
import io.spine.users.user.RoleInGroup;
import io.spine.users.user.User;
import io.spine.users.user.UserNature;
import io.spine.users.user.command.CreateUser;
import io.spine.users.user.command.JoinGroup;
import io.spine.users.user.command.LeaveGroup;

import static io.spine.users.server.given.TestIdentifiers.orgId;

public class UserRolesProjectionTestCommands {

    /** Prevents instantiation of this utility class. */
    private UserRolesProjectionTestCommands() {
    }

    public static CreateUser createUser(UserId userId) {
        OrganizationId organizationId = orgId("organization of " + userId.getValue());
        OrganizationOrUnit orgEntity = OrganizationOrUnit
                .newBuilder()
                .setOrganization(organizationId)
                .build();
        return CreateUser
                .newBuilder()
                .setId(userId)
                .setNature(UserNature.UNAVAILABLE)
                .setDisplayName("display name of " + userId.getValue())
                .setOrgEntity(orgEntity)
                .setStatus(User.Status.ACTIVE)
                .build();
    }

    public static CreateRole createRole(RoleId id) {
        return CreateRole
                .newBuilder()
                .setId(id)
                .build();
    }

    public static CreateGroup createGroup(GroupId groupId) {
        return CreateGroup
                .newBuilder()
                .setId(groupId)
                .setDisplayName("group " + groupId.getValue())
                .setEmail(GroupTestEnv.groupEmail())
                .setOrgEntity(GroupTestEnv.groupOrgEntityOrganization())
                .setDescription(GroupTestEnv.groupDescription())
                .build();
    }

    public static JoinGroup joinGroup(UserId user, GroupId groupId) {
        return JoinGroup
                .newBuilder()
                .setId(user)
                .setGroupId(groupId)
                .setRole(RoleInGroup.MEMBER)
                .build();
    }

    public static LeaveGroup leaveGroup(UserId userId, GroupId group) {
        return LeaveGroup
                .newBuilder()
                .setId(userId)
                .setGroupId(group)
                .build();
    }
}
