/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.user.given.UserTestCommands.renameUser;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("RenameUser command should")
class RenameUserTest extends UserPartCommandTest<RenameUser> {

    RenameUserTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate UserRenamed event")
    void generateEvent() {
        UserPart aggregate = createPartWithState();
        String oldName = aggregate.getState()
                                  .getDisplayName();
        expectThat(aggregate).producesEvent(UserRenamed.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getNewName(), event.getNewName());
            assertEquals(oldName, event.getOldName());
        });
    }

    @Test
    @DisplayName("change User's display_name")
    void changeState() {
        UserPart aggregate = createPartWithState();
        expectThat(aggregate).hasState(
                state -> assertEquals(message().getNewName(), state.getDisplayName()));
    }

    private static RenameUser createMessage() {
        return renameUser(USER_ID);
    }
}
