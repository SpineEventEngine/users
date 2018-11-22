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

import io.spine.core.EventContext;
import io.spine.server.aggregate.Aggregate;
import io.spine.server.event.Enricher;
import io.spine.users.c.group.RoleIds;
import io.spine.users.c.group.RoleNames;
import io.spine.users.c.group.RoleNamesVBuilder;
import io.spine.users.c.role.Role;
import io.spine.users.c.role.RoleAggregate;
import io.spine.users.c.role.RoleAggregateRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

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
        Enricher enricher = Enricher.newBuilder()
                                    .add(RoleId.class, RoleName.class,
                                         roleNameLookup(roleAggregateRepository))
                                    .add(RoleIds.class, RoleNames.class,
                                         roleNamesLookup(roleAggregateRepository))
                                    .build();
        return enricher;
    }

    private static BiFunction<RoleId, EventContext, RoleName>
    roleNameLookup(RoleAggregateRepository repository) {
        return (roleId, context) -> findName(repository, roleId);
    }

    private static BiFunction<RoleIds, EventContext, RoleNames>
    roleNamesLookup(RoleAggregateRepository repository) {
        return (roleIds, context) -> findNames(repository, roleIds);
    }

    private static RoleName findName(RoleAggregateRepository repository, RoleId id) {
        Optional<RoleAggregate> role = repository.find(id);
        String name = role.map(Aggregate::getState)
                          .map(Role::getDisplayName)
                          .orElse("");
        return RoleNameVBuilder.newBuilder()
                               .setId(id)
                               .setName(name)
                               .build();
    }

    private static RoleNames findNames(RoleAggregateRepository repository, RoleIds roleIds) {
        List<RoleName> names = roleIds.getIdList()
                                      .stream()
                                      .map(id -> findName(repository, id))
                                      .collect(Collectors.toList());
        return RoleNamesVBuilder.newBuilder()
                                .addAllName(names)
                                .build();
    }
}
