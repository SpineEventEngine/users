/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.organization;

import com.google.protobuf.Message;
import io.spine.server.entity.Repository;
import io.spine.testing.server.aggregate.AggregateCommandTest;
import io.spine.users.OrganizationId;
import io.spine.users.c.group.Group;

import static io.spine.users.c.organization.given.OrganizationTestEnv.newOrganizationId;

/**
 * An implementation base for the {@link Group} aggregate command handler tests.
 *
 * @param <C>
 *         the type of the command being tested
 * @author Vladyslav Lubenskyi
 */
abstract class OrgCommandTest<C extends Message>
        extends AggregateCommandTest<OrganizationId, C, Organization, OrganizationAggregate> {

    static final OrganizationId ORG_ID = newOrganizationId();

    OrgCommandTest(C commandMessage) {
        super(ORG_ID, commandMessage);
    }

    @Override
    protected Repository<OrganizationId, OrganizationAggregate> createEntityRepository() {
        return new OrganizationAggregateRepository();
    }
}
