/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user;

import io.spine.users.signin.RemoteIdentitySignInFinished;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.user.given.TestAggregateFactory.createAggregate;
import static io.spine.users.user.given.UserTestEvents.signInFinished;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("RemoteIdentitySignInFinished event should")
public class RemoteIdentitySignedInEventShould extends UserEventTest<RemoteIdentitySignInFinished> {

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

    @Override
    protected RemoteIdentitySignInFinished createMessage() {
        return signInFinished();
    }
}
