/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import io.spine.users.c.signin.SignOutCompleted;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.user.TestUserFactory.createAggregate;
import static io.spine.users.c.user.given.UserTestEvents.signOutCompleted;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("SignOutCompleted event should")
class SignedOutCompletedEventTest extends UserEventTest<SignOutCompleted> {

    SignedOutCompletedEventTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate UserSignedIn event")
    void generateEvent() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).producesEvent(UserSignedOut.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertTrue(event.hasWhenSignedOut());
        });
    }

    private static SignOutCompleted createMessage() {
        return signOutCompleted(USER_ID);
    }
}
