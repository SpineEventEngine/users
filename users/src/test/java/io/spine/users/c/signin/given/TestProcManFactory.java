/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.signin.given;

import io.spine.core.UserId;
import io.spine.testing.server.entity.given.Given;
import io.spine.users.c.signin.SignIn;
import io.spine.users.c.signin.SignIn.Status;
import io.spine.users.c.signin.SignInPm;
import io.spine.users.c.signin.SignInVBuilder;
import io.spine.users.c.user.UserAggregate;

/**
 * A factory for creating test {@linkplain UserAggregate User aggregates}.
 *
 * @author Vladyslav Lubenskyi
 */
public class TestProcManFactory {

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
                                     .setId(SignInTestEnv.userId())
                                     .setIdentity(SignInTestEnv.identity())
                                     .setStatus(status)
                                     .build();
        return Given.processManagerOfClass(SignInPm.class)
                    .withId(SignInTestEnv.userId())
                    .withState(state)
                    .build();
    }
}
