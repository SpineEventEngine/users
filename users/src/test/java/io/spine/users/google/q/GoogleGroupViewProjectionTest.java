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

package io.spine.users.google.q;

import io.spine.users.google.c.group.GoogleGroupAliasesChanged;
import io.spine.users.google.c.group.GoogleGroupCreated;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.spine.users.google.given.GoogleGroupTestEvents.googleGroupAliasesChanged;
import static io.spine.users.google.given.GoogleGroupTestEvents.internalGoogleGroupCreated;
import static io.spine.users.google.q.given.GoogleGroupViewProjectionTestEnv.projectionWithValidState;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("GoogleGroupViewProjection should")
class GoogleGroupViewProjectionTest {

    @Nested
    @DisplayName("subscribe on GoogleGroupCreated")
    class OnGoogleGroupCreated extends GoogleGroupViewEventTest<GoogleGroupCreated> {

        private final GoogleGroupViewProjection projection =
                new GoogleGroupViewProjection(PROJECTION_ID);

        OnGoogleGroupCreated() {
            super(internalGoogleGroupCreated(PROJECTION_ID));
        }

        @Test
        @DisplayName("and update state")
        void updateState() {
            GoogleGroupView expectedState = GoogleGroupView
                    .newBuilder()
                    .setId(event().getId())
                    .setGoogleId(event().getGoogleId())
                    .addAllAlias(event().getAliasList())
                    .build();
            expectThat(projection)
                    .hasState(state -> assertEquals(expectedState, state));
        }
    }

    @Nested
    @DisplayName("subscribe on GoogleGroupAliasesChanged")
    class OnGoogleGroupAliasesChanged extends GoogleGroupViewEventTest<GoogleGroupAliasesChanged> {

        private final GoogleGroupViewProjection projection =
                projectionWithValidState(PROJECTION_ID);

        OnGoogleGroupAliasesChanged() {
            super(googleGroupAliasesChanged(PROJECTION_ID));
        }

        @Test
        @DisplayName("and update state")
        void updateState() {
            GoogleGroupView expectedState = projection.getState()
                                                      .toBuilder()
                                                      .clearAlias()
                                                      .addAllAlias(event().getNewAliasList())
                                                      .build();
            expectThat(projection).hasState(state -> assertEquals(expectedState, state));
        }
    }
}
