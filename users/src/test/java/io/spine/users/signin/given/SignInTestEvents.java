/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin.given;

import io.spine.core.UserId;
import io.spine.users.signin.SignInPm;
import io.spine.users.user.UserCreated;
import io.spine.users.user.UserCreatedVBuilder;

import static io.spine.time.OffsetDateTimes.now;
import static io.spine.time.ZoneOffsets.utc;
import static io.spine.users.User.Status.ACTIVE;
import static io.spine.users.signin.given.SignInTestEnv.displayName;
import static io.spine.users.signin.given.SignInTestEnv.identity;
import static io.spine.users.signin.given.SignInTestEnv.profile;

/**
 * Test events for {@link SignInPm}.
 *
 * @author Vladyslav Lubenskyi
 */
public class SignInTestEvents {

    /**
     * Prevents direct instantiation.
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
                                  .setWhenCreated(now(utc()))
                                  .build();
    }
}
