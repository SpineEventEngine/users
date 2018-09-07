/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.user.TestUserFactory.createAggregate;
import static io.spine.users.c.user.given.UserTestCommands.removeUserAttribute;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("RemoveUserAttribute command should")
class RemoveUserAttributeTest extends UserCommandTest<RemoveUserAttribute> {

    RemoveUserAttributeTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate UserAttributeRemoved event")
    void generateEvent() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).producesEvent(UserAttributeRemoved.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getName(), event.getName());
            assertTrue(event.hasValue());
        });
    }

    @Test
    @DisplayName("remove an attribute")
    void changeState() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).hasState(state -> {
            assertTrue(state.getAttributesMap()
                            .isEmpty());
        });
    }

    private static RemoveUserAttribute createMessage() {
        return removeUserAttribute(USER_ID);
    }
}
