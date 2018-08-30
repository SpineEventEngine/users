/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import com.google.protobuf.Any;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.spine.users.c.user.given.TestAggregateFactory.createAggregate;
import static io.spine.users.c.user.given.UserTestCommands.addUserAttribute;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("AddUserAttribute command should")
class AddUserAttributeCommandTest extends UserCommandTest<AddUserAttribute> {

    AddUserAttributeCommandTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate UserAttributeAdded event")
    void generateEvent() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).producesEvent(UserAttributeAdded.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getName(), event.getName());
            assertEquals(message().getValue(), event.getValue());
        });
    }

    @Test
    @DisplayName("add a new attribute")
    void changeState() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).hasState(
                state -> {
                    Map<String, Any> actualAttributes = state.getAttributesMap();
                    String expectedName = message().getName();
                    Any expectedValue = message().getValue();
                    assertTrue(actualAttributes.containsKey(expectedName));
                    assertEquals(expectedValue, actualAttributes.get(expectedName));
                });
    }

    private static AddUserAttribute createMessage() {
        return addUserAttribute(USER_ID);
    }
}
