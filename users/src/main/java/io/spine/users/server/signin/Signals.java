/*
 * Copyright 2019, TeamDev. All rights reserved.
 *
 * Redistribution and use in source and/or binary forms, with or without
 * modification, must retain the above copyright notice and the following
 * disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.spine.users.server.signin;

import io.spine.core.UserId;
import io.spine.users.PersonProfile;
import io.spine.users.signin.SignInFailureReason;
import io.spine.users.signin.command.FinishSignIn;
import io.spine.users.signin.command.SignUserIn;
import io.spine.users.signin.event.SignInFailed;
import io.spine.users.signin.event.SignInSuccessful;
import io.spine.users.signin.event.SignOutCompleted;
import io.spine.users.user.Identity;
import io.spine.users.user.command.CreateUser;

import static io.spine.users.user.User.Status.ACTIVE;
import static io.spine.users.user.UserNature.PERSON;

/**
 * Convenience methods for creating command- and event messages related to the sign-in process.
 */
public final class Signals {

    /** Prevents instantiation of this utility class. */
    private Signals() {
    }

    public static SignUserIn signIn(UserId user, Identity identity) {
        return SignUserIn
                .newBuilder()
                .setId(user)
                .setIdentity(identity)
                .build();
    }

    static FinishSignIn finishSuccessfully(UserId user) {
        return FinishSignIn
                .newBuilder()
                .setId(user)
                .setSuccessful(true)
                .build();
    }

    public static FinishSignIn finishWithError(UserId user, SignInFailureReason failureReason) {
        return FinishSignIn
                .newBuilder()
                .setId(user)
                .setSuccessful(false)
                .setFailureReason(failureReason)
                .build();
    }

    static CreateUser createUser(UserId user, Identity identity, PersonProfile profile) {
        String displayName = profile.getEmail()
                                    .getValue();
        return CreateUser
                .newBuilder()
                .setId(user)
                .setDisplayName(displayName)
                .setPrimaryIdentity(identity)
                .setProfile(profile)
                .setStatus(ACTIVE)
                .setExternalDomain(identity.getDomain())
                .setNature(PERSON)
                .build();
    }

    static SignInSuccessful signInSuccessful(UserId user, Identity identity) {
        return SignInSuccessful
                .newBuilder()
                .setId(user)
                .setIdentity(identity)
                .build();
    }

    static SignInFailed
    signInFailed(UserId user, Identity identity, SignInFailureReason reason) {
        return SignInFailed
                .newBuilder()
                .setId(user)
                .setIdentity(identity)
                .setFailureReason(reason)
                .build();
    }

    static SignOutCompleted signOutCompleted(UserId user) {
        return SignOutCompleted
                .newBuilder()
                .setId(user)
                .build();
    }
}
