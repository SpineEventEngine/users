/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import io.spine.users.UserAuthIdentity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.user.TestUserFactory.createAggregate;
import static io.spine.users.c.user.TestUserFactory.createEmptyAggregate;
import static io.spine.users.c.user.given.UserTestCommands.removeAuthIdentity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("RemoveSecondaryAuthIdentity command should")
class RemoveAuthIdentityTest extends UserCommandTest<RemoveSecondaryAuthIdentity> {

    RemoveAuthIdentityTest() {
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
            assertEquals(message().getUserId(), eventIdentity.getUserId());
        });
    }

    @Test
    @DisplayName("remove an identity")
    void changeState() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).hasState(state -> assertTrue(state.getSecondaryAuthIdentityList()
                                                                .isEmpty()));
    }

    @Test
    @DisplayName("throw rejection if auth identity doesn't exist")
    void generateRejection() {
        UserAggregate aggregate = createEmptyAggregate();
        expectThat(aggregate).throwsRejection(Rejections.AuthIdentityDoesNotExist.class);
    }

    private static RemoveSecondaryAuthIdentity createMessage() {
        return removeAuthIdentity(USER_ID);
    }
}
