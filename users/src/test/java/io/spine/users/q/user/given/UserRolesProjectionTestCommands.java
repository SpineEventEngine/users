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

package io.spine.users.q.user.given;

import io.spine.core.UserId;
import io.spine.users.GroupId;
import io.spine.users.OrganizationId;
import io.spine.users.OrganizationOrUnit;
import io.spine.users.OrganizationOrUnitVBuilder;
import io.spine.users.RoleId;
import io.spine.users.c.group.CreateGroup;
import io.spine.users.c.group.CreateGroupVBuilder;
import io.spine.users.c.role.CreateRole;
import io.spine.users.c.role.CreateRoleVBuilder;
import io.spine.users.c.user.CreateUser;
import io.spine.users.c.user.CreateUserVBuilder;
import io.spine.users.c.user.JoinGroup;
import io.spine.users.c.user.JoinGroupVBuilder;
import io.spine.users.c.user.LeaveGroup;
import io.spine.users.c.user.LeaveGroupVBuilder;
import io.spine.users.c.user.RoleInGroup;
import io.spine.users.c.user.User;
import io.spine.users.c.user.UserNature;

import static io.spine.users.given.GivenId.organizationId;

public class UserRolesProjectionTestCommands {

    /** Prevents instantiation of this utility class. */
    private UserRolesProjectionTestCommands() {
    }

    public static CreateUser createUser(UserId userId) {
        OrganizationId organizationId = organizationId("organization of " + userId.getValue());
        OrganizationOrUnit orgEntity = OrganizationOrUnitVBuilder.newBuilder()
                                                                 .setOrganization(organizationId)
                                                                 .build();
        return CreateUserVBuilder.newBuilder()
                                 .setId(userId)
                                 .setNature(UserNature.UNAVAILABLE)
                                 .setDisplayName("display name of " + userId.getValue())
                                 .setOrgEntity(orgEntity)
                                 .setStatus(User.Status.ACTIVE)
                                 .build();
    }

    public static CreateRole createRole(RoleId id, String name) {
        return CreateRoleVBuilder.newBuilder()
                                 .setId(id)
                                 .setDisplayName(name)
                                 .build();
    }

    public static CreateGroup createGroup(GroupId groupId) {
        return CreateGroupVBuilder.newBuilder()
                                  .setId(groupId)
                                  .setDisplayName("group " + groupId.getValue())
                                  .build();
    }

    public static JoinGroup joinGroup(UserId user, GroupId groupId) {
        return JoinGroupVBuilder.newBuilder()
                                .setId(user)
                                .setGroupId(groupId)
                                .setRole(RoleInGroup.MEMBER)
                                .build();
    }

    public static LeaveGroup leaveGroup(UserId userId, GroupId group) {
        return LeaveGroupVBuilder.newBuilder()
                                 .setId(userId)
                                 .setGroupId(group)
                                 .build();
    }
}
