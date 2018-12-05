/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user;

import io.spine.users.user.command.UnassignRoleFromUser;
import io.spine.users.user.event.RoleUnassignedFromUser;
import io.spine.users.user.rejection.Rejections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.user.given.UserTestCommands.unassignRoleFromUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("UnassignRoleFromUser command should")
class UnassignRoleFromUserTest extends UserPartCommandTest<UnassignRoleFromUser> {

    UnassignRoleFromUserTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate RoleUnassignedFromUser event")
    void generateEvent() {
        UserPart aggregate = createPartWithState();
        expectThat(aggregate).producesEvent(RoleUnassignedFromUser.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getRoleId(), event.getRoleId());
        });
    }

    @Test
    @DisplayName("remove a role")
    void changeState() {
        UserPart aggregate = createPartWithState();
        expectThat(aggregate).hasState(state -> assertTrue(state.getRoleList()
                .isEmpty()));
    }

    @Test
    @DisplayName("throw rejection if role isn't assigned to a user")
    void throwRejection() {
        expectThat(new UserPart(root(USER_ID))).throwsRejection(Rejections.RoleIsNotAssignedToUser.class);
    }

    private static UnassignRoleFromUser createMessage() {
        return unassignRoleFromUser(USER_ID);
    }
}
