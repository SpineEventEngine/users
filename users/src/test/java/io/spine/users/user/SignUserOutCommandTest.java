/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.user.given.TestAggregateFactory.createAggregate;
import static io.spine.users.user.given.UserTestCommands.signUserOut;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("SignUserOut command should")
public class SignUserOutCommandTest extends UserCommandTest<SignUserOut> {

    protected SignUserOutCommandTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate UserSignedOut event")
    void generateEvent() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).producesEvent(UserSignedOut.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertTrue(event.hasWhenSignedOut());
        });
    }

    private static SignUserOut createMessage() {
        return signUserOut(USER_ID);
    }
}
