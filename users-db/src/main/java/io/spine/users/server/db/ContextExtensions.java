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

package io.spine.users.server.db;

import io.spine.server.BoundedContextBuilder;
import io.spine.users.server.UsersContext;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Adds entities implementing database-based user management to
 * {@link io.spine.users.server.UsersContext UsersContext}.
 */
public class ContextExtensions {

    /** Prevents instantiation of this utility class. */
    private ContextExtensions() {
    }

    /**
     * Adds database-related types to the passed instance of the builder of the Users context.
     *
     * @throws IllegalArgumentException
     *         if a builder with another name is passed
     */
    public static BoundedContextBuilder withDatabase(BoundedContextBuilder builder) {
        checkNotNull(builder);
        String passedContextName = builder.name().value();
        checkArgument(UsersContext.NAME.equals(passedContextName),
                      "Wrong context passed for getting extensions: `%s`. Expected: `%s`.",
                      passedContextName, UsersContext.NAME);
        builder.add(UserAccountPart.class)
               .add(GroupAccountPart.class)
               .add(GroupMembersPart.class);
        return builder;
    }
}
