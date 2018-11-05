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

package io.spine.users.q.group;

import io.spine.users.GroupId;
import io.spine.users.c.group.JoinedParentGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.spine.users.q.group.GroupViewTestProjections.groupWithoutMemberProjection;
import static io.spine.users.q.group.given.GroupViewTestEvents.joinedParentGroup;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("when a group JoinedParentGroup")
class JoinedParentGroupTest extends GroupViewTest<JoinedParentGroup> {

    JoinedParentGroupTest() {
        super(joinedParentGroup(PROJECTION_ID));
    }

    @Test
    @DisplayName("parent group should add a member")
    void testState() {
        expectThat(groupWithoutMemberProjection(PROJECTION_ID)).hasState(state -> {

            List<GroupId> membersList = state.getChildGroupsList();
            assertFalse(membersList.isEmpty());
            assertEquals(membersList.get(0), message().getId());
        });
    }
}
