/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.group;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.group.TestGroupFactory.createAggregate;
import static io.spine.users.c.group.TestGroupFactory.createEmptyAggregate;
import static io.spine.users.c.group.given.GroupTestCommands.removeGroupAttribute;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("RemoveGroupAttribute command should")
class RemoveGroupAttributeTest extends GroupCommandTest<RemoveGroupAttribute> {

    RemoveGroupAttributeTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate GroupAttributeRemoved event")
    void generateEvent() {
        GroupAggregate aggregate = createAggregate(GROUP_ID);
        expectThat(aggregate).producesEvent(GroupAttributeRemoved.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getName(), event.getName());
            assertTrue(event.hasValue());
        });
    }

    @Test
    @DisplayName("remove an attribute")
    void changeState() {
        GroupAggregate aggregate = createAggregate(GROUP_ID);
        expectThat(aggregate).hasState(state -> {
            assertTrue(state.getAttributesMap()
                            .isEmpty());
        });
    }

    @Test
    @DisplayName("throw rejection if an attribute doesn't exist")
    void generateRejection() {
        GroupAggregate aggregate = createEmptyAggregate(GROUP_ID);
        expectThat(aggregate).throwsRejection(Rejections.GroupAttributeDoesNotExist.class);
    }

    private static RemoveGroupAttribute createMessage() {
        return removeGroupAttribute(GROUP_ID);
    }
}
