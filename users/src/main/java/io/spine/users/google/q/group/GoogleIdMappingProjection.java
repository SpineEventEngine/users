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

package io.spine.users.google.q.group;

import io.spine.core.Subscribe;
import io.spine.server.projection.Projection;
import io.spine.users.google.GoogleIdMappingViewId;
import io.spine.users.google.c.group.GoogleGroupCreated;

import static io.spine.users.google.q.group.GoogleIdMappingRepository.PROJECTION_ID;

/**
 * A projection that maps Google Group IDs to `GroupId`s.
 *
 * <p>This projection is used to find corresponding `GroupId` when handling external events from
 * Directory API.
 *
 * @author Vladyslav Lubenskyi
 */
public class GoogleIdMappingProjection
        extends Projection<GoogleIdMappingViewId,
                           GoogleIdMappingView,
                           GoogleIdMappingViewVBuilder> {

    /**
     * @see Projection#Projection(Object)
     */
    protected GoogleIdMappingProjection(GoogleIdMappingViewId id) {
        super(id);
    }

    @Subscribe
    public void on(GoogleGroupCreated event) {
        getBuilder().setId(PROJECTION_ID)
                    .putMappings(event.getGoogleId(), event.getId());
    }
}
