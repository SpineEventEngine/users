/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.user.TestUserFactory.createAggregate;
import static io.spine.users.c.user.TestUserFactory.createEmptyAggregate;
import static io.spine.users.c.user.given.UserTestCommands.unassignRoleFromUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("UnassignRoleFromUser command should")
class UnassignRoleFromUserTest extends UserCommandTest<UnassignRoleFromUser> {

    UnassignRoleFromUserTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate RoleUnassignedFromUser event")
    void generateEvent() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).producesEvent(RoleUnassignedFromUser.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getRoleId(), event.getRoleId());
        });
    }

    @Test
    @DisplayName("remove a role")
    void changeState() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).hasState(state -> assertTrue(state.getRoleList()
                .isEmpty()));
    }

    @Test
    @DisplayName("throw rejection if role isn't assigned to a user")
    void throwRejection() {
        UserAggregate aggregate = createEmptyAggregate();
        expectThat(aggregate).throwsRejection(Rejections.RoleIsNotAssignedToUser.class);
    }

    private static UnassignRoleFromUser createMessage() {
        return unassignRoleFromUser(USER_ID);
    }
}