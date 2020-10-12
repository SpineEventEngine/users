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

package io.spine.users.server;

import io.spine.net.EmailAddress;
import io.spine.users.Group;
import io.spine.users.GroupId;
import io.spine.users.event.GroupCreated;
import io.spine.users.event.GroupDeleted;
import io.spine.users.event.GroupDescriptionChanged;
import io.spine.users.event.GroupEmailChanged;
import io.spine.users.event.GroupRenamed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.spine.testing.TestValues.randomString;
import static io.spine.users.server.given.Given.groupEmail;
import static io.spine.users.server.given.Given.groupEmailUuid;
import static io.spine.users.server.given.Given.groupId;

@DisplayName("`GroupProjection` should")
class GroupProjectionTest extends UsersContextTest {

    private GroupId id;

    @BeforeEach
    void generateData() {
        id = groupId();
    }

    @Test
    @DisplayName("initialize on `GroupCreated`")
    void whenCreated() {
        String displayName = randomString();
        String description = randomString();
        EmailAddress email = groupEmail();
        GroupCreated groupCreated = created(displayName, description, email);

        context().receivesEvent(groupCreated);

        Group expected = Group
                .newBuilder()
                .setId(id)
                .setDisplayName(displayName)
                .setDescription(description)
                .setEmail(email)
                .vBuild();
        assertState(expected);
    }

    @Test
    @DisplayName("update group display name")
    void whenRenamed() {
        String newName = randomString();
        String oldName = randomString();
        GroupRenamed groupRenamed = GroupRenamed
                .newBuilder()
                .setGroup(id)
                .setNewName(newName)
                .setOldName(oldName)
                .vBuild();

        context().receivesEvent(groupRenamed);

        Group expected = Group
                .newBuilder()
                .setId(id)
                .setDisplayName(newName)
                .build();
        assertState(expected);
    }

    @Test
    @DisplayName("mark the entity `deleted` on `GroupDeleted`")
    void whenDeleted() {
        GroupDeleted groupDeleted = GroupDeleted
                .newBuilder()
                .setGroup(id)
                .vBuild();

        context().receivesEvent(created(randomString(), randomString()))
                 .receivesEvent(groupDeleted);

        context().assertEntity(id, GroupProjection.class)
                 .deletedFlag()
                 .isTrue();
    }

    @Test
    @DisplayName("update group description")
    void whenDescriptionChanged() {
        String newDescription = randomString();
        String prevDescription = randomString();
        GroupDescriptionChanged descriptionChanged = GroupDescriptionChanged
                .newBuilder()
                .setGroup(id)
                .setNewDescription(newDescription)
                .setOldDescription(prevDescription)
                .vBuild();

        context().receivesEvent(created(randomString(), prevDescription))
                 .receivesEvent(descriptionChanged);

        Group expected = Group.newBuilder()
                .setId(id)
                .setDescription(newDescription)
                .build();
        assertState(expected);
    }

    @ParameterizedTest
    @MethodSource("emailPairs")
    @DisplayName("update email address when it changes")
    void onEvent(EmailAddress oldEmail, EmailAddress newEmail) {
        GroupEmailChanged emailChanged = GroupEmailChanged
                .newBuilder()
                .setGroup(id)
                .setNewEmail(newEmail)
                .setOldEmail(oldEmail)
                .vBuild();

        context().receivesEvent(created(randomString(), randomString(), oldEmail))
                 .receivesEvent(emailChanged);

        Group expected = Group
                .newBuilder()
                .setId(id)
                .setEmail(newEmail)
                .buildPartial();

        assertState(expected);
    }

    /**
     * Provides pairs of group email addresses for {@link #onEvent(EmailAddress, EmailAddress)}.
     */
    private static Stream<Arguments> emailPairs() {
        EmailAddress empty = EmailAddress.getDefaultInstance();
        return Stream.of(
                Arguments.of(groupEmail(), empty),
                Arguments.of(empty, groupEmail()),
                Arguments.of(groupEmail(), groupEmailUuid())
        );
    }

    private GroupCreated created(String displayName, String description) {
        return GroupCreated.newBuilder()
                           .setGroup(id)
                           .setDisplayName(displayName)
                           .setDescription(description)
                           .vBuild();
    }

    private void assertState(Group expected) {
        context().assertState(id, Group.class)
                 .comparingExpectedFieldsOnly()
                 .isEqualTo(expected);
    }

    private GroupCreated created(String displayName, String description, EmailAddress email) {
        return created(displayName, description)
                .toBuilder()
                .setEmail(email)
                .vBuild();
    }
}
