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

package io.spine.users.server.user;

import io.spine.core.UserId;
import io.spine.users.user.User;
import io.spine.users.user.command.UnassignRoleFromUser;
import io.spine.users.user.event.RoleUnassignedFromUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.user.given.UserTestCommands.unassignRoleFromUser;

@DisplayName("`UnassignRoleFromUser` command should")
class UnassignRoleFromUserTest
        extends UserPartCommandTest<UnassignRoleFromUser, RoleUnassignedFromUser> {

    @Override
    @Test
    @DisplayName("generate `RoleUnassignedFromUser` and unassign the role from the user")
    protected void produceEventAndChangeState() {
        createPartWithState();
        super.produceEventAndChangeState();
    }

    @Override
    protected UnassignRoleFromUser command(UserId id) {
        return unassignRoleFromUser(id);
    }

    @Override
    protected RoleUnassignedFromUser expectedEventAfter(UnassignRoleFromUser command) {
        return RoleUnassignedFromUser
                .newBuilder()
                .setId(command.getId())
                .setRoleId(command.getRoleId())
                .buildPartial();
    }

    @Override
    protected User expectedStateAfter(UnassignRoleFromUser command) {
        return User
                .newBuilder()
                .setId(command.getId())
                .buildPartial();
    }
}
