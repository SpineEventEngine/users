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

package io.spine.users.c.group;

import io.spine.testing.server.entity.given.Given;
import io.spine.users.GroupId;

import static io.spine.users.c.group.given.GroupTestEnv.displayName;
import static io.spine.users.c.group.given.GroupTestEnv.groupAttributeName;
import static io.spine.users.c.group.given.GroupTestEnv.groupAttributeValue;
import static io.spine.users.c.group.given.GroupTestEnv.groupEmail;
import static io.spine.users.c.group.given.GroupTestEnv.upperGroupId;
import static io.spine.users.c.group.given.GroupTestEnv.groupOwner;
import static io.spine.users.c.group.given.GroupTestEnv.groupParentOrganization;
import static io.spine.users.c.group.given.GroupTestEnv.groupRole;

/**
 * A factory for creating test {@linkplain GroupAggregate Group aggregates}.
 *
 * @author Vladyslav Lubenskyi
 */
final class TestGroupFactory {

    /**
     * Prevents instantiation.
     */
    private TestGroupFactory() {
    }

    /**
     * Creates a new instance of the aggregate with the default state.
     */
    static GroupAggregate createEmptyAggregate(GroupId id) {
        return new GroupAggregate(id);
    }

    /**
     * Creates a new instance of the aggregate with the filled state.
     */
    static GroupAggregate createAggregate(GroupId id) {
        return aggregate(state(id).build());
    }

    private static GroupAggregate aggregate(Group state) {
        return Given.aggregateOfClass(GroupAggregate.class)
                    .withState(state)
                    .withId(state.getId())
                    .build();
    }

    private static GroupVBuilder state(GroupId id) {
        return GroupVBuilder.newBuilder()
                            .setId(id)
                            .setParentEntity(groupParentOrganization())
                            .setDisplayName(displayName())
                            .setEmail(groupEmail())
                            .setOwner(groupOwner())
                            .putAttributes(groupAttributeName(), groupAttributeValue())
                            .addMembership(upperGroupId())
                            .addRole(groupRole());
    }
}
