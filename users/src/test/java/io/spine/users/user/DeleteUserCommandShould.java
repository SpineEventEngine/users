/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.user.given.TestAggregateFactory.createAggregate;
import static io.spine.users.user.given.UserTestCommands.deleteUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("DeleteUser command should")
public class DeleteUserCommandShould extends UserCommandTest<DeleteUser> {

    @Test
    @DisplayName("generate UserDeleted event")
    void generateEvent() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).producesEvent(UserDeleted.class,
                event -> assertEquals(message().getId(),
                        event.getId()));
    }

    @Test
    @DisplayName("mark aggregate as deleted")
    void changeState() {
        UserAggregate aggregate = createAggregate();
        expectThat(aggregate).hasState(state -> assertTrue(aggregate.getLifecycleFlags()
                .getDeleted()));
    }

    @Override
    protected DeleteUser createMessage() {
        return deleteUser();
    }

}
