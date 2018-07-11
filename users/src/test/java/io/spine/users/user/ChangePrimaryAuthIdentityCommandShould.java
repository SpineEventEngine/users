/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.user.given.TestAggregateFactory.createAggregate;
import static io.spine.users.user.given.UserTestCommands.changePrimaryIdentity;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("ChangePrimaryAuthIdentity command should")
public class ChangePrimaryAuthIdentityCommandShould extends UserCommandTest<ChangePrimaryAuthIdentity> {

    @Test
    @DisplayName("generate PrimaryAuthIdentityChanged event")
    void generateEvent() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).producesEvent(PrimaryAuthIdentityChanged.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getIdentity(), event.getIdentity());
        });
    }

    @Test
    @DisplayName("change the primary googleIdentity")
    void changeState() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).hasState(
                state -> assertEquals(message().getIdentity(), state.getPrimaryAuthIdentity()));
    }

    @Override
    protected ChangePrimaryAuthIdentity createMessage() {
        return changePrimaryIdentity();
    }

}
