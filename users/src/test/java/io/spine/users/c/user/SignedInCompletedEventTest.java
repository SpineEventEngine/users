/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import io.spine.users.c.signin.SignInCompleted;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.user.TestUserFactory.createAggregate;
import static io.spine.users.c.user.given.UserTestEvents.signInCompleted;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("SignInCompleted event should")
class SignedInCompletedEventTest extends UserEventTest<SignInCompleted> {

    SignedInCompletedEventTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate UserSignedIn event")
    void generateEvent() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).producesEvent(UserSignedIn.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertTrue(event.hasWhenSignedIn());
            assertEquals(message().getIdentity(), event.getIdentity());
        });
    }

    private static SignInCompleted createMessage() {
        return signInCompleted(USER_ID);
    }
}
