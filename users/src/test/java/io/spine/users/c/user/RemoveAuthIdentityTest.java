/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import io.spine.users.Identity;
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
@DisplayName("RemoveSecondaryIdentity command should")
class RemoveAuthIdentityTest extends UserCommandTest<RemoveSecondaryIdentity> {

    RemoveAuthIdentityTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate AuthIdentityRemoved event")
    void generateEvent() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).producesEvent(SecondaryIdentityRemoved.class, event -> {
            assertEquals(message().getId(), event.getId());
            Identity eventIdentity = event.getIdentity();
            assertEquals(message().getProviderId(), eventIdentity.getProviderId());
            assertEquals(message().getUserId(), eventIdentity.getUserId());
        });
    }

    @Test
    @DisplayName("remove an identity")
    void changeState() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).hasState(state -> assertTrue(state.getSecondaryIdentityList()
                                                                .isEmpty()));
    }

    @Test
    @DisplayName("throw rejection if auth identity doesn't exist")
    void generateRejection() {
        UserAggregate aggregate = createEmptyAggregate();
        expectThat(aggregate).throwsRejection(Rejections.IdentityDoesNotExist.class);
    }

    private static RemoveSecondaryIdentity createMessage() {
        return removeAuthIdentity(USER_ID);
    }
}
