/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.user.given.TestAggregateFactory.createAggregate;
import static io.spine.users.user.given.UserTestCommands.addUserAttribute;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("AddUserAttribute command should")
public class AddUserAttributeCommandShould extends UserCommandTest<AddUserAttribute> {

    @Test
    @DisplayName("generate UserAttributeAdded event")
    void generateEvent() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).producesEvent(UserAttributeAdded.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getAttribute(), event.getAttribute());
        });
    }

    @Test
    @DisplayName("add a new attribute")
    void changeState() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).hasState(
                state -> assertEquals(message().getAttribute(), state.getAttribute(1)));
    }

    @Override
    protected AddUserAttribute createMessage() {
        return addUserAttribute();
    }
}
