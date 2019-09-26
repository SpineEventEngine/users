/*
 * Copyright 2019, TeamDev. All rights reserved.
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

package io.spine.users.google.server;

import io.spine.users.google.event.GoogleGroupCreated;
import io.spine.users.google.IdMap;
import io.spine.users.server.UsersContextTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.google.server.given.GoogleIdMappingTestEvents.googleGroupCreated;

@DisplayName("`GoogleIdMappingProjection` should")
class GoogleIdMappingProjectionTest extends UsersContextTest {

    @Test
    @DisplayName("store `GoogleId`-`GroupId` pair when the Google group is created")
    void testPair() {
        GoogleGroupCreated event = googleGroupCreated();
        IdMap expectedState = IdMap
                .newBuilder()
                .setId(IdMapping.ID)
                .putGroups(event.getGoogleId()
                                .getValue(), event.getId())
                .build();
        context().receivesEvent(event)
                 .assertEntityWithState(IdMap.class, expectedState.getId())
                 .hasStateThat()
                 .comparingExpectedFieldsOnly()
                 .isEqualTo(expectedState);
    }
}
