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
//import io.spine.testing.server.entity.given.Given;
import io.spine.users.DirectoryId;
import io.spine.users.server.user.UserPart;
import io.spine.users.signin.SignIn;
import io.spine.users.signin.SignIn.Status;
import io.spine.users.user.Identity;

import static io.spine.users.server.signin.given.SignInTestEnv.identity;
import static io.spine.users.server.signin.given.SignInTestEnv.userId;
import static io.spine.users.signin.SignIn.Status.SIS_UNKNOWN;

/**
 * A factory for creating test {@linkplain UserPart User aggregates}.
 */
final class TestProcManFactory {

    /**
     * Prevents direct instantiation.
     */
    private TestProcManFactory() {
    }

    /**
     * Creates a new instance of the process manager with the default state.
     */
    static SignInPm createEmptyProcMan(UserId id) {
        return new SignInPm(id);
    }

//    static SignInPm nonEmptyProcMan(Status status) {
//        SignIn state = signIn(userId(), identity(), status);
//        return Given.processManagerOfClass(SignInPm.class)
//                    .withId(userId())
//                    .withState(state)
//                    .build();
//    }

//    static SignInPm withIdentity(UserId id, DirectoryId directoryId) {
//        Identity identity = identity(id.getValue(), directoryId, "Test User");
//        SignIn state = signIn(id, identity, SIS_UNKNOWN);
//        return Given.processManagerOfClass(SignInPm.class)
//                    .withId(id)
//                    .withState(state)
//                    .build();
//    }

    private static SignIn signIn(UserId id, Identity identity, Status status) {
        return SignIn
                .newBuilder()
                .setId(id)
                .setIdentity(identity)
                .setStatus(status)
                .build();
    }

    static DirectoryId directoryId(String id) {
        return DirectoryId
                .newBuilder()
                .setValue(id)
                .build();
    }
}
