/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.orgunit;

import com.google.protobuf.Message;
import io.spine.server.entity.Repository;
import io.spine.testing.server.aggregate.AggregateCommandTest;
import io.spine.users.OrgUnitId;

import static io.spine.users.c.orgunit.given.OrgUnitTestEnv.newOrgUnitId;

/**
 * An implementation base for the {@link OrgUnit} aggregate command handler tests.
 *
 * @param <C> the type of the command being tested
 * @author Vladyslav Lubenskyi
 */
abstract class OrgUnitCommandTest<C extends Message>
        extends AggregateCommandTest<OrgUnitId, C, OrgUnit, OrgUnitAggregate> {

    static final OrgUnitId ORG_UNIT_ID = newOrgUnitId();

    OrgUnitCommandTest(C commandMessage) {
        super(ORG_UNIT_ID, commandMessage);
    }

    @Override
    protected Repository<OrgUnitId, OrgUnitAggregate> createEntityRepository() {
        return new OrgUnitAggregateRepository();
    }
}
