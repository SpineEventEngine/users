/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import io.spine.users.UserAuthIdentity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.user.given.TestAggregateFactory.createAggregate;
import static io.spine.users.c.user.given.UserTestCommands.removeAuthIdentity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("RemoveAuthIdentity command should")
class RemoveAuthIdentityCommandTest extends UserCommandTest<RemoveSecondaryAuthIdentity> {

    RemoveAuthIdentityCommandTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate AuthIdentityRemoved event")
    void generateEvent() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).producesEvent(SecondaryAuthIdentityRemoved.class, event -> {
            assertEquals(message().getId(), event.getId());
            UserAuthIdentity eventIdentity = event.getIdentity();
            assertEquals(message().getProviderId(), eventIdentity.getProviderId());
            assertEquals(message().getUid(), eventIdentity.getUid());
        });
    }

    @Test
    @DisplayName("remove an identity")
    void changeState() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).hasState(state -> assertTrue(state.getSecondaryAuthIdentityList()
                                                                .isEmpty()));
    }

    private static RemoveSecondaryAuthIdentity createMessage() {
        return removeAuthIdentity(USER_ID);
    }
}
