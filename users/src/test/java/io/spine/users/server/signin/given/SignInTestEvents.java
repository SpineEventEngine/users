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

package io.spine.users.server.signin.given;

import io.spine.core.UserId;
import io.spine.users.server.signin.SignInPm;
import io.spine.users.user.event.UserCreated;
import io.spine.users.user.event.UserCreatedVBuilder;

import static io.spine.net.InternetDomains.valueOf;
import static io.spine.users.server.signin.given.SignInTestEnv.displayName;
import static io.spine.users.server.signin.given.SignInTestEnv.identity;
import static io.spine.users.server.signin.given.SignInTestEnv.profile;
import static io.spine.users.user.User.Status.ACTIVE;
import static io.spine.users.user.UserNature.PERSON;

/**
 * Test events for {@link SignInPm}.
 */
public final class SignInTestEvents {

    /**
     * Prevents instantiation.
     */
    private SignInTestEvents() {
    }

    public static UserCreated userCreated(UserId id) {
        return UserCreatedVBuilder.newBuilder()
                                  .setId(id)
                                  .setProfile(profile())
                                  .setStatus(ACTIVE)
                                  .setDisplayName(displayName())
                                  .setPrimaryIdentity(identity())
                                  .setNature(PERSON)
                                  .setExternalDomain(valueOf("teamvdev.com"))
                                  .build();
    }
}
