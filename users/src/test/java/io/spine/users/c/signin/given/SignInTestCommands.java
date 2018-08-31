/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.signin.given;

import io.spine.core.UserId;
import io.spine.users.UserAuthIdentity;
import io.spine.users.c.signin.SignInPm;
import io.spine.users.c.signin.SignUserIn;
import io.spine.users.c.signin.SignUserInVBuilder;
import io.spine.users.c.signin.SignUserOut;
import io.spine.users.c.signin.SignUserOutVBuilder;

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

    public static SignUserOut signOutCommand(UserId id) {
        return SignUserOutVBuilder.newBuilder()
                                  .setId(id)
                                  .build();
    }
}
