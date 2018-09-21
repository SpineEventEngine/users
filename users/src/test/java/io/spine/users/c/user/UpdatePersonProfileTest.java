/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.user.TestUserFactory.createAggregate;
import static io.spine.users.c.user.given.UserTestCommands.updatePersonProfile;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("UpdatePersonProfile command should")
class UpdatePersonProfileTest extends UserCommandTest<UpdatePersonProfile> {

    UpdatePersonProfileTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate PersonProfileUpdated event")
    void generateEvent() {
        expectThat(createAggregate()).producesEvent(PersonProfileUpdated.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getUpdatedProfile(), event.getUpdatedProfile());
        });
    }

    @Test
    @DisplayName("update the profile")
    void changeState() {
        expectThat(createAggregate()).hasState(
                state -> assertEquals(message().getUpdatedProfile(), state.getProfile()));
    }

    private static UpdatePersonProfile createMessage() {
        return updatePersonProfile(USER_ID);
    }
}
