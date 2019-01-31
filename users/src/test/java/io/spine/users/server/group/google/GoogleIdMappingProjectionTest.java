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

package io.spine.users.server.group.google;

import io.spine.server.entity.Repository;
import io.spine.testing.server.projection.ProjectionTest;
import io.spine.users.GoogleIdMappingViewId;
import io.spine.users.GoogleIdMappingViewIdVBuilder;
import io.spine.users.GroupId;
import io.spine.users.google.group.event.GoogleGroupCreated;
import io.spine.users.group.google.GoogleIdMappingView;
import io.spine.users.server.group.google.given.GoogleIdMappingTestEvents;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.spine.users.GoogleIdMappingViewId.Value.SINGLETON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("GoogleMappingProjection should")
class GoogleIdMappingProjectionTest extends ProjectionTest<GoogleIdMappingViewId,
                                                           GoogleGroupCreated,
                                                           GoogleIdMappingView,
                                                           GoogleIdMappingProjection> {

    private static final GoogleIdMappingViewId ID =
            GoogleIdMappingViewIdVBuilder.newBuilder()
                                         .setValue(SINGLETON)
                                         .build();

    GoogleIdMappingProjectionTest() {
        super(ID, groupCreated());
    }

    @Test
    @DisplayName("store GoogleId - GroupId pair when group is created")
    void testPair() {
        GoogleIdMappingProjection projection = new GoogleIdMappingProjection(ID);
        expectThat(projection).hasState(state -> {
            Map<String, GroupId> mapping = state.getGroupsMap();
            assertFalse(mapping.isEmpty());

            String rawGoogleId = message().getGoogleId()
                                          .getValue();
            assertTrue(mapping.containsKey(rawGoogleId));

            GroupId groupId = mapping.get(rawGoogleId);
            assertEquals(message().getId(), groupId);
        });
    }

    @Override
    protected Repository<GoogleIdMappingViewId, GoogleIdMappingProjection> createRepository() {
        return new GoogleIdMappingRepository();
    }

    private static GoogleGroupCreated groupCreated() {
        return GoogleIdMappingTestEvents.googleGroupCreated();
    }
}
