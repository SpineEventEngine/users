/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.user.given.TestAggregateFactory.createAggregateWithGroup;
import static io.spine.users.user.given.UserTestCommands.stopGroupMembership;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("LeaveGroup command should")
public class LeaveGroupCommandTest extends UserCommandTest<LeaveGroup> {

    protected LeaveGroupCommandTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate UserLeftGroup event")
    void generateEvent() {
        UserAggregate aggregate = createAggregateWithGroup();
        expectThat(aggregate).producesEvent(UserLeftGroup.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getGroupId(), event.getGroupId());
        });
    }

    @Test
    @DisplayName("stop a group membership")
    void changeState() {
        UserAggregate aggregate = createAggregateWithGroup();
        expectThat(aggregate).hasState(state -> assertTrue(state.getMembershipList()
                .isEmpty()));
    }

    private static LeaveGroup createMessage() {
        return stopGroupMembership(USER_ID);
    }
}
