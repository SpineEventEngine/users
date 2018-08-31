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

import static io.spine.time.ZonedDateTimes.now;
import static io.spine.users.c.signin.given.SignInTestEnv.displayName;
import static io.spine.users.c.signin.given.SignInTestEnv.identity;
import static io.spine.users.c.signin.given.SignInTestEnv.profile;
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
                                  .setProfile(profile())
                                  .setStatus(ACTIVE)
                                  .setDisplayName(displayName())
                                  .setPrimaryIdentity(identity())
                                  .setWhenCreated(now())
                                  .build();
    }
}
