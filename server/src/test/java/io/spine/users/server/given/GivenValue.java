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

import io.spine.net.EmailAddress;
import io.spine.people.PersonName;
import io.spine.users.PersonProfile;
import io.spine.users.user.User;

import static io.spine.util.Preconditions2.checkNotEmptyOrBlank;

/**
 * Test value objects for the Users context.
 */
public final class GivenValue {

    /** Prevents instantiation of this utility class. */
    private GivenValue() {
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
}
