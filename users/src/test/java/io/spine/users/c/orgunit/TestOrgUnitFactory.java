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

package io.spine.users.c.orgunit;

import io.spine.testing.server.entity.given.Given;
import io.spine.users.OrgUnitId;

import static io.spine.users.c.orgunit.given.OrgUnitTestEnv.orgUnitAttributeName;
import static io.spine.users.c.orgunit.given.OrgUnitTestEnv.orgUnitAttributeValue;
import static io.spine.users.c.orgunit.given.OrgUnitTestEnv.orgUnitDomain;
import static io.spine.users.c.orgunit.given.OrgUnitTestEnv.orgUnitName;
import static io.spine.users.c.orgunit.given.OrgUnitTestEnv.orgUnitOwner;
import static io.spine.users.c.orgunit.given.OrgUnitTestEnv.orgUnitParentEntity;

/**
 * A factory for creating test {@linkplain OrgUnitAggregate OrgUnit aggregates}.
 *
 * @author Vladyslav Lubenskyi
 */
public class TestOrgUnitFactory {

    /**
     * Prevents instantiation.
     */
    private TestOrgUnitFactory() {
    }

    /**
     * Creates a new instance of the aggregate with the default state.
     */
    static OrgUnitAggregate createEmptyAggregate(OrgUnitId id) {
        return new OrgUnitAggregate(id);
    }

    /**
     * Creates a new instance of the aggregate with the filled state.
     */
    static OrgUnitAggregate createAggregate(OrgUnitId id) {
        return aggregate(state(id).build());
    }

    private static OrgUnitAggregate aggregate(OrgUnit state) {
        return Given.aggregateOfClass(OrgUnitAggregate.class)
                    .withState(state)
                    .withId(state.getId())
                    .build();
    }

    private static OrgUnitVBuilder state(OrgUnitId id) {
        return OrgUnitVBuilder.newBuilder()
                              .setId(id)
                              .setOwner(orgUnitOwner())
                              .setParentEntity(orgUnitParentEntity())
                              .setDomain(orgUnitDomain())
                              .setDisplayName(orgUnitName())
                              .putAttributes(orgUnitAttributeName(), orgUnitAttributeValue());
    }
}
