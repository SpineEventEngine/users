/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.user.User.Status.NOT_READY;
import static io.spine.users.c.user.User.Status.SUSPENDED;
import static io.spine.users.c.user.TestUserFactory.createAggregate;
import static io.spine.users.c.user.given.UserTestCommands.changeUserStatus;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("ChangeUserStatus command should")
class ChangeUserStatusCommandTest extends UserCommandTest<ChangeUserStatus> {

    ChangeUserStatusCommandTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate UserStatusChanged event")
    void generateEvent() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).producesEvent(UserStatusChanged.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getStatus(), event.getNewStatus());
            assertEquals(NOT_READY, event.getOldStatus());
        });
    }

    @Test
    @DisplayName("change status of the user")
    void changeState() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).hasState(state -> assertEquals(SUSPENDED, state.getStatus()));
    }

    private static ChangeUserStatus createMessage() {
        return changeUserStatus(USER_ID);
    }
}
