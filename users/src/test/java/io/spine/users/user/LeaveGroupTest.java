/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.user.given.UserTestCommands.stopGroupMembership;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("LeaveGroup command should")
class LeaveGroupTest extends UserMembershipCommandTest<LeaveGroup> {

    LeaveGroupTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate UserLeftGroup event")
    void generateEvent() {
        UserMembershipPart part = createPartWithState();
        expectThat(part).producesEvent(UserLeftGroup.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getGroupId(), event.getGroupId());
        });
    }

    @Test
    @DisplayName("stop a group membership")
    void changeState() {
        UserMembershipPart part = createPartWithState();
        expectThat(part)
                .hasState(state -> assertTrue(state.getMembershipList()
                                                   .isEmpty()));
    }

    private static LeaveGroup createMessage() {
        return stopGroupMembership(USER_ID);
    }
}
