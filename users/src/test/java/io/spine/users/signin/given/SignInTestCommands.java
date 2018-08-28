/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin.given;

import io.spine.core.UserId;
import io.spine.users.UserAuthIdentity;
import io.spine.users.signin.RemoteIdentitySignInPm;
import io.spine.users.signin.SignIn;
import io.spine.users.signin.SignInVBuilder;

/**
 * Test commands for {@link RemoteIdentitySignInPm}.
 *
 * @author Vladyslav Lubenskyi
 */
public class SignInTestCommands {

    /**
     * Prevents direct instantiation.
     */
    private SignInTestCommands() {
    }

    public static SignIn signInCommand(UserId id, UserAuthIdentity identity) {
        return SignInVBuilder.newBuilder()
                             .setId(id)
                             .setIdentity(identity)
                             .build();
    }
}
