/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.user.TestUserFactory.createEmptyAggregate;
import static io.spine.users.c.user.given.UserTestCommands.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("CreateUser command should")
class CreateUserTest extends UserCommandTest<CreateUser> {

    CreateUserTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate UserCreated event")
    void produceEvent() {
        UserAggregate emptyAggregate = createEmptyAggregate();
        expectThat(emptyAggregate).producesEvent(UserCreated.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getDisplayName(), event.getDisplayName());
            assertEquals(message().getPrimaryIdentity(), event.getPrimaryIdentity());
            assertEquals(message().getOrgEntity(), event.getOrgEntity());
            assertEquals(message().getStatus(), event.getStatus());
            assertEquals(message().getRole(0), event.getRole(0));
            assertEquals(message().getKind(), event.getKind());
        });
    }

    @Test
    @DisplayName("create the user")
    void changeState() {
        UserAggregate emptyAggregate = createEmptyAggregate();
        expectThat(emptyAggregate).hasState(state -> {
            assertEquals(message().getId(), state.getId());
            assertEquals(message().getDisplayName(), state.getDisplayName());
            assertEquals(message().getPrimaryIdentity(), state.getPrimaryIdentity());
            assertEquals(message().getOrgEntity(), state.getOrgEntity());
            assertEquals(message().getStatus(), state.getStatus());
            assertEquals(message().getRole(0), state.getRole(0));
            assertEquals(message().getKind(), state.getKind());
        });
    }

    private static CreateUser createMessage() {
        return createUser(USER_ID);
    }
}
