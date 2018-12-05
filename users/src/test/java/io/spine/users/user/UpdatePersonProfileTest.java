/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user;

import io.spine.users.user.command.UpdatePersonProfile;
import io.spine.users.user.event.PersonProfileUpdated;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.user.given.UserTestCommands.updatePersonProfile;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("UpdatePersonProfile command should")
class UpdatePersonProfileTest extends UserPartCommandTest<UpdatePersonProfile> {

    UpdatePersonProfileTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate PersonProfileUpdated event")
    void generateEvent() {
        expectThat(createPartWithState()).producesEvent(PersonProfileUpdated.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getUpdatedProfile(), event.getUpdatedProfile());
        });
    }

    @Test
    @DisplayName("update the profile")
    void changeState() {
        expectThat(createPartWithState()).hasState(
                state -> assertEquals(message().getUpdatedProfile(), state.getProfile()));
    }

    private static UpdatePersonProfile createMessage() {
        return updatePersonProfile(USER_ID);
    }
}
