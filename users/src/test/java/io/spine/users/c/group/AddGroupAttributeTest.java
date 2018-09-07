/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.group;

import com.google.protobuf.Any;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.spine.users.c.group.TestGroupFactory.createAggregate;
import static io.spine.users.c.group.given.GroupTestCommands.addGroupAttribute;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings("Duplicates") // The same assertions are made for other aggregates.
@DisplayName("AddGroupAttribute command should")
class AddGroupAttributeTest extends GroupCommandTest<AddGroupAttribute> {

    AddGroupAttributeTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate GroupAttributeAdded event")
    void generateEvent() {
        GroupAggregate aggregate = createAggregate();
        expectThat(aggregate).producesEvent(GroupAttributeAdded.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getName(), event.getName());
            assertEquals(message().getValue(), event.getValue());
        });
    }

    @Test
    @DisplayName("add a new attribute")
    void changeState() {
        GroupAggregate aggregate = createAggregate();
        expectThat(aggregate).hasState(
                state -> {
                    Map<String, Any> actualAttributes = state.getAttributesMap();
                    String expectedName = message().getName();
                    Any expectedValue = message().getValue();
                    assertTrue(actualAttributes.containsKey(expectedName));
                    assertEquals(expectedValue, actualAttributes.get(expectedName));
                });
    }

    private static AddGroupAttribute createMessage() {
        return addGroupAttribute(GROUP_ID);
    }
}
