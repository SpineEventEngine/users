/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.user.given.TestAggregateFactory.createEmptyAggregate;
import static io.spine.users.user.given.UserTestCommands.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("CreateUser command should")
public class CreateUserCommandTest extends UserCommandTest<CreateUser> {

    protected CreateUserCommandTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate UserCreated event")
    void generateEvent() {
        UserAggregate emptyAggregate = createEmptyAggregate();
        expectThat(emptyAggregate).producesEvent(UserCreated.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getDisplayName(), event.getDisplayName());
            assertEquals(message().getPrimaryIdentity(), event.getPrimaryIdentity());
            assertEquals(message().getParentEntity(), event.getParentEntity());
            assertEquals(message().getStatus(), event.getStatus());
            assertEquals(message().getRole(0), event.getRole(0));
            assertEquals(message().getAttribute(0), event.getAttribute(0));
            assertTrue(event.hasWhenCreated());
        });
    }

    @Test
    @DisplayName("create the user")
    void changeState() {
        UserAggregate emptyAggregate = createEmptyAggregate();
        expectThat(emptyAggregate).hasState(state -> {
            assertEquals(message().getId(), state.getId());
            assertEquals(message().getDisplayName(), state.getDisplayName());
            assertEquals(message().getPrimaryIdentity(), state.getPrimaryAuthIdentity());
            assertEquals(message().getParentEntity(), state.getParentEntity());
            assertEquals(message().getStatus(), state.getStatus());
            assertEquals(message().getRole(0), state.getRole(0));
            assertEquals(message().getAttribute(0), state.getAttribute(0));
        });
    }

    private static CreateUser createMessage() {
        return createUser(USER_ID);
    }
}
