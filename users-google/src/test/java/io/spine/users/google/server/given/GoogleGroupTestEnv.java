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

package io.spine.users.google.server.given;

import io.spine.net.EmailAddress;
import io.spine.net.InternetDomain;
import io.spine.users.google.GoogleGroupId;
import io.spine.users.GroupId;
import io.spine.users.google.server.GoogleGroupLifecyclePm;

import static io.spine.base.Identifier.newUuid;

/**
 * Test environment for testing {@link GoogleGroupLifecyclePm}.
 */
public final class GoogleGroupTestEnv {

    /**
     * Prevents instantiation.
     */
    private GoogleGroupTestEnv() {
    }

    public static GroupId newGroupId() {
        return GroupId
                .newBuilder()
                .setValue(newUuid())
                .build();
    }

    public static GoogleGroupId googleId() {
        return GoogleGroupId
                .newBuilder()
                .setValue("x123pwd")
                .build();
    }

    static String description() {
        return "Developers from the 3rd floor";
    }

    static EmailAddress email() {
        return EmailAddress
                .newBuilder()
                .setValue("developers@spine.io")
                .build();
    }

    static EmailAddress newEmail() {
        return EmailAddress
                .newBuilder()
                .setValue("developers+1@spine.io")
                .build();
    }

    static String newDescription() {
        return "Developers from the 5th floor";
    }

    static InternetDomain internalDomain() {
        return InternetDomain
                .newBuilder()
                .setValue("spine.io")
                .vBuild();
    }

    static InternetDomain externalDomain() {
        return InternetDomain
                .newBuilder()
                .setValue("another-spine.io")
                .vBuild();
    }

    public static EmailAddress alias() {
        return EmailAddress.newBuilder()
                           .setValue("developers@spine.eu")
                           .build();
    }

    public static GroupId parentGroup() {
        return GroupId
                .newBuilder()
                .setValue(newUuid())
                .build();
    }

    public static String groupName() {
        return "devs";
    }

    public static String role() {
        return "MEMBER";
    }

    static String displayName() {
        return "Developers At Spine";
    }
}
