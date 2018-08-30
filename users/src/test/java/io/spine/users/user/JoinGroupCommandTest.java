/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.user.given.TestAggregateFactory.createAggregate;
import static io.spine.users.user.given.UserTestCommands.startGroupMembership;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("JoinGroup command should")
public class JoinGroupCommandTest extends UserCommandTest<JoinGroup> {

    protected JoinGroupCommandTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate UserJoinedGroup event")
    void generateEvent() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).producesEvent(UserJoinedGroup.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getGroupId(), event.getGroupId());
        });
    }

    @Test
    @DisplayName("add a new group membership")
    void changeState() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).hasState(
                state -> assertEquals(message().getGroupId(), state.getMembership(0)));
    }

    private static JoinGroup createMessage() {
        return startGroupMembership(USER_ID);
    }
}
