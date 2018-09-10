/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.user.TestUserFactory.createAggregate;
import static io.spine.users.c.user.given.UserTestCommands.updateUserProfile;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("UpdateUserProfile command should")
class UpdateUserProfileTest extends UserCommandTest<UpdateUserProfile> {

    UpdateUserProfileTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("generate UserProfileUpdated event")
    void generateEvent() {
        expectThat(createAggregate()).producesEvent(UserProfileUpdated.class, event -> {
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

    private static UpdateUserProfile createMessage() {
        return updateUserProfile(USER_ID);
    }
}
