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

package io.spine.users.server.user.given;

import io.spine.core.UserId;
import io.spine.net.EmailAddress;
import io.spine.people.PersonName;
import io.spine.testing.core.given.GivenUserId;
import io.spine.users.GroupId;
import io.spine.users.PersonProfile;

/**
 * The environment for the {@code UserPart} tests.
 */
public class UserTestEnv {

    /**
     * Prevents direct instantiation.
     */
    private UserTestEnv() {
    }

    public static UserId userId() {
        return GivenUserId.of("john.smith@example.com");
    }

    public static GroupId firstGroupId() {
        return GroupId
                .newBuilder()
                .setValue("fisrt_group@example.com")
                .vBuild();
    }

    public static String userDisplayName() {
        return "John Smith";
    }

    public static String newUserDisplayName() {
        return "John Doe";
    }

    public static PersonProfile profile() {
        return PersonProfile
                .newBuilder()
                .setName(personName())
                .setEmail(email())
                .vBuild();
    }

    public static PersonProfile newProfile() {
        return PersonProfile
                .newBuilder()
                .setName(newPersonName())
                .setEmail(newEmail())
                .vBuild();
    }

    private static EmailAddress email() {
        return EmailAddress
                .newBuilder()
                .setValue("john@smith.com")
                .vBuild();
    }

    private static PersonName personName() {
        return PersonName
                .newBuilder()
                .setGivenName("Jane")
                .setFamilyName("Jones")
                .vBuild();
    }

    private static EmailAddress newEmail() {
        return EmailAddress
                .newBuilder()
                .setValue("john+alias@smith.com")
                .vBuild();
    }

    private static PersonName newPersonName() {
        return PersonName
                .newBuilder()
                .setGivenName("John")
                .setMiddleName("The Person")
                .setFamilyName("Smith")
                .vBuild();
    }
}
