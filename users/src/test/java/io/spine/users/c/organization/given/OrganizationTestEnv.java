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

package io.spine.users.c.organization.given;

import com.google.protobuf.Any;
import com.google.protobuf.Int64Value;
import io.spine.core.TenantId;
import io.spine.core.TenantIdVBuilder;
import io.spine.core.UserId;
import io.spine.core.UserIdVBuilder;
import io.spine.net.EmailAddress;
import io.spine.net.EmailAddressVBuilder;
import io.spine.net.InternetDomain;
import io.spine.net.InternetDomainVBuilder;
import io.spine.users.OrganizationId;
import io.spine.users.OrganizationIdVBuilder;
import io.spine.users.c.organization.OrganizationAggregate;

import static io.spine.base.Identifier.newUuid;
import static io.spine.protobuf.AnyPacker.pack;

/**
 * The environment for the {@link OrganizationAggregate} tests.
 *
 * @author Vladyslav Lubenskyi
 */
public final class OrganizationTestEnv {

    /**
     * Prevents instantiation.
     */
    private OrganizationTestEnv() {
    }

    public static OrganizationId createOrganizationId() {
        return OrganizationIdVBuilder.newBuilder()
                                     .setValue(newUuid())
                                     .build();
    }

    public static UserId orgOwner() {
        return UserIdVBuilder.newBuilder()
                             .setValue(newUuid())
                             .build();
    }

    public static UserId newOrgOwner() {
        return UserIdVBuilder.newBuilder()
                             .setValue(newUuid())
                             .build();
    }

    public static String orgName() {
        return "Test Organization 1";
    }

    public static String orgNewName() {
        return "Test Organization 1 - Renamed";
    }

    public static InternetDomain orgDomain() {
        return InternetDomainVBuilder.newBuilder()
                                     .setValue("organization.com")
                                     .build();
    }

    public static InternetDomain newOrgDomain() {
        return InternetDomainVBuilder.newBuilder()
                                     .setValue("organization-renamed.com")
                                     .build();
    }

    public static TenantId orgTenant() {
        return TenantIdVBuilder.newBuilder()
                               .setDomain(orgDomain())
                               .setEmail(email())
                               .setValue(newUuid())
                               .build();
    }

    public static TenantId newOrgTenant() {
        return TenantIdVBuilder.newBuilder()
                               .setDomain(orgDomain())
                               .setEmail(email())
                               .setValue(newUuid())
                               .build();
    }

    public static String orgAttributeName() {
        return "org-attribute-name-1";
    }

    public static Any orgAttributeValue() {
        return pack(Int64Value.of(271));
    }

    public static String newOrgAttributeName() {
        return "org-attribute-name-2";
    }

    public static Any newOrgAttributeValue() {
        return pack(Int64Value.of(271));
    }

    private static EmailAddress email() {
        return EmailAddressVBuilder.newBuilder()
                                   .setValue("random@email.com")
                                   .build();
    }
}
