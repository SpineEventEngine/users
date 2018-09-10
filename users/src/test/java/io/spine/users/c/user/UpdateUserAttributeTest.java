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

import static io.spine.users.c.user.TestUserFactory.createAggregate;
import static io.spine.users.c.user.TestUserFactory.createEmptyAggregate;
import static io.spine.users.c.user.given.UserTestCommands.updateUserAttribute;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("UpdateUserAttribute command should")
class UpdateUserAttributeTest extends UserCommandTest<UpdateUserAttribute> {

    UpdateUserAttributeTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate UserAttributeUpdated event")
    void generateEvent() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).producesEvent(UserAttributeUpdated.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getName(), event.getName());
            assertEquals(message().getNewValue(), event.getNewValue());
            assertTrue(event.hasOldValue());
        });
    }

    @Test
    @DisplayName("throw rejection if an attribute doesn't exist")
    void generateRejection() {
        UserAggregate aggregate = createEmptyAggregate();
        expectThat(aggregate).throwsRejection(Rejections.UserAttributeDoesNotExist.class);
    }

    @Test
    @DisplayName("update an attribute")
    void changeState() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).hasState(
                state -> {
                    Map<String, Any> attributes = state.getAttributesMap();
                    String name = message().getName();
                    assertTrue(attributes.containsKey(name));
                    Any actualValue = attributes.get(name);
                    assertEquals(message().getNewValue(), actualValue);
                });
    }

    private static UpdateUserAttribute createMessage() {
        return updateUserAttribute(USER_ID);
    }
}
