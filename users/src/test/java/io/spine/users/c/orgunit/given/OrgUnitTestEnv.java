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

package io.spine.users.c.orgunit.given;

import com.google.protobuf.Any;
import com.google.protobuf.Int64Value;
import io.spine.core.UserId;
import io.spine.core.UserIdVBuilder;
import io.spine.net.InternetDomain;
import io.spine.net.InternetDomainVBuilder;
import io.spine.users.OrgUnitId;
import io.spine.users.OrgUnitIdVBuilder;
import io.spine.users.OrganizationalEntity;
import io.spine.users.OrganizationalEntityVBuilder;
import io.spine.users.c.orgunit.OrgUnitAggregate;

import static io.spine.base.Identifier.newUuid;
import static io.spine.protobuf.AnyPacker.pack;

/**
 * The environment for the {@link OrgUnitAggregate} tests.
 *
 * @author Vladyslav Lubenskyi
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

    public static UserId orgUnitOwner() {
        return UserIdVBuilder.newBuilder()
                             .setValue(newUuid())
                             .build();
    }

    public static UserId orgUnitNewOwner() {
        return UserIdVBuilder.newBuilder()
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

    public static String orgUnitAttributeName() {
        return "org-attribute-name-1";
    }

    public static Any orgUnitAttributeValue() {
        return pack(Int64Value.of(271));
    }

    public static OrganizationalEntity orgUnitParentEntity() {
        return OrganizationalEntityVBuilder.newBuilder()
                                           .setOrgUnit(organization())
                                           .build();
    }

    public static OrganizationalEntity newOrgUnitParentEntity() {
        return OrganizationalEntityVBuilder.newBuilder()
                                           .setOrgUnit(createOrgUnitId())
                                           .build();
    }

    public static String newOrgUnitAttributeName() {
        return "org-attribute-name-2";
    }

    public static Any newOrgUnitAttributeValue() {
        return pack(Int64Value.of(271));
    }

    private static OrgUnitId organization() {
        return OrgUnitIdVBuilder.newBuilder()
                                     .setValue(newUuid())
                                     .build();
    }
}
