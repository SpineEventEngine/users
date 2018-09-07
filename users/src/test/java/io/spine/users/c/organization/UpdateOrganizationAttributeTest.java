/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.organization;

import com.google.protobuf.Any;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.spine.users.c.organization.TestOrganizationFactory.createAggregate;
import static io.spine.users.c.organization.given.OrganizationTestCommands.updateOrganizationAttribute;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("UpdateOrganizationAttribute command should")
class UpdateOrganizationAttributeTest extends OrgCommandTest<UpdateOrganizationAttribute> {

    UpdateOrganizationAttributeTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate OrganizationAttributeUpdated event")
    void generateEvent() {
        OrganizationAggregate aggregate = createAggregate(ORG_ID);
        expectThat(aggregate).producesEvent(OrganizationAttributeUpdated.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getName(), event.getName());
            assertEquals(message().getNewValue(), event.getNewValue());
            assertTrue(event.hasOldValue());
        });
    }

    @Test
    @DisplayName("update an attribute")
    void changeState() {
        OrganizationAggregate aggregate = createAggregate(ORG_ID);
        expectThat(aggregate).hasState(
                state -> {
                    Map<String, Any> attributes = state.getAttributesMap();
                    String name = message().getName();
                    assertTrue(attributes.containsKey(name));
                    Any actualValue = attributes.get(name);
                    assertEquals(message().getNewValue(), actualValue);
                });
    }

    private static UpdateOrganizationAttribute createMessage() {
        return updateOrganizationAttribute(ORG_ID);
    }
}
