/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import io.spine.testing.server.entity.given.Given;

import static io.spine.users.c.user.User.Status.NOT_READY;
import static io.spine.users.c.user.UserKind.PERSON;
import static io.spine.users.c.user.given.UserTestEnv.adminRoleId;
import static io.spine.users.c.user.given.UserTestEnv.firstGroupId;
import static io.spine.users.c.user.given.UserTestEnv.googleIdentity;
import static io.spine.users.c.user.given.UserTestEnv.profile;
import static io.spine.users.c.user.given.UserTestEnv.userDisplayName;
import static io.spine.users.c.user.given.UserTestEnv.userId;
import static io.spine.users.c.user.given.UserTestEnv.userOrgEntity;

/**
 * A factory for creating test {@linkplain UserAggregate User aggregates}.
 *
 * @author Vladyslav Lubenskyi
 */
final class TestUserFactory {

    /**
     * Prevents instantiation.
     */
    private TestUserFactory() {
    }

    /**
     * Creates a new instance of the aggregate with the default state.
     */
    static UserAggregate createEmptyAggregate() {
        return new UserAggregate(userId());
    }

    /**
     * Creates a new instance of the aggregate with the filled state.
     */
    static UserAggregate createAggregate() {
        return aggregate(state().build());
    }

    /**
     * Creates a new instance of the aggregate with the group membership.
     */
    static UserAggregate createAggregateWithGroup() {
        User state = state().addMembership(firstGroupId())
                            .build();
        return aggregate(state);
    }

    private static UserAggregate aggregate(User state) {
        return Given.aggregateOfClass(UserAggregate.class)
                    .withState(state)
                    .withId(userId())
                    .build();
    }

    private static UserVBuilder state() {
        return UserVBuilder.newBuilder()
                           .setId(userId())
                           .setOrgEntity(userOrgEntity())
                           .setDisplayName(userDisplayName())
                           .setPrimaryIdentity(googleIdentity())
                           .setProfile(profile())
                           .setStatus(NOT_READY)
                           .addSecondaryIdentity(googleIdentity())
                           .setKind(PERSON)
                           .addRole(adminRoleId());
    }
}
