/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin.given;

import io.spine.core.UserId;
import io.spine.testing.server.entity.given.Given;
import io.spine.users.signin.RemoteIdentitySignIn;
import io.spine.users.signin.RemoteIdentitySignIn.Status;
import io.spine.users.signin.RemoteIdentitySignInPm;
import io.spine.users.signin.RemoteIdentitySignInVBuilder;
import io.spine.users.user.UserAggregate;

import static io.spine.users.signin.given.SignInTestEnv.identity;
import static io.spine.users.signin.given.SignInTestEnv.userId;

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
    public static RemoteIdentitySignInPm createEmptyProcMan(UserId id) {
        return new RemoteIdentitySignInPm(id);
    }

    public static RemoteIdentitySignInPm nonEmptyProcMan(Status status) {
        RemoteIdentitySignIn state = RemoteIdentitySignInVBuilder.newBuilder()
                .setId(userId())
                .setIdentity(identity())
                .setStatus(status)
                .build();
        return Given.processManagerOfClass(RemoteIdentitySignInPm.class)
                    .withId(userId())
                    .withState(state)
                    .build();
    }
}
