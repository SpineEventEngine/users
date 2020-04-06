/*
 * Copyright 2020, TeamDev. All rights reserved.
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

package io.spine.roles.server;

import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
import io.spine.server.procman.ProcessManagerRepository;
import io.spine.server.route.EventRoute;
import io.spine.server.route.EventRouting;
import io.spine.users.GroupId;
import io.spine.users.group.event.UserAddedToGroup;
import io.spine.users.group.event.UserRemovedFromGroup;

import static io.spine.server.route.EventRoute.withId;

/**
 * The repository for {@link RolePropagation}.
 */
final class RolePropagations
        extends ProcessManagerRepository<GroupId, RolePropagation, GroupRolesPropagation> {

    @OverridingMethodsMustInvokeSuper
    @Override
    protected void setupEventRouting(EventRouting<GroupId> routing) {
        super.setupEventRouting(routing);
        routing.replaceDefault(EventRoute.byFirstMessageField(idClass()));
        routing.route(UserAddedToGroup.class, (message, context) -> withId(message.getGroup()));
        routing.route(UserRemovedFromGroup.class, (message, context) -> withId(message.getGroup()));
    }
}
