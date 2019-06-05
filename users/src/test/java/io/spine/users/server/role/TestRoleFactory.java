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

import io.spine.testing.server.entity.given.Given;
import io.spine.users.RoleId;
import io.spine.users.role.Role;
import io.spine.users.server.role.given.RoleTestEnv;

/**
 * A factory for creating test {@linkplain RoleAggregate Role aggregates}.
 *
 * @author Vladyslav Lubenskyi
 */
final class TestRoleFactory {

    /**
     * Prevents instantiation.
     */
    private TestRoleFactory() {
    }

    /**
     * Creates a new instance of the aggregate with the default state.
     */
    static RoleAggregate createEmptyAggregate(RoleId id) {
        return new RoleAggregate(id);
    }

    /**
     * Creates a new instance of the aggregate with the filled state.
     */
    static RoleAggregate createAggregate(RoleId id) {
        return aggregate(state(id).build());
    }

    private static RoleAggregate aggregate(Role state) {
        return Given.aggregateOfClass(RoleAggregate.class)
                    .withState(state)
                    .withId(state.getId())
                    .build();
    }

    private static Role.Builder state(RoleId id) {
        return Role
                .newBuilder()
                .setId(id)
                .setDisplayName(RoleTestEnv.roleName())
                .setOrgEntity(RoleTestEnv.roleParent());
    }
}
