/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin.given;

import io.spine.core.UserId;
import io.spine.users.UserAuthIdentity;
import io.spine.users.signin.SignInPm;
import io.spine.users.signin.SignUserIn;
import io.spine.users.signin.SignUserInVBuilder;

/**
 * Test commands for {@link SignInPm}.
 *
 * @author Vladyslav Lubenskyi
 */
public class SignInTestCommands {

    /**
     * Prevents direct instantiation.
     */
    private SignInTestCommands() {
    }

    public static SignUserIn signInCommand(UserId id, UserAuthIdentity identity) {
        return SignUserInVBuilder.newBuilder()
                                 .setId(id)
                                 .setIdentity(identity)
                                 .build();
    }
}
