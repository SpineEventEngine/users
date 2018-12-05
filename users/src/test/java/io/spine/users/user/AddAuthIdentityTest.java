/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user;

import io.spine.users.user.command.AddSecondaryIdentity;
import io.spine.users.user.event.SecondaryIdentityAdded;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.user.given.UserTestCommands.addAuthIdentity;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("AddAuthIdentity command should")
class AddAuthIdentityTest extends UserPartCommandTest<AddSecondaryIdentity> {

    AddAuthIdentityTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate AuthIdentityAdded event")
    void generateEvent() {
        UserPart aggregate = createPartWithState();
        expectThat(aggregate).producesEvent(SecondaryIdentityAdded.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getIdentity(), event.getIdentity());
        });
    }

    @Test
    @DisplayName("add a new identity")
    void changeState() {
        UserPart aggregate = createPartWithState();
        expectThat(aggregate).hasState(
                state -> assertEquals(message().getIdentity(), state.getSecondaryIdentity(1)));
    }

    private static AddSecondaryIdentity createMessage() {
        return addAuthIdentity(USER_ID);
    }
}
