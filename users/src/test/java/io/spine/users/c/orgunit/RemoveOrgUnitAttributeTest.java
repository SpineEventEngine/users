/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.orgunit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.orgunit.TestOrgUnitFactory.createAggregate;
import static io.spine.users.c.orgunit.given.OrgUnitTestCommands.removeOrgUnitAttribute;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("RemoveOrgUnitAttribute command should")
class RemoveOrgUnitAttributeTest extends OrgUnitCommandTest<RemoveOrgUnitAttribute> {

    RemoveOrgUnitAttributeTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate OrgUnitAttributeRemoved event")
    void generateEvent() {
        OrgUnitAggregate aggregate = createAggregate(ORG_UNIT_ID);
        expectThat(aggregate).producesEvent(OrgUnitAttributeRemoved.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getName(), event.getName());
            assertTrue(event.hasValue());
        });
    }

    @Test
    @DisplayName("remove an attribute")
    void changeState() {
        OrgUnitAggregate aggregate = createAggregate(ORG_UNIT_ID);
        expectThat(aggregate).hasState(state -> {
            assertTrue(state.getAttributesMap()
                            .isEmpty());
        });
    }

    private static RemoveOrgUnitAttribute createMessage() {
        return removeOrgUnitAttribute(ORG_UNIT_ID);
    }
}
