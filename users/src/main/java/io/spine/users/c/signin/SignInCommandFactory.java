/*
 * Copyright 2018, TeamDev. All rights reserved.
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

package io.spine.users.c.signin;

import io.spine.core.UserId;
import io.spine.users.Identity;
import io.spine.users.PersonProfile;
import io.spine.users.c.user.CreateUser;
import io.spine.users.c.user.CreateUserVBuilder;

import static io.spine.users.c.user.User.Status.ACTIVE;
import static io.spine.users.c.user.UserNature.PERSON;

/**
 * A command factory for the {@link SignIn} process manager.
 *
 * @author Vladyslav Lubenskyi
 */
class SignInCommandFactory {

    private final UserId id;

    /**
     * Prevents direct instantiation.
     *
     * @param id an ID of the process manager
     */
    private SignInCommandFactory(UserId id) {
        this.id = id;
    }

    /**
     * Retrieves an instance of {@link SignInCommandFactory}.
     *
     * @param id an ID of the process manager
     */
    static SignInCommandFactory instance(UserId id) {
        return new SignInCommandFactory(id);
    }

    FinishSignIn finishWithError(SignInFailureReason failureReason) {
        return FinishSignInVBuilder
                .newBuilder()
                .setId(id)
                .setSuccessful(false)
                .setFailureReason(failureReason)
                .build();
    }

    FinishSignIn finishSuccessfully() {
        return FinishSignInVBuilder
                .newBuilder()
                .setId(id)
                .setSuccessful(true)
                .build();
    }

    SignUserIn signIn(Identity identity) {
        return SignUserInVBuilder
                .newBuilder()
                                 .setId(id)
                                 .setIdentity(identity)
                                 .build();
    }

    CreateUser createUser(Identity identity, PersonProfile profile) {
        String displayName = profile.getEmail()
                                    .getValue();
        return CreateUserVBuilder
                .newBuilder()
                                 .setId(id)
                                 .setDisplayName(displayName)
                                 .setPrimaryIdentity(identity)
                                 .setProfile(profile)
                                 .setStatus(ACTIVE)
                                 .setNature(PERSON)
                                 .build();
    }
}
