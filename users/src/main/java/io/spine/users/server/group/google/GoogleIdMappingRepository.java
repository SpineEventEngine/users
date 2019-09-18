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

package io.spine.users.server.group.google;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
import io.spine.server.projection.ProjectionRepository;
import io.spine.server.route.EventRouting;
import io.spine.users.GoogleIdMappingViewId;
import io.spine.users.google.group.event.GoogleGroupCreated;
import io.spine.users.group.google.GoogleIdMappingView;

import static io.spine.users.GoogleIdMappingViewId.Value.SINGLETON;

/**
 * The repository for {@link GoogleIdMappingProjection}.
 *
 * @author Vladyslav Lubenskyi
 */
public class GoogleIdMappingRepository extends ProjectionRepository<GoogleIdMappingViewId,
                                                                    GoogleIdMappingProjection,
                                                                    GoogleIdMappingView> {

    @VisibleForTesting
    static final GoogleIdMappingViewId PROJECTION_ID =
            GoogleIdMappingViewId
                    .newBuilder()
                    .setValue(SINGLETON)
                    .build();

    private static final ImmutableSet<GoogleIdMappingViewId> SINGLE_ID =
            ImmutableSet.of(PROJECTION_ID);

    /**
     * {@inheritDoc}
     *
     * <p>Sets up the event routing for {@link GoogleGroupCreated} events.
     */
    @OverridingMethodsMustInvokeSuper
    @Override
    protected void setupEventRouting(EventRouting<GoogleIdMappingViewId> routing) {
        super.setupEventRouting(routing);
        routing.route(GoogleGroupCreated.class, (m, ctx) -> SINGLE_ID);
    }
}
