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
import static io.spine.users.c.group.given.GroupTestCommands.addSuperGroup;
import static io.spine.users.c.group.given.GroupTestEnv.createGroupId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("AddSuperGroup command should")
class StartNestedMembershipTest extends GroupCommandTest<AddSuperGroup> {

    private static final GroupId SUPER_GROUP = createGroupId();

    StartNestedMembershipTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("produce SuperGroupAdded event")
    void produceEvent() {
        GroupAggregate aggregate = createAggregate(GROUP_ID);
        expectThat(aggregate).producesEvent(SuperGroupAdded.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getSuperGroupId(), event.getSuperGroupId());
        });
    }

    @Test
    @DisplayName("add a group membership")
    void changeState() {
        GroupAggregate aggregate = createAggregate(GROUP_ID);

        expectThat(aggregate).hasState(state -> {
            GroupId expectedGroup = message().getSuperGroupId();
            assertTrue(state.getMembershipList()
                            .contains(expectedGroup));
        });
    }

    private static AddSuperGroup createMessage() {
        return addSuperGroup(GROUP_ID, SUPER_GROUP);
    }
}
