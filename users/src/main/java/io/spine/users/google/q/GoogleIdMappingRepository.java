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

import io.spine.server.projection.ProjectionRepository;
import io.spine.users.google.GoogleIdMappingViewId;
import io.spine.users.google.GoogleIdMappingViewIdVBuilder;
import io.spine.users.google.c.group.GoogleGroupCreated;

import static com.google.common.collect.ImmutableSet.of;
import static io.spine.users.google.GoogleIdMappingViewId.Value.SINGLETON;

/**
 * The repository for {@link GoogleIdMappingProjection}.
 *
 * @author Vladyslav Lubenskyi
 */
public class GoogleIdMappingRepository extends ProjectionRepository<GoogleIdMappingViewId,
                                                                    GoogleIdMappingProjection,
                                                                    GoogleIdMappingView> {

    static final GoogleIdMappingViewId PROJECTION_ID =
            GoogleIdMappingViewIdVBuilder
                    .newBuilder()
                    .setValue(SINGLETON)
                    .build();

    /**
     * {@inheritDoc}
     *
     * <p>Sets up the event routing for {@link io.spine.users.google.c.group.GoogleGroupCreated}
     * events.
     */
    @Override
    public void onRegistered() {
        super.onRegistered();
        getEventRouting().route(GoogleGroupCreated.class, (event, context) -> of(PROJECTION_ID));
    }
}
