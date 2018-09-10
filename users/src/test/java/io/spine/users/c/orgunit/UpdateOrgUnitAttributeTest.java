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
import static io.spine.users.c.orgunit.TestOrgUnitFactory.createEmptyAggregate;
import static io.spine.users.c.orgunit.given.OrgUnitTestCommands.updateOrgUnitAttribute;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("UpdateOrgUnitAttribute command should")
class UpdateOrgUnitAttributeTest extends OrgUnitCommandTest<UpdateOrgUnitAttribute> {

    UpdateOrgUnitAttributeTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate OrgUnitAttributeUpdated event")
    void generateEvent() {
        OrgUnitAggregate aggregate = createAggregate(ORG_UNIT_ID);
        expectThat(aggregate).producesEvent(OrgUnitAttributeUpdated.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getName(), event.getName());
            assertEquals(message().getNewValue(), event.getNewValue());
            assertTrue(event.hasOldValue());
        });
    }

    @Test
    @DisplayName("throw rejection if an attribute doesn't exist")
    void generateRejection() {
        OrgUnitAggregate aggregate = createEmptyAggregate(ORG_UNIT_ID);
        expectThat(aggregate).throwsRejection(Rejections.OrgUnitAttributeDoesNotExist.class);
    }

    @Test
    @DisplayName("update an attribute")
    void changeState() {
        OrgUnitAggregate aggregate = createAggregate(ORG_UNIT_ID);
        expectThat(aggregate).hasState(
                state -> {
                    Map<String, Any> attributes = state.getAttributesMap();
                    String name = message().getName();
                    assertTrue(attributes.containsKey(name));
                    Any actualValue = attributes.get(name);
                    assertEquals(message().getNewValue(), actualValue);
                });
    }

    private static UpdateOrgUnitAttribute createMessage() {
        return updateOrgUnitAttribute(ORG_UNIT_ID);
    }
}
