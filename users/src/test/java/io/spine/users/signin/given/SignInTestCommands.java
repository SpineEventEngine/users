/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin.given;

import io.spine.core.UserId;
import io.spine.users.signin.SignInPm;
import io.spine.users.signin.command.FinishSignIn;
import io.spine.users.signin.command.FinishSignInVBuilder;
import io.spine.users.signin.command.SignUserIn;
import io.spine.users.signin.command.SignUserInVBuilder;
import io.spine.users.signin.command.SignUserOut;
import io.spine.users.signin.command.SignUserOutVBuilder;
import io.spine.users.user.Identity;

import static io.spine.users.signin.given.SignInTestEnv.failureReason;

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
