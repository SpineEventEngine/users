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

package io.spine.roles.server;

import io.spine.roles.Role;
import io.spine.roles.RoleId;
import io.spine.roles.event.RoleCreated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.roles.server.given.GivenCommand.createRole;
import static io.spine.roles.server.given.GivenCommand.deleteRole;
import static io.spine.testing.TestValues.randomString;

@DisplayName("A role should")
class RoleTest extends RolesContextTest {

    private RoleId role;
    private String displayName;

    @BeforeEach
    void setupRole() {
        role = RoleId.generate();
        displayName = randomString();
        rolesContext().receivesCommand(createRole(role, displayName));
    }

    @Test
    @DisplayName("be created on a command")
    void created() {
        assertEvent(
                RoleCreated
                        .newBuilder()
                        .setRole(role)
                        .setDisplayName(displayName)
                        .build()
        );

        Role expected = Role
                .newBuilder()
                .setId(role)
                .setDisplayName(displayName)
                .build();
        rolesContext()
                .assertEntityWithState(Role.class, role)
                .hasStateThat()
                .comparingExpectedFieldsOnly()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("be deleted on a command")
    void deleted() {
        rolesContext().receivesCommand(deleteRole(role));

        rolesContext()
                .assertEntityWithState(Role.class, role)
                .deletedFlag()
                .isTrue();
    }
}
