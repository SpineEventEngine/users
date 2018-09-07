/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.user.TestUserFactory.createAggregate;
import static io.spine.users.c.user.given.UserTestCommands.addAuthIdentity;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("AddAuthIdentity command should")
class AddAuthIdentityTest extends UserCommandTest<AddSecondaryAuthIdentity> {

    AddAuthIdentityTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate AuthIdentityAdded event")
    void generateEvent() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).producesEvent(SecondaryAuthIdentityAdded.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getIdentity(), event.getIdentity());
        });
    }

    @Test
    @DisplayName("add a new identity")
    void changeState() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).hasState(
                state -> assertEquals(message().getIdentity(), state.getSecondaryAuthIdentity(1)));
    }

    private static AddSecondaryAuthIdentity createMessage() {
        return addAuthIdentity(USER_ID);
    }
}
