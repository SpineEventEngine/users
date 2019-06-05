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

package io.spine.users.server.organization.given;

import io.spine.core.TenantId;
import io.spine.net.EmailAddress;
import io.spine.net.InternetDomain;
import io.spine.users.OrganizationId;
import io.spine.users.server.organization.OrganizationAggregate;

import static io.spine.base.Identifier.newUuid;

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
        return OrganizationId
                .newBuilder()
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
        return InternetDomain
                .newBuilder()
                .setValue("organization.com")
                .vBuild();
    }

    public static InternetDomain newOrgDomain() {
        return InternetDomain
                .newBuilder()
                .setValue("organization-renamed.com")
                .vBuild();
    }

    public static TenantId orgTenant() {
        return TenantId
                .newBuilder()
                .setDomain(orgDomain())
                .setEmail(email())
                .setValue(newUuid())
                .build();
    }

    public static TenantId newOrgTenant() {
        return TenantId
                .newBuilder()
                .setDomain(orgDomain())
                .setEmail(email())
                .setValue(newUuid())
                .build();
    }

    private static EmailAddress email() {
        return EmailAddress
                .newBuilder()
                .setValue("random@email.com")
                .vBuild();
    }
}
