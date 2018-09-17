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

package io.spine.users.c.role.given;

import io.spine.users.OrgUnitId;
import io.spine.users.OrgUnitIdVBuilder;
import io.spine.users.OrganizationId;
import io.spine.users.OrganizationIdVBuilder;
import io.spine.users.OrganizationalEntity;
import io.spine.users.OrganizationalEntityVBuilder;
import io.spine.users.RoleId;
import io.spine.users.RoleIdVBuilder;
import io.spine.users.c.role.RoleAggregate;

import static io.spine.base.Identifier.newUuid;

/**
 * The environment for the {@link RoleAggregate} tests.
 *
 * @author Vladyslav Lubenskyi
 */
public final class RoleTestEnv {

    /**
     * Prevents instantiation.
     */
    private RoleTestEnv() {
    }

    public static RoleId createRoleId() {
        return RoleIdVBuilder.newBuilder()
                             .setValue(newUuid())
                             .build();
    }

    public static String roleName() {
        return "github-contributor";
    }

    public static String newRoleName() {
        return "bitbucket-contributor";
    }

    public static OrganizationalEntity roleParent() {
        return OrganizationalEntityVBuilder.newBuilder()
                                   .setOrganization(organization())
                                   .build();
    }

    public static OrganizationalEntity newRoleParent() {
        return OrganizationalEntityVBuilder.newBuilder()
                                   .setOrgUnit(orgUnit())
                                   .build();
    }

    private static OrganizationId organization() {
        return OrganizationIdVBuilder.newBuilder()
                                     .setValue(newUuid())
                                     .build();
    }

    private static OrgUnitId orgUnit() {
        return OrgUnitIdVBuilder.newBuilder()
                                .setValue(newUuid())
                                .build();
    }
}
