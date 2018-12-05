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

package io.spine.users.group;

import io.spine.users.GroupId;
import io.spine.users.group.command.LeaveParentGroup;
import io.spine.users.group.event.LeftParentGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.group.given.GroupTestCommands.leaveParentGroup;
import static io.spine.users.group.given.GroupTestEnv.upperGroupId;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("LeaveParentGroup command should")
class LeaveParentGroupTest extends GroupMembershipCommandTest<LeaveParentGroup> {

    LeaveParentGroupTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("produce ParentGroupLeft event")
    void produceEvent() {
        GroupMembershipPart part = createPartWithState();
        expectThat(part).producesEvent(LeftParentGroup.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getParentGroupId(), event.getParentGroupId());
        });
    }

    @Test
    @DisplayName("remove a group membership")
    void changeState() {
        GroupMembershipPart part = createPartWithState();
        expectThat(part).hasState(state -> {
            GroupId expectedGroup = message().getParentGroupId();
            assertFalse(state.getMembershipList()
                             .contains(expectedGroup));
        });
    }

    private static LeaveParentGroup createMessage() {
        return leaveParentGroup(GROUP_ID, upperGroupId());
    }
}
