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

package io.spine.users.role.given;

import io.spine.users.OrganizationOrUnit;
import io.spine.users.OrganizationOrUnitVBuilder;
import io.spine.users.RoleId;
import io.spine.users.role.RoleAggregate;

import static io.spine.base.Identifier.newUuid;
import static io.spine.users.given.GivenId.orgUnitUuid;
import static io.spine.users.given.GivenId.organizationUuid;
import static io.spine.users.role.RoleIds.roleId;

/**
 * The environment for the {@link RoleAggregate} tests.
 */
public final class RoleTestEnv {

    /**
     * Prevents instantiation.
     */
    private RoleTestEnv() {
    }

    public static RoleId createRoleId() {
        return roleId(roleParent(), newUuid());
    }

    public static String roleName() {
        return "github-contributor";
    }

    public static OrganizationOrUnit roleParent() {
        return OrganizationOrUnitVBuilder.newBuilder()
                                         .setOrganization(organizationUuid())
                                         .build();
    }

    public static OrganizationOrUnit newRoleParent() {
        return OrganizationOrUnitVBuilder.newBuilder()
                                         .setOrgUnit(orgUnitUuid())
                                         .build();
    }
}
