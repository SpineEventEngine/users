/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.organization;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.organization.TestOrganizationFactory.createAggregate;
import static io.spine.users.c.organization.given.OrganizationTestCommands.removeOrganizationAttribute;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("RemoveOrganizationAttribute command should")
class RemoveOrganizationAttributeTest extends OrgCommandTest<RemoveOrganizationAttribute> {

    RemoveOrganizationAttributeTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate OrganizationAttributeRemoved event")
    void generateEvent() {
        OrganizationAggregate aggregate = createAggregate(ORG_ID);
        expectThat(aggregate).producesEvent(OrganizationAttributeRemoved.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getName(), event.getName());
            assertTrue(event.hasValue());
        });
    }

    @Test
    @DisplayName("remove an attribute")
    void changeState() {
        OrganizationAggregate aggregate = createAggregate(ORG_ID);
        expectThat(aggregate).hasState(state -> {
            assertTrue(state.getAttributesMap()
                            .isEmpty());
        });
    }

    private static RemoveOrganizationAttribute createMessage() {
        return removeOrganizationAttribute(ORG_ID);
    }
}
