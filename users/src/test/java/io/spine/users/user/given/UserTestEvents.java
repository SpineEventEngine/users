/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user.given;

import io.spine.core.UserId;
import io.spine.users.signin.SignInCompleted;
import io.spine.users.signin.SignInCompletedVBuilder;
import io.spine.users.user.UserAggregate;

import static io.spine.users.user.given.UserTestEnv.googleIdentity;

/**
 * Test events for {@link UserAggregate}.
 *
 * @author Vladyslav Lubenskyi
 */
public class UserTestEvents {

    /**
     * Prevents direct instantiation.
     */
    private UserTestEvents() {
    }

    public static SignInCompleted signInCompleted(UserId id) {
        return SignInCompletedVBuilder.newBuilder()
                                      .setId(id)
                                      .setIdentity(googleIdentity())
                                      .build();
    }
}
