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
import io.spine.users.c.group.GroupCreated;
import io.spine.users.c.group.JoinedParentGroup;
import io.spine.users.c.group.LeftParentGroup;
import io.spine.users.q.group.given.GroupViewTestEvents;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.spine.users.q.group.GroupViewTest.PROJECTION_ID;
import static io.spine.users.q.group.GroupViewTestProjections.emptyProjection;
import static io.spine.users.q.group.GroupViewTestProjections.groupWithMemberProjection;
import static io.spine.users.q.group.GroupViewTestProjections.groupWithoutMemberProjection;
import static io.spine.users.q.group.given.GroupViewTestEvents.joinedParentGroup;
import static io.spine.users.q.group.given.GroupViewTestEvents.leftParentGroup;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("GroupView projection should")
class GroupViewProjectionTest {

    @Nested
    @DisplayName("when internal GroupCreated")
    class OnInternalGroupCreated extends GroupViewTest<GroupCreated> {

        OnInternalGroupCreated() {
            super(internalGroupCreated());
        }

        @Test
        @DisplayName("initialize state")
        void testState() {
            expectThat(emptyProjection(PROJECTION_ID)).hasState(state -> {
                assertEquals(message().getDisplayName(), state.getDisplayName());
                assertEquals(message().getEmail(), state.getEmail());
                assertEquals(message().getOrgEntity(), state.getOrgEntity());
                assertEquals(message().getRoleList(), state.getRoleList());
                assertFalse(state.getExternal());
            });
        }
    }

    @Nested
    @DisplayName("when external GroupCreated")
    class OnExternalGroupCreated extends GroupViewTest<GroupCreated> {

        OnExternalGroupCreated() {
            super(externalGroupCreated());
        }

        @Test
        @DisplayName("initialize state")
        void testState() {
            expectThat(emptyProjection(PROJECTION_ID)).hasState(state -> {
                assertEquals(message().getDisplayName(), state.getDisplayName());
                assertEquals(message().getEmail(), state.getEmail());
                assertEquals(message().getExternalDomain(), state.getExternalDomain());
                assertEquals(message().getRoleList(), state.getRoleList());
                assertTrue(state.getExternal());
            });
        }
    }

    @Nested
    @DisplayName("when a group LeftParentGroup")
    class OnLeftParentGroup extends GroupViewTest<LeftParentGroup> {

        OnLeftParentGroup() {
            super(leftParentGroup(PROJECTION_ID));
        }

        @Test
        @DisplayName("parent group should remove a member")
        void testState() {
            expectThat(groupWithMemberProjection(PROJECTION_ID)).hasState(state -> {
                List<GroupId> membersList = state.getChildGroupList();
                assertTrue(membersList.isEmpty());
            });
        }
    }

    @Nested
    @DisplayName("when a group JoinedParentGroup")
    class OnJoinedParentGroup extends GroupViewTest<JoinedParentGroup> {

        OnJoinedParentGroup() {
            super(joinedParentGroup(PROJECTION_ID));
        }

        @Test
        @DisplayName("parent group should add a member")
        void testState() {
            expectThat(groupWithoutMemberProjection(PROJECTION_ID)).hasState(state -> {
                List<GroupId> membersList = state.getChildGroupList();
                assertFalse(membersList.isEmpty());
                assertEquals(membersList.get(0), message().getId());
            });
        }
    }

    private static GroupCreated internalGroupCreated() {
        return GroupViewTestEvents.internalGroupCreated(PROJECTION_ID);
    }

    private static GroupCreated externalGroupCreated() {
        return GroupViewTestEvents.externalGroupCreated(PROJECTION_ID);
    }
}
