/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin.given;

import io.spine.users.signin.RemoteIdentitySignInProcMan;
import io.spine.users.signin.SignInRemoteIdentity;
import io.spine.users.signin.SignInRemoteIdentityVBuilder;

import static io.spine.users.signin.given.SignInTestEnv.identity;
import static io.spine.users.signin.given.SignInTestEnv.userId;

/**
 * Test commands for {@link RemoteIdentitySignInProcMan}.
 *
 * @author Vladyslav Lubenskyi
 */
public class SignInTestCommands {

    /**
     * Prevents direct instantiation.
     */
    private SignInTestCommands() {
    }

    public static SignInRemoteIdentity signInRemoteIdentity() {
        return SignInRemoteIdentityVBuilder.newBuilder()
                .setId(userId())
                .setIdentity(identity())
                .build();
    }
}
