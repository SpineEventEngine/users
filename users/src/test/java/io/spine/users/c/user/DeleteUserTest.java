/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.user.given.UserTestCommands.deleteUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("DeleteUser command should")
class DeleteUserTest extends UserPartCommandTest<DeleteUser> {

    DeleteUserTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate UserDeleted event")
    void generateEvent() {
        UserPart aggregate = createPartWithState();
        expectThat(aggregate).producesEvent(UserDeleted.class,
                event -> assertEquals(message().getId(),
                        event.getId()));
    }

    @Test
    @DisplayName("mark aggregate as deleted")
    void changeState() {
        UserPart aggregate = createPartWithState();
        expectThat(aggregate).hasState(state -> assertTrue(aggregate.getLifecycleFlags()
                .getDeleted()));
    }

    private static DeleteUser createMessage() {
        return deleteUser(USER_ID);
    }
}
