/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user;

import io.spine.users.signin.SignInCompleted;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.user.given.TestAggregateFactory.createAggregate;
import static io.spine.users.user.given.UserTestEvents.signInCompleted;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("SignInFinished event should")
public class SignedInCompletedEventTest extends UserEventTest<SignInCompleted> {

    protected SignedInCompletedEventTest() {
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
