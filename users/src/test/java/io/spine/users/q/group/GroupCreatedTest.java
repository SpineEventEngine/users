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

import io.spine.users.c.group.GroupCreated;
import io.spine.users.q.group.given.GroupViewTestEvents;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.spine.users.q.group.GroupViewTest.PROJECTION_ID;
import static io.spine.users.q.group.GroupViewTestProjections.emptyProjection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("GroupView projection should")
class GroupCreatedTest {

    @Nested
    @DisplayName("when inner GroupCreated")
    class InnerGroupCreatedTest extends GroupViewTest<GroupCreated> {

        InnerGroupCreatedTest() {
            super(innerGroupCreated());
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
    class ExternalGroupCreatedTest extends GroupViewTest<GroupCreated> {

        ExternalGroupCreatedTest() {
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

    private static GroupCreated innerGroupCreated() {
        return GroupViewTestEvents.internalGroupCreated(PROJECTION_ID);
    }

    private static GroupCreated externalGroupCreated() {
        return GroupViewTestEvents.externalGroupCreated(PROJECTION_ID);
    }
}
