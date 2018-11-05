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

import static io.spine.users.c.group.given.GroupTestCommands.joinParentGroup;
import static io.spine.users.c.group.given.GroupTestEnv.createGroupId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("JoinParentGroup command should")
class JoinParentGroupTest extends GroupMembershipCommandTest<JoinParentGroup> {

    private static final GroupId SUPER_GROUP = createGroupId();

    JoinParentGroupTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("produce JoinedParentGroup event")
    void produceEvent() {
        GroupMembershipPart part = createPartWithState();
        expectThat(part).producesEvent(JoinedParentGroup.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getParentGroupId(), event.getParentGroupId());
        });
    }

    @Test
    @DisplayName("add a group membership")
    void changeState() {
        GroupMembershipPart part = createPartWithState();
        expectThat(part).hasState(state -> {
            GroupId expectedGroup = message().getParentGroupId();
            assertTrue(state.getMembershipList()
                            .contains(expectedGroup));
        });
    }

    private static JoinParentGroup createMessage() {
        return joinParentGroup(GROUP_ID, SUPER_GROUP);
    }
}
