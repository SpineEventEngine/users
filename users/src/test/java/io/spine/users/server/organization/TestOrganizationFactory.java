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

package io.spine.users.server.organization;

import io.spine.testing.server.entity.given.Given;
import io.spine.users.OrganizationId;
import io.spine.users.organization.Organization;

import static io.spine.users.server.organization.given.OrganizationTestEnv.orgDomain;
import static io.spine.users.server.organization.given.OrganizationTestEnv.orgName;
import static io.spine.users.server.organization.given.OrganizationTestEnv.orgTenant;

/**
 * A factory for creating test {@linkplain OrganizationAggregate Organization aggregates}.
 *
 * @author Vladyslav Lubenskyi
 */
final class TestOrganizationFactory {

    /**
     * Prevents instantiation.
     */
    private TestOrganizationFactory() {
    }

    /**
     * Creates a new instance of the aggregate with the default state.
     */
    static OrganizationAggregate createEmptyAggregate(OrganizationId id) {
        return new OrganizationAggregate(id);
    }

    /**
     * Creates a new instance of the aggregate with the filled state.
     */
    static OrganizationAggregate createAggregate(OrganizationId id) {
        return aggregate(state(id));
    }

    private static OrganizationAggregate aggregate(Organization state) {
        return Given.aggregateOfClass(OrganizationAggregate.class)
                    .withState(state)
                    .withId(state.getId())
                    .build();
    }

    private static Organization state(OrganizationId id) {
        return Organization
                .vBuilder()
                .setId(id)
                .setTenant(orgTenant())
                .setDomain(orgDomain())
                .setDisplayName(orgName())
                .build();
    }
}
