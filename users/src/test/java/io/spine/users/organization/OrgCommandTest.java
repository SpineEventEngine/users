/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.organization;

import io.spine.base.CommandMessage;
import io.spine.server.entity.Repository;
import io.spine.testing.server.aggregate.AggregateCommandTest;
import io.spine.users.OrganizationId;

import static io.spine.users.organization.given.OrganizationTestEnv.createOrganizationId;

/**
 * An implementation base for the {@link Organization} aggregate command handler tests.
 *
 * @param <C> the type of the command being tested
 * @author Vladyslav Lubenskyi
 */
abstract class OrgCommandTest<C extends CommandMessage>
        extends AggregateCommandTest<OrganizationId, C, Organization, OrganizationAggregate> {

    static final OrganizationId ORG_ID = createOrganizationId();

    OrgCommandTest(C commandMessage) {
        super(ORG_ID, commandMessage);
    }

    @Override
    protected Repository<OrganizationId, OrganizationAggregate> createRepository() {
        return new OrganizationAggregateRepository();
    }
}
