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

package io.spine.users.server.user.given;

import io.spine.core.UserId;
import io.spine.testing.core.given.GivenUserId;
import io.spine.users.RoleId;
import io.spine.users.user.UserRoles;
import io.spine.users.user.UserRolesVBuilder;

import static io.spine.base.Identifier.newUuid;
import static io.spine.users.server.given.GivenId.organizationUuid;
import static io.spine.users.server.role.RoleIds.roleId;

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

    public static UserRoles userWithRole(UserId userId, RoleId roleId) {
        return UserRolesVBuilder.newBuilder()
                                .setId(userId)
                                .addRole(roleId)
                                .build();
    }

    public static String roleNameUuid() {
        return "Role-" + newUuid();
    }

    public static RoleId roleUuid() {
        return roleId(organizationUuid(), roleNameUuid());
    }
}