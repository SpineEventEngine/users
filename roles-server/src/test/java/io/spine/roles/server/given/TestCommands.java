/*
 * Copyright 2019, TeamDev. All rights reserved.
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
import io.spine.roles.command.AssignRoleToUser;
import io.spine.roles.command.RemoveRoleAssignmentFromUser;
import io.spine.users.GroupId;
import io.spine.roles.RoleId;
import io.spine.roles.command.AssignRoleToGroup;
import io.spine.roles.command.RemoveRoleAssignmentFromGroup;
import io.spine.roles.command.CreateRole;
import io.spine.roles.command.DeleteRole;
import io.spine.roles.server.RoleAggregate;

/**
 * Test commands for {@link RoleAggregate}.
 */
public final class TestCommands {

    /**
     * Prevents instantiation.
     */
    private TestCommands() {
    }

    public static CreateRole createRole(RoleId r) {
        return CreateRole.newBuilder().setRole(r).vBuild();
    }

    public static DeleteRole deleteRole(RoleId r) {
        return DeleteRole.newBuilder().setRole(r).vBuild();
    }

    public static AssignRoleToUser assignRoleToUser(UserId u, RoleId r) {
        return AssignRoleToUser.newBuilder().setUser(u).setRole(r).vBuild();
    }

    public static RemoveRoleAssignmentFromUser removeRoleFromUser(UserId u, RoleId r) {
        return RemoveRoleAssignmentFromUser.newBuilder().setUser(u).setRole(r).vBuild();
    }

    public static AssignRoleToGroup assignRoleToGroup(GroupId g, RoleId r) {
        return AssignRoleToGroup.newBuilder().setGroup(g).setRole(r).vBuild();
    }

    public static RemoveRoleAssignmentFromGroup removeRoleFromGroup(GroupId g, RoleId r) {
        return RemoveRoleAssignmentFromGroup.newBuilder().setGroup(g).setRole(r).vBuild();
    }
}
