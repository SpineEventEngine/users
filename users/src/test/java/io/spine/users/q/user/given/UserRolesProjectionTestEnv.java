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
import io.spine.testing.core.given.GivenUserId;
import io.spine.users.GroupId;
import io.spine.users.RoleId;
import io.spine.users.RoleName;
import io.spine.users.RoleNameVBuilder;
import io.spine.users.q.user.GroupRoles;
import io.spine.users.q.user.GroupRolesVBuilder;
import io.spine.users.q.user.UserRoles;
import io.spine.users.q.user.UserRolesVBuilder;

import static io.spine.base.Identifier.newUuid;
import static io.spine.users.q.group.given.GroupViewTestEnv.role;

public class UserRolesProjectionTestEnv {

    /** Prevents instantiation of this utility class. */
    private UserRolesProjectionTestEnv() {
    }

    public static UserId userUuid() {
        return GivenUserId.newUuid();
    }

    public static UserRoles userWithoutRoles(UserId user) {
        return UserRolesVBuilder.newBuilder()
                                .setId(user)
                                .build();
    }

    public static UserRoles userWithRole(UserId userId, RoleId roleId, String roleName) {
        return UserRolesVBuilder.newBuilder()
                                .setId(userId)
                                .addRole(roleName(roleId, roleName))
                                .build();
    }

    public static UserRoles userWithRole(UserId userId, GroupId groupId,
                                         RoleId roleId, String roleName) {
        GroupRoles expectedGroupRoles = GroupRolesVBuilder.newBuilder()
                                                          .setGroup(groupId)
                                                          .addRole(roleName(roleId, roleName))
                                                          .build();
        return UserRolesVBuilder.newBuilder()
                                .setId(userId)
                                .addGroupRole(expectedGroupRoles)
                                .build();
    }

    public static RoleName roleName(RoleId id, String name) {
        return RoleNameVBuilder.newBuilder()
                               .setId(id)
                               .setName(name)
                               .build();
    }

    public static RoleName roleName() {
        return roleName(role(), roleDisplayName());
    }

    public static String roleDisplayName() {
        return "Role-" + newUuid();
    }
}
