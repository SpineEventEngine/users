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
@DisplayName("StopGroupMembership command should")
public class StopGroupMembershipCommandShould extends UserCommandTest<StopGroupMembership> {

    @Test
    @DisplayName("generate GroupMembershipStopped event")
    void generateEvent() {
        UserAggregate aggregate = createAggregateWithGroup();
        expectThat(aggregate).producesEvent(GroupMembershipStopped.class, event -> {
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

    @Override
    protected StopGroupMembership createMessage() {
        return stopGroupMembership();
    }
}
