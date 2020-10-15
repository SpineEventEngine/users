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

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import io.spine.core.UserId;
import io.spine.net.EmailAddress;
import io.spine.people.PersonName;
import io.spine.testing.TestValues;
import io.spine.testing.core.given.GivenUserId;
import io.spine.users.GroupId;
import io.spine.users.Person;
import io.spine.users.Service;
import io.spine.users.User;

import java.util.List;

import static io.spine.testing.TestValues.random;
import static io.spine.testing.TestValues.randomString;
import static io.spine.util.Preconditions2.checkNotEmptyOrBlank;
import static java.lang.String.format;

/**
 * Test value objects for the Users context.
 */
public final class Given {

    private static final ImmutableList<PersonName> names = ImmutableList.of(
            name("Georgina Ortega"),
            name("Rosa Sparks"),
            name("Orla Mccoy"),
            name("Betty Saunders"),
            name("Isabelle Khan"),
            name("Jeremiah Wallace"),
            name("Hugo Olsen"),
            name("Raymond Livingston"),
            name("Eugene O'Neill"),
            name("Keith Floyd")
    );

    /** Prevents instantiation of this utility class. */
    private Given() {
    }

    /** Generates a user ID using a random number in the range [0, 999]. */
    public static UserId userId() {
        String value = generateId("u-%03d");
        return GivenUserId.of(value);
    }

    private static String generateId(String fmt) {
        String value = format(fmt, random(1, 999));
        return value;
    }

    /** Generates a group ID using a random number in the range [0, 999]. */
    public static GroupId groupId() {
        return groupId(generateId("g-%03d"));
    }

    private static GroupId groupId(String value) {
        return GroupId
                .newBuilder()
                .setValue(value)
                .vBuild();
    }

    private static PersonName name(String name) {
        List<String> names =
                Splitter.on(' ')
                        .limit(2)
                        .splitToList(name);
        return name(names.get(0), names.get(1));
    }

    /** Creates a person name using the passed values. */
    public static PersonName name(String givenName, String familyName) {
        return PersonName
                .newBuilder()
                .setGivenName(givenName)
                .setFamilyName(familyName)
                .vBuild();
    }

    /** Creates a profile for the person with the given name. */
    public static Person person(PersonName name) {
        return Person
                .newBuilder()
                .setName(name)
                .vBuild();
    }

    /** Creates an email address with the passed value. */
    public static EmailAddress email(String value) {
        checkNotEmptyOrBlank(value);
        return EmailAddress
                .newBuilder()
                .setValue(value)
                .vBuild();
    }

    /** Creates a user with a randomly selected person name. */
    public static User humanUser() {
        return User.newBuilder()
                   .setPerson(person(name()))
                   .vBuild();
    }

    /** Generates a service user with a generated display name using the range [1, 99]. */
    public static User service() {
        String displayName = "Service_" + format("%02d", random(1, 99));
        Service service = Service
                .newBuilder()
                .setDisplayName(displayName)
                .vBuild();
        return User.newBuilder()
                   .setService(service)
                   .vBuild();
    }

    /** Generates an email address using a randomly selected person name. */
    public static EmailAddress email() {
        PersonName name = name();
        return email(name);
    }

    /** Generates a group email address using a random number in the range [1, 99]. */
    public static EmailAddress groupEmail() {
        String value = format("group-%02d@example.com", random(1, 99));
        return email(value);
    }

    /** Generates a group email address using UUID-based string. */
    public static EmailAddress groupEmailUuid() {
        String value = format("group-%s@example.com", randomString());
        return email(value);
    }

    /**
     * Creates an email address in the form {@code "<givenName>.<familyName>@example.com"}.
     */
    public static EmailAddress email(PersonName name) {
        String value = format(
                "%s.%s@example.com",
                name.getGivenName().toLowerCase(),
                name.getFamilyName().toLowerCase()
        );
        return EmailAddress.newBuilder()
                           .setValue(value)
                           .vBuild();
    }

    /** Randomly selected name from a list of pre-defined. */
    public static PersonName name() {
        int index = TestValues.random(names.size() - 1);
        PersonName result = names.get(index);
        return result;
    }

    /** Creates a person profile with randomly selected name and corresponding email address. */
    public static Person person() {
        PersonName name = name();
        return Person.newBuilder()
                     .setName(name)
                     .setEmail(email(name))
                     .vBuild();
    }
}
