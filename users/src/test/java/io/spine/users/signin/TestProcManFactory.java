/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin;

import io.spine.core.UserId;
import io.spine.testing.server.entity.given.Given;
import io.spine.users.signin.SignIn.Status;
import io.spine.users.user.UserPart;

import static io.spine.users.signin.given.SignInTestEnv.identity;
import static io.spine.users.signin.given.SignInTestEnv.userId;

/**
 * A factory for creating test {@linkplain UserPart User aggregates}.
 *
 * @author Vladyslav Lubenskyi
 */
final class TestProcManFactory {

    /**
     * Prevents direct instantiation.
     */
    private TestProcManFactory() {
    }

    /**
     * Creates a new instance of the process manager with the default state.
     */
    public static SignInPm createEmptyProcMan(UserId id) {
        return new SignInPm(id);
    }

    public static SignInPm nonEmptyProcMan(Status status) {
        SignIn state = SignInVBuilder.newBuilder()
                                     .setId(userId())
                                     .setIdentity(identity())
                                     .setStatus(status)
                                     .build();
        return Given.processManagerOfClass(SignInPm.class)
                    .withId(userId())
                    .withState(state)
                    .build();
    }
}
