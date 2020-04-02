/*
 * Copyright 2020, TeamDev. All rights reserved.
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

package io.spine.users.server.given;

import io.spine.core.UserId;
import io.spine.net.EmailAddress;
import io.spine.people.PersonName;
import io.spine.testing.core.given.GivenUserId;
import io.spine.users.GroupId;
import io.spine.users.PersonProfile;
import io.spine.users.user.User;

import static io.spine.testing.TestValues.random;
import static io.spine.util.Preconditions2.checkNotEmptyOrBlank;
import static java.lang.String.format;

/**
 * Test value objects for the Users context.
 */
public final class Given {

    /** Prevents instantiation of this utility class. */
    private Given() {
    }

    public static EmailAddress email(String value) {
        checkNotEmptyOrBlank(value);
        return EmailAddress
                .newBuilder()
                .setValue(value)
                .vBuild();
    }

    public static PersonName personName(String givenName, String familyName) {
        return PersonName
                .newBuilder()
                .setGivenName(givenName)
                .setFamilyName(familyName)
                .vBuild();
    }

    public static PersonProfile profile(PersonName name) {
        return PersonProfile
                .newBuilder()
                .setName(name)
                .vBuild();
    }

    public static User user(PersonName name) {
        return User
                .newBuilder()
                .setPerson(profile(name))
                .vBuild();
    }

    private static String generateId(String fmt) {
        String value = format(fmt, random(1, 1000));
        return value;
    }

    public static UserId userId() {
        String value = generateId("user-%d@example.com");
        return GivenUserId.of(value);
    }

    public static GroupId groupId() {
        return GroupId
                .newBuilder()
                .setValue(generateId("group-%d@example.com"))
                .vBuild();
    }

    public static EmailAddress email() {
        return EmailAddress
                .newBuilder()
                .setValue(generateId("john-%d@smith.com"))
                .vBuild();
    }
}
