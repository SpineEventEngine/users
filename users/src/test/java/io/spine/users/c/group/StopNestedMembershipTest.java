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

import io.spine.users.GroupId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.group.TestGroupFactory.createAggregate;
import static io.spine.users.c.group.given.GroupTestCommands.removeSuperGroup;
import static io.spine.users.c.group.given.GroupTestEnv.upperGroupId;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("RemoveSuperGroup command should")
class StopNestedMembershipTest extends GroupCommandTest<RemoveSuperGroup> {

    StopNestedMembershipTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("produce SuperGroupRemoved event")
    void produceEvent() {
        GroupAggregate aggregate = createAggregate(GROUP_ID);
        expectThat(aggregate).producesEvent(SuperGroupRemoved.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getSuperGroupId(), event.getSuperGroupId());
        });
    }

    @Test
    @DisplayName("remove a group membership")
    void changeState() {
        GroupAggregate aggregate = createAggregate(GROUP_ID);

        expectThat(aggregate).hasState(state -> {
            GroupId expectedGroup = message().getSuperGroupId();
            assertFalse(state.getMembershipList()
                             .contains(expectedGroup));
        });
    }

    private static RemoveSuperGroup createMessage() {
        return removeSuperGroup(GROUP_ID, upperGroupId());
    }
}
