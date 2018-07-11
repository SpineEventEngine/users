/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user.given;

import io.spine.users.signin.RemoteIdentitySignInFinished;
import io.spine.users.signin.RemoteIdentitySignInFinishedVBuilder;
import io.spine.users.user.UserAggregate;

import static io.spine.users.user.given.UserTestEnv.googleIdentity;
import static io.spine.users.user.given.UserTestEnv.userId;

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

    public static RemoteIdentitySignInFinished signInFinished() {
        return RemoteIdentitySignInFinishedVBuilder.newBuilder()
                .setId(userId())
                .setIdentity(googleIdentity())
                .build();
    }
}
