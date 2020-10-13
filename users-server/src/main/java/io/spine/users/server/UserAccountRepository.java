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

package io.spine.users.server;

import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
import io.spine.core.UserId;
import io.spine.server.projection.ProjectionRepository;
import io.spine.server.route.EventRouting;
import io.spine.users.UserAccount;
import io.spine.users.event.AccountAware;

import static io.spine.server.route.EventRoute.withId;

/**
 * Tunes event routing to direct {@link AccountAware} events to the managed
 * {@link UserAccountProjection}s.
 */
final class UserAccountRepository
        extends ProjectionRepository<UserId, UserAccountProjection, UserAccount> {

    /**
     * Customizes the routing to deliver {@link AccountAware} events to corresponding projections.
     */
    @OverridingMethodsMustInvokeSuper
    @Override
    protected void setupEventRouting(EventRouting<UserId> routing) {
        super.setupEventRouting(routing);
        routing.route(AccountAware.class, (e, ctx) -> withId(e.getAccount()));
    }
}
