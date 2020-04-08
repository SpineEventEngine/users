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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
import io.spine.core.Subscribe;
import io.spine.server.projection.Projection;
import io.spine.server.projection.ProjectionRepository;
import io.spine.server.route.EventRouting;
import io.spine.users.google.GoogleGroupId;
import io.spine.users.google.event.GoogleGroupCreated;
import io.spine.users.google.IdMap;

/**
 * The repository for {@link Entity}.
 */
public class IdMapping
        extends ProjectionRepository<String, IdMapping.Entity, IdMap> {

    @VisibleForTesting
    static final String ID = "IdMap";

    private static final ImmutableSet<String> SINGLETON = ImmutableSet.of(ID);

    /**
     * {@inheritDoc}
     *
     * <p>Sets up the event routing for {@link GoogleGroupCreated} events.
     */
    @OverridingMethodsMustInvokeSuper
    @Override
    protected void setupEventRouting(EventRouting<String> routing) {
        super.setupEventRouting(routing);
        routing.route(GoogleGroupCreated.class, (m, ctx) -> SINGLETON);
    }

    /**
     * A projection that maps Google Group IDs to `GroupId`s.
     *
     * <p>This projection is used to find corresponding `GroupId` when handling external events
     * from the Google Directory API.
     */
    static class Entity extends Projection<String, IdMap, IdMap.Builder> {

        @Subscribe
        void on(GoogleGroupCreated event) {
            GoogleGroupId googleId = event.getGoogleId();
            builder().setId(ID)
                     .putGroups(googleId.getValue(), event.getId());
        }
    }
}
