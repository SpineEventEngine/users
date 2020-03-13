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

package io.spine.users.server;

import io.spine.server.BoundedContext;
import io.spine.server.BoundedContextBuilder;

/**
 * A factory of {@code Users} bounded context.
 */
public final class UsersContext {

    /**
     * The name of the context.
     */
    public static final String NAME = "Users";

    /** Prevents instantiation of this static factory. */
    private UsersContext() {
    }

    /**
     * Creates a builder of the {@code Users} Context along with the registered repositories.
     *
     * @return a new instance of {@code BoundedContextBuilder} for this Bounded Context
     */
    public static BoundedContextBuilder newBuilder() {
        BoundedContextBuilder builder = BoundedContext
                .multitenant(NAME)
                .add(UserPart.class)
                .add(UserMembershipPart.class)
                .add(OrgUnitAggregate.class)
                .add(OrganizationAggregate.class)
                .add(GroupAccount.class)
                .add(GroupMembershipPart.class);
        return builder;
    }
}