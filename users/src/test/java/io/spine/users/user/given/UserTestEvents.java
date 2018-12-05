/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user.given;

import io.spine.core.UserId;
import io.spine.users.c.signin.SignInSuccessful;
import io.spine.users.c.signin.SignInSuccessfulVBuilder;
import io.spine.users.c.signin.SignOutCompleted;
import io.spine.users.c.signin.SignOutCompletedVBuilder;
import io.spine.users.c.user.UserPart;

import static io.spine.users.c.user.given.UserTestEnv.googleIdentity;

/**
 * Test events for {@link UserPart}.
 *
 * @author Vladyslav Lubenskyi
 */
public class UserTestEvents {

    /**
     * Prevents direct instantiation.
     */
    private UserTestEvents() {
    }

    public static SignInSuccessful signInSuccessful(UserId id) {
        return SignInSuccessfulVBuilder.newBuilder()
                                       .setId(id)
                                       .setIdentity(googleIdentity())
                                       .build();
    }

    public static SignOutCompleted signOutCompleted(UserId id) {
        return SignOutCompletedVBuilder.newBuilder()
                                       .setId(id)
                                       .build();
    }
}
