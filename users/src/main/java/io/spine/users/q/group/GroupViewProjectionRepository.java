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

import io.spine.server.projection.ProjectionRepository;
import io.spine.server.route.EventRouting;
import io.spine.users.GroupId;
import io.spine.users.c.group.JoinedParentGroup;
import io.spine.users.c.group.LeftParentGroup;
import io.spine.users.c.user.UserJoinedGroup;
import io.spine.users.c.user.UserLeftGroup;

import static com.google.common.collect.ImmutableSet.of;

/**
 * The repository for {@link GroupViewProjection}.
 */
public class GroupViewProjectionRepository extends ProjectionRepository<GroupId,
                                                                        GroupViewProjection,
                                                                        GroupView> {

    /**
     * {@inheritDoc}
     *
     * <p>Sets up the event routing for {@link JoinedParentGroup} and {@link LeftParentGroup}
     * events.
     */
    @Override
    public void onRegistered() {
        super.onRegistered();
        EventRouting<GroupId> routing = getEventRouting();
        routing.route(JoinedParentGroup.class,
                      (event, context) -> of(event.getParentGroupId()))
               .route(LeftParentGroup.class,
                      (event, context) -> of(event.getParentGroupId()))
               .route(UserJoinedGroup.class,
                      (event, context) -> of(event.getGroupId()))
               .route(UserLeftGroup.class,
                      (event, context) -> of(event.getGroupId()));
    }
}
