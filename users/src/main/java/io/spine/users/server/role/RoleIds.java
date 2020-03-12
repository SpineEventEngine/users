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

import io.spine.users.OrgUnitId;
import io.spine.users.OrganizationId;
import io.spine.users.OrganizationOrUnit;
import io.spine.users.RoleId;

/**
 * Factory methods for producing {@linkplain RoleId role IDs}.
 */
public final class RoleIds {

    /** Prevents instantiation of this utility class. */
    private RoleIds() {
    }

    /**
     * Creates an ID of a role belonging to the organization.
     */
    public static RoleId roleId(OrganizationId organizationId, String name) {
        OrganizationOrUnit orgEntity = OrganizationOrUnit
                .newBuilder()
                .setOrganization(organizationId)
                .vBuild();
        return roleId(orgEntity, name);
    }

    /**
     * Creates an ID of a role belonging to the organizational unit.
     */
    public static RoleId roleId(OrgUnitId orgUnitId, String name) {
        OrganizationOrUnit orgEntity = OrganizationOrUnit
                .newBuilder()
                .setOrgUnit(orgUnitId)
                .vBuild();
        return roleId(orgEntity, name);
    }

    /**
     * Creates an ID of a role belonging to the {@link OrganizationOrUnit}.
     */
    public static RoleId roleId(OrganizationOrUnit orgEntity, String name) {
        return RoleId
                .newBuilder()
                .setOrgEntity(orgEntity)
                .setName(name)
                .vBuild();
    }
}
