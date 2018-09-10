/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.orgunit;

import com.google.protobuf.Any;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.spine.users.c.orgunit.TestOrgUnitFactory.createAggregate;
import static io.spine.users.c.orgunit.given.OrgUnitTestCommands.addOrgUnitAttribute;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings("Duplicates") // The same assertions are made for other aggregates.
@DisplayName("AddOrgUnitAttribute command should")
class AddOrgUnitAttributeTest extends OrgUnitCommandTest<AddOrgUnitAttribute> {

    AddOrgUnitAttributeTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate OrgUnitAttributeAdded event")
    void generateEvent() {
        OrgUnitAggregate aggregate = createAggregate(ORG_UNIT_ID);
        expectThat(aggregate).producesEvent(OrgUnitAttributeAdded.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getName(), event.getName());
            assertEquals(message().getValue(), event.getValue());
        });
    }

    @Test
    @DisplayName("add a new attribute")
    void changeState() {
        OrgUnitAggregate aggregate = createAggregate(ORG_UNIT_ID);
        expectThat(aggregate).hasState(
                state -> {
                    Map<String, Any> actualAttributes = state.getAttributesMap();
                    String expectedName = message().getName();
                    Any expectedValue = message().getValue();
                    assertTrue(actualAttributes.containsKey(expectedName));
                    assertEquals(expectedValue, actualAttributes.get(expectedName));
                });
    }

    private static AddOrgUnitAttribute createMessage() {
        return addOrgUnitAttribute(ORG_UNIT_ID);
    }
}
