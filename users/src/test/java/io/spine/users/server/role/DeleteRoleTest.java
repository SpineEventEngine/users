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

package io.spine.users.server.role;

import io.spine.client.Query;
import io.spine.testing.client.TestActorRequestFactory;
import io.spine.testing.server.blackbox.SingleTenantBlackBoxContext;
import io.spine.users.RoleId;
import io.spine.users.role.Role;
import io.spine.users.role.command.CreateRole;
import io.spine.users.role.command.DeleteRole;
import io.spine.users.role.event.RoleDeleted;
import io.spine.users.server.UsersContextTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.client.Filters.all;
import static io.spine.client.Filters.eq;
import static io.spine.users.server.role.given.RoleTestCommands.createRole;
import static io.spine.users.server.role.given.RoleTestCommands.deleteRole;
import static io.spine.users.server.role.given.RoleTestEnv.createRoleId;

@DisplayName("`DeleteRole` command should")
class DeleteRoleTest extends UsersContextTest {

    @Test
    @DisplayName("produce `RoleDeleted` event and delete the role")
    void produceEventAndChangeState() {
        RoleId id = createRoleId();
        CreateRole createCmd = createRole(id);
        DeleteRole deleteCmd = deleteRole(id);
        SingleTenantBlackBoxContext afterCommand = context().receivesCommands(createCmd, deleteCmd);
        RoleDeleted expectedEvent = expectedEvent(deleteCmd);
        afterCommand.assertEvents()
                    .message(1)
                    .comparingExpectedFieldsOnly()
                    .isEqualTo(expectedEvent);
        Query findDeleted = findDeleted(id);
        afterCommand
                .assertQueryResult(findDeleted)
                .containsSingleEntityStateThat()
                .comparingExpectedFieldsOnly()
                .isEqualTo(expectedState(deleteCmd));
    }

    private static Query findDeleted(RoleId id) {
        TestActorRequestFactory factory = new TestActorRequestFactory(DeleteRoleTest.class);
        return factory
                .query()
                .select(Role.class)
                .where(all(eq("id", id), eq("deleted", true)))
                .build();
    }

    private static RoleDeleted expectedEvent(DeleteRole command) {
        return RoleDeleted
                .newBuilder()
                .setId(command.getId())
                .build();
    }

    private static Role expectedState(DeleteRole command) {
        return Role
                .newBuilder()
                .setId(command.getId())
                .build();
    }
}
