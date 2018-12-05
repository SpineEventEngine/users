/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.orgunit;

import io.spine.base.CommandMessage;
import io.spine.server.entity.Repository;
import io.spine.testing.server.aggregate.AggregateCommandTest;
import io.spine.users.OrgUnitId;

import static io.spine.users.orgunit.given.OrgUnitTestEnv.createOrgUnitId;

/**
 * An implementation base for the {@link OrgUnit} aggregate command handler tests.
 *
 * @param <C> the type of the command being tested
 * @author Vladyslav Lubenskyi
 */
abstract class OrgUnitCommandTest<C extends CommandMessage>
        extends AggregateCommandTest<OrgUnitId, C, OrgUnit, OrgUnitAggregate> {

    static final OrgUnitId ORG_UNIT_ID = createOrgUnitId();

    OrgUnitCommandTest(C commandMessage) {
        super(ORG_UNIT_ID, commandMessage);
    }

    @Override
    protected Repository<OrgUnitId, OrgUnitAggregate> createRepository() {
        return new OrgUnitAggregateRepository();
    }
}
