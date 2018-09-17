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

package io.spine.users.c.group;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.group.TestGroupFactory.createAggregate;
import static io.spine.users.c.group.TestGroupFactory.createAggregateWithOwners;
import static io.spine.users.c.group.TestGroupFactory.createEmptyAggregate;
import static io.spine.users.c.group.given.GroupTestCommands.removeGroupOwner;
import static io.spine.users.c.group.given.GroupTestEnv.groupOwner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("RemoveGroupOwner command should")
class RemoveGroupOwnerTest extends GroupCommandTest<RemoveGroupOwner> {

    RemoveGroupOwnerTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("produce GroupOwnerRemoved event")
    void produceEvent() {
        GroupAggregate aggregate = createAggregateWithOwners(GROUP_ID);
        expectThat(aggregate).producesEvent(GroupOwnerRemoved.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getOwner(), event.getRemovedOwner());
        });
    }

    @Test
    @DisplayName("remove the owner")
    void changeState() {
        GroupAggregate aggregate = createAggregateWithOwners(GROUP_ID);
        expectThat(aggregate).hasState(state -> {
            assertFalse(state.getOwnersList()
                             .contains(groupOwner()));
        });
    }

    @Test
    @DisplayName("generates NoSuchOwnerInGroup")
    void handleNoSuchOwner() {
        GroupAggregate aggregate = createEmptyAggregate(GROUP_ID);
        expectThat(aggregate).throwsRejection(Rejections.NoSuchOwnerInGroup.class);
    }

    @Test
    @DisplayName("generates GroupMustHaveAnOwner")
    void handleNoOwners() {
        GroupAggregate aggregate = createAggregate(GROUP_ID);

        expectThat(aggregate).throwsRejection(Rejections.GroupMustHaveAnOwner.class);
    }

    private static RemoveGroupOwner createMessage() {
        return removeGroupOwner(GROUP_ID);
    }
}
