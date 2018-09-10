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
import static io.spine.users.c.group.TestGroupFactory.createEmptyAggregate;
import static io.spine.users.c.group.given.GroupTestCommands.updateGroupAttribute;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("UpdateGroupAttribute command should")
class UpdateGroupAttributeTest extends GroupCommandTest<UpdateGroupAttribute> {

    UpdateGroupAttributeTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate GroupAttributeUpdated event")
    void generateEvent() {
        GroupAggregate aggregate = createAggregate(GROUP_ID);
        expectThat(aggregate).producesEvent(GroupAttributeUpdated.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getName(), event.getName());
            assertEquals(message().getNewValue(), event.getNewValue());
            assertTrue(event.hasOldValue());
        });
    }

    @Test
    @DisplayName("throw rejection if an attribute doesn't exist")
    void generateRejection() {
        GroupAggregate aggregate = createEmptyAggregate(GROUP_ID);
        expectThat(aggregate).throwsRejection(Rejections.GroupAttributeDoesNotExist.class);
    }

    @Test
    @DisplayName("update an attribute")
    void changeState() {
        GroupAggregate aggregate = createAggregate(GROUP_ID);
        expectThat(aggregate).hasState(
                state -> {
                    Map<String, Any> attributes = state.getAttributesMap();
                    String name = message().getName();
                    assertTrue(attributes.containsKey(name));
                    Any actualValue = attributes.get(name);
                    assertEquals(message().getNewValue(), actualValue);
                });
    }

    private static UpdateGroupAttribute createMessage() {
        return updateGroupAttribute(GROUP_ID);
    }
}
