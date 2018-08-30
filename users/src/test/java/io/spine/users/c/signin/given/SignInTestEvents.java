/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.signin.given;

import io.spine.core.UserId;
import io.spine.users.c.signin.SignInPm;
import io.spine.users.c.user.UserCreated;
import io.spine.users.c.user.UserCreatedVBuilder;

import static io.spine.time.OffsetDateTimes.now;
import static io.spine.time.ZoneOffsets.utc;
import static io.spine.users.c.user.User.Status.ACTIVE;

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
                                  .setProfile(SignInTestEnv.profile())
                                  .setStatus(ACTIVE)
                                  .setDisplayName(SignInTestEnv.displayName())
                                  .setPrimaryIdentity(SignInTestEnv.identity())
                                  .setWhenCreated(now(utc()))
                                  .build();
    }
}
