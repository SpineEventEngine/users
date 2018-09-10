/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import io.spine.users.ParentEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.user.TestUserFactory.createAggregate;
import static io.spine.users.c.user.given.UserTestCommands.moveUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("MoveUser command should")
class MoveUserTest extends UserCommandTest<MoveUser> {

    MoveUserTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate UserMoved event")
    void generateEvent() {
        UserAggregate aggregate = createAggregate();
        ParentEntity oldParent = aggregate.getState()
                                          .getParentEntity();
        expectThat(aggregate).producesEvent(UserMoved.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getNewParentEntity(), event.getNewParentEntity());
            assertEquals(oldParent, event.getOldParentEntity());
        });
    }

    @Test
    @DisplayName("change parent entity of the user")
    void changeState() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).hasState(
                state -> assertEquals(message().getNewParentEntity(), state.getParentEntity()));
    }

    private static MoveUser createMessage() {
        return moveUser(USER_ID);
    }
}
