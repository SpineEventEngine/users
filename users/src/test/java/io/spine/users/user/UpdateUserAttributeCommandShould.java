/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.user.given.TestAggregateFactory.createAggregate;
import static io.spine.users.user.given.UserTestCommands.updateUserAttribute;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("UpdateUserAttribute command should")
public class UpdateUserAttributeCommandShould extends UserCommandTest<UpdateUserAttribute> {

    @Test
    @DisplayName("generate UserAttributeUpdated event")
    void generateEvent() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).producesEvent(UserAttributeUpdated.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getAttribute(), event.getNewAttribute());
            assertTrue(event.hasOldAttribute());
        });
    }

    @Test
    @DisplayName("update an attribute")
    void changeState() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).hasState(
                state -> assertEquals(message().getAttribute(), state.getAttribute(0)));
    }

    @Override
    protected UpdateUserAttribute createMessage() {
        return updateUserAttribute();
    }
}
