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
import io.spine.roles.InheritedRoles;
import io.spine.roles.RoleId;
import io.spine.roles.UserRoles;
import io.spine.testing.core.given.GivenUserId;
import io.spine.users.GroupId;

public class Given {

    /** Prevents instantiation of this utility class. */
    private Given() {
    }

    public static UserId userUuid() {
        return GivenUserId.newUuid();
    }

    public static UserRoles userWithoutRoles(UserId user) {
        return UserRoles
                .newBuilder()
                .setUser(user)
                .vBuild();
    }

    public static UserRoles userWithAssignedRole(UserId user, RoleId role) {
        return UserRoles
                .newBuilder()
                .setUser(user)
                .addAssigned(role)
                .vBuild();
    }

    public static UserRoles userWithInheritedRole(UserId user, GroupId group, RoleId role) {
        return UserRoles
                .newBuilder()
                .setUser(user)
                .setInherited(
                        InheritedRoles
                                .newBuilder()
                                .addItem(InheritedRoles.Item
                                                 .newBuilder()
                                                 .setGroup(group)
                                                 .setRole(role)))
                .build();
    }
}
