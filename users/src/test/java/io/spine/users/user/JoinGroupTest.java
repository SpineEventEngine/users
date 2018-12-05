/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.user.given.UserTestCommands.startGroupMembership;

@DisplayName("JoinGroup command should")
class JoinGroupTest extends UserMembershipCommandTest<JoinGroup> {

    JoinGroupTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate UserJoinedGroup event")
    void generateEvent() {
        UserMembershipPart part = createPart();
        expectThat(part).producesEvent(UserJoinedGroup.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getGroupId(), event.getGroupId());
        });
    }

    @Test
    @DisplayName("add a new group membership")
    void changeState() {
        UserMembershipPart part = createPart();
        expectThat(part).hasState(
                state -> assertEquals(message().getGroupId(), state.getMembership(0)
                                                                   .getGroupId()));
    }

    private static JoinGroup createMessage() {
        return startGroupMembership(USER_ID);
    }
}
