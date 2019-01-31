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

package io.spine.users.server.orgunit.given;

import io.spine.net.InternetDomain;
import io.spine.net.InternetDomainVBuilder;
import io.spine.users.OrgUnitId;
import io.spine.users.OrgUnitIdVBuilder;
import io.spine.users.OrganizationOrUnit;
import io.spine.users.OrganizationOrUnitVBuilder;

import static io.spine.base.Identifier.newUuid;

/**
 * The environment for the {@link io.spine.users.server.orgunit.OrgUnitAggregate} tests.
 */
public final class OrgUnitTestEnv {

    /**
     * Prevents instantiation.
     */
    private OrgUnitTestEnv() {
    }

    public static OrgUnitId createOrgUnitId() {
        return OrgUnitId.newBuilder()
                        .setValue(newUuid())
                        .build();
    }

    public static String orgUnitName() {
        return "Test OrgUnit 1";
    }

    public static String orgUnitNewName() {
        return "Test OrgUnit 1 Renamed";
    }

    public static InternetDomain orgUnitDomain() {
        return InternetDomainVBuilder.newBuilder()
                                     .setValue("unit.organization.com")
                                     .build();
    }

    public static InternetDomain orgUnitNewDomain() {
        return InternetDomainVBuilder.newBuilder()
                                     .setValue("unit-renamed.organization.com")
                                     .build();
    }

    public static OrganizationOrUnit orgUnitParentEntity() {
        return OrganizationOrUnitVBuilder.newBuilder()
                                         .setOrgUnit(organization())
                                         .build();
    }

    public static OrganizationOrUnit newOrgUnitParentEntity() {
        return OrganizationOrUnitVBuilder.newBuilder()
                                         .setOrgUnit(createOrgUnitId())
                                         .build();
    }

    private static OrgUnitId organization() {
        return OrgUnitIdVBuilder.newBuilder()
                                .setValue(newUuid())
                                .build();
    }
}
