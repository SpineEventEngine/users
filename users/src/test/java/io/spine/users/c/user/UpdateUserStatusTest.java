/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.user.TestUserFactory.createAggregate;
import static io.spine.users.c.user.given.UserTestCommands.updateUserStatus;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("UpdateUserStatus command should")
class UpdateUserStatusTest extends UserCommandTest<UpdateUserStatus> {

    UpdateUserStatusTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate UserStatusUpdated event")
    void generateEvent() {
        UserAggregate aggregate = createAggregate();
        User.Status oldStatus = aggregate.getState()
                                         .getStatus();
        expectThat(aggregate).producesEvent(UserStatusUpdated.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getNewStatus(), event.getNewStatus());
            assertEquals(oldStatus, event.getOldStatus());
        });
    }

    @Test
    @DisplayName("update the status")
    void changeState() {
        expectThat(createAggregate()).hasState(
                state -> assertEquals(message().getNewStatus(), state.getStatus()));
    }

    private static UpdateUserStatus createMessage() {
        return updateUserStatus(USER_ID);
    }
}
