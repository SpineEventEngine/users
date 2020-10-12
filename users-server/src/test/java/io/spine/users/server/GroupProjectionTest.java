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

import io.spine.users.Group;
import io.spine.users.GroupId;
import io.spine.users.event.GroupCreated;
import io.spine.users.event.GroupDeleted;
import io.spine.users.event.GroupRenamed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.testing.TestValues.randomString;
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
        GroupCreated groupCreated = created(displayName, description);

        context().receivesEvent(groupCreated);

        Group expected = Group.newBuilder()
                .setId(id)
                .setDisplayName(displayName)
                .setDescription(description)
                .vBuild();
        assertState(expected);
    }

    private GroupCreated created(String displayName, String description) {
        return GroupCreated.newBuilder()
                           .setGroup(id)
                           .setDisplayName(displayName)
                           .setDescription(description)
                           .vBuild();
    }

    @Test
    @DisplayName("update group display name")
    void whenRenamed() {
        String newName = randomString();
        String oldName = randomString();
        GroupRenamed groupRenamed = GroupRenamed.newBuilder()
                .setGroup(id)
                .setNewName(newName)
                .setOldName(oldName)
                .vBuild();

        context().receivesEvent(groupRenamed);

        Group expected = Group.newBuilder()
                .setId(id)
                .setDisplayName(newName)
                .build();
        assertState(expected);
    }

    @Test
    @DisplayName("mark the entity `deleted` on `GroupDeleted`")
    void whenDeleted() {
        GroupDeleted groupDeleted = GroupDeleted.newBuilder()
                .setGroup(id)
                .vBuild();

        context().receivesEvent(created(randomString(), randomString()))
                 .receivesEvent(groupDeleted);

        context().assertEntity(id, GroupProjection.class)
                 .deletedFlag()
                 .isTrue();
    }

    private void assertState(Group expected) {
        context().assertState(id, Group.class)
                 .comparingExpectedFieldsOnly()
                 .isEqualTo(expected);
    }
}
