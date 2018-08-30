/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user.given;

import io.spine.testing.server.entity.given.Given;
import io.spine.users.User;
import io.spine.users.UserVBuilder;
import io.spine.users.user.UserAggregate;

import static io.spine.users.User.Status.NOT_READY;
import static io.spine.users.user.given.UserTestEnv.adminRoleId;
import static io.spine.users.user.given.UserTestEnv.attributeName;
import static io.spine.users.user.given.UserTestEnv.attributeValue;
import static io.spine.users.user.given.UserTestEnv.displayName;
import static io.spine.users.user.given.UserTestEnv.firstGroupId;
import static io.spine.users.user.given.UserTestEnv.googleIdentity;
import static io.spine.users.user.given.UserTestEnv.parentEntity;
import static io.spine.users.user.given.UserTestEnv.profile;
import static io.spine.users.user.given.UserTestEnv.userId;

/**
 * A factory for creating test {@linkplain UserAggregate User aggregates}.
 *
 * @author Vladyslav Lubenskyi
 */
public class TestAggregateFactory {

    /**
     * Prevents direct instantiation.
     */
    private TestAggregateFactory() {
    }

    /**
     * Creates a new instance of the aggregate with the default state.
     */
    public static UserAggregate createEmptyAggregate() {
        return new UserAggregate(userId());
    }

    /**
     * Creates a new instance of the aggregate with the filled state.
     */
    public static UserAggregate createAggregate() {
        return aggregate(state().build());
    }

    /**
     * Creates a new instance of the aggregate with the group membership.
     */
    public static UserAggregate createAggregateWithGroup() {
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
                           .setParentEntity(parentEntity())
                           .setDisplayName(displayName())
                           .setPrimaryAuthIdentity(googleIdentity())
                           .setProfile(profile())
                           .setStatus(NOT_READY)
                           .addAuthIdentity(googleIdentity())
                           .putAttributes(attributeName(), attributeValue())
                           .addRole(adminRoleId());
    }
}
