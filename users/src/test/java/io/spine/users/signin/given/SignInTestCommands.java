/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.signin.given;

import io.spine.core.UserId;
import io.spine.users.Identity;
import io.spine.users.c.signin.FinishSignIn;
import io.spine.users.c.signin.FinishSignInVBuilder;
import io.spine.users.c.signin.SignInPm;
import io.spine.users.c.signin.SignUserIn;
import io.spine.users.c.signin.SignUserInVBuilder;
import io.spine.users.c.signin.SignUserOut;
import io.spine.users.c.signin.SignUserOutVBuilder;

import static io.spine.users.c.signin.given.SignInTestEnv.failureReason;

/**
 * Test commands for {@link SignInPm}.
 *
 * @author Vladyslav Lubenskyi
 */
public final class SignInTestCommands {

    /**
     * Prevents instantiation.
     */
    private SignInTestCommands() {
    }

    public static SignUserIn signInCommand(UserId id, Identity identity) {
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

    public static FinishSignIn finishSignInSuccessfully(UserId id) {
        return FinishSignInVBuilder.newBuilder()
                                   .setId(id)
                                   .setSuccessful(true)
                                   .build();
    }

    public static FinishSignIn finishSignInUnsuccessfully(UserId id) {
        return FinishSignInVBuilder.newBuilder()
                                   .setId(id)
                                   .setSuccessful(false)
                                   .setFailureReason(failureReason())
                                   .build();
    }
}
