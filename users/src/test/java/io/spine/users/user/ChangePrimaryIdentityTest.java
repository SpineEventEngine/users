/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user;

import io.spine.users.user.command.ChangePrimaryIdentity;
import io.spine.users.user.event.PrimaryIdentityChanged;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.user.given.UserTestCommands.changePrimaryIdentity;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("ChangePrimaryIdentity command should")
class ChangePrimaryIdentityTest extends UserPartCommandTest<ChangePrimaryIdentity> {

    ChangePrimaryIdentityTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate PrimaryIdentityChanged event")
    void generateEvent() {
        UserPart aggregate = createPartWithState();
        expectThat(aggregate).producesEvent(PrimaryIdentityChanged.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getIdentity(), event.getIdentity());
        });
    }

    @Test
    @DisplayName("change the primary googleIdentity")
    void changeState() {
        UserPart aggregate = createPartWithState();
        expectThat(aggregate).hasState(
                state -> assertEquals(message().getIdentity(), state.getPrimaryIdentity()));
    }

    private static ChangePrimaryIdentity createMessage() {
        return changePrimaryIdentity(USER_ID);
    }
}
