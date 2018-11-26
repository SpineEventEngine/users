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

package io.spine.users;

import io.spine.server.event.Enricher;
import io.spine.users.c.role.RoleAggregate;
import io.spine.users.c.role.RoleAggregateRepository;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.spine.util.Exceptions.newIllegalStateException;

/**
 * A factory of {@link Enricher} instances for the {@code Users} bounded context.
 */
public class UsersEnricher {

    /** Prevents instantiation of this utility class. */
    private UsersEnricher() {
    }

    /**
     * Creates the new {@link Enricher} for {@code Users} bounded context.
     *
     * @param roleAggregateRepository
     *         repository to find enrichment values in
     * @return new {@link Enricher}
     */
    public static Enricher create(RoleAggregateRepository roleAggregateRepository) {
        checkNotNull(roleAggregateRepository);
        Enricher enricher = Enricher
                .newBuilder()
                .add(RoleId.class, RoleName.class,
                     (roleId, context) -> findName(roleAggregateRepository, roleId))
                .build();
        return enricher;
    }

    private static RoleName findName(RoleAggregateRepository repository, RoleId id) {
        Optional<RoleAggregate> role = repository.find(id);
        if (!role.isPresent()) {
            throw newIllegalStateException("Cannot find the role %s.", id.getValue());
        }
        String name = role.get()
                          .getState()
                          .getDisplayName();
        return RoleNameVBuilder.newBuilder()
                               .setId(id)
                               .setName(name)
                               .build();
    }
}
