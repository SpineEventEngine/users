/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.user.TestUserFactory.createAggregate;
import static io.spine.users.c.user.given.UserTestCommands.assignRoleToUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("AssignRoleToUser command should")
class AssignRoleToUserTest extends UserCommandTest<AssignRoleToUser> {

    AssignRoleToUserTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate RoleAssignedToUser event")
    void generateEvent() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).producesEvent(RoleAssignedToUser.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getRoleId(), event.getRoleId());
        });
    }

    @Test
    @DisplayName("add a new role")
    void changeState() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).hasState(
                state -> assertEquals(message().getRoleId(), state.getRole(1)));
    }

    private static AssignRoleToUser createMessage() {
        return assignRoleToUser(USER_ID);
    }
}
