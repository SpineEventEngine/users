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

package io.spine.users.q.role;

import io.spine.server.projection.ProjectionRepository;
import io.spine.users.RoleId;
import io.spine.users.c.group.RoleAssignedToGroup;
import io.spine.users.c.group.RoleUnassignedFromGroup;
import io.spine.users.c.user.RoleAssignedToUser;
import io.spine.users.c.user.RoleUnassignedFromUser;

import static com.google.common.collect.ImmutableSet.of;

/**
 * The repository for {@link RoleViewProjection}.
 */
public class RoleViewRepository
        extends ProjectionRepository<RoleId, RoleViewProjection, RoleView> {

    public RoleViewRepository() {
        super();
        getEventRouting().route(RoleAssignedToUser.class,
                                (event, context) -> of(event.getRoleId()))
                         .route(RoleUnassignedFromUser.class,
                                (event, context) -> of(event.getRoleId()))
                         .route(RoleAssignedToGroup.class,
                                (event, context) -> of(event.getRoleId()))
                         .route(RoleUnassignedFromGroup.class,
                                (event, context) -> of(event.getRoleId()));
    }
}
