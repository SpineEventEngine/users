/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin.given;

import io.spine.core.UserId;
import io.spine.users.signin.SignInPm;
import io.spine.users.user.event.UserCreated;
import io.spine.users.user.event.UserCreatedVBuilder;

import static io.spine.users.signin.given.SignInTestEnv.displayName;
import static io.spine.users.signin.given.SignInTestEnv.identity;
import static io.spine.users.signin.given.SignInTestEnv.profile;
import static io.spine.users.user.User.Status.ACTIVE;
import static io.spine.users.user.UserNature.PERSON;

/**
 * Test events for {@link SignInPm}.
 *
 * @author Vladyslav Lubenskyi
 */
public final class SignInTestEvents {

    /**
     * Prevents instantiation.
     */
    private SignInTestEvents() {
    }

    public static UserCreated userCreated(UserId id) {
        return UserCreatedVBuilder.newBuilder()
                                  .setId(id)
                                  .setProfile(profile())
                                  .setStatus(ACTIVE)
                                  .setDisplayName(displayName())
                                  .setPrimaryIdentity(identity())
                                  .setNature(PERSON)
                                  .build();
    }
}
