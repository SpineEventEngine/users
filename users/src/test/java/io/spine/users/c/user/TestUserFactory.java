/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import io.spine.testing.server.entity.given.Given;

import static io.spine.users.c.user.User.Status.NOT_READY;
import static io.spine.users.c.user.UserNature.PERSON;
import static io.spine.users.c.user.given.UserTestEnv.adminRoleId;
import static io.spine.users.c.user.given.UserTestEnv.googleIdentity;
import static io.spine.users.c.user.given.UserTestEnv.profile;
import static io.spine.users.c.user.given.UserTestEnv.userDisplayName;
import static io.spine.users.c.user.given.UserTestEnv.userId;
import static io.spine.users.c.user.given.UserTestEnv.userOrgEntity;

/**
 * A factory for creating test {@linkplain UserPart User aggregates}.
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
     * Creates a new instance of the {@link UserPart} with the filled state.
     */
    static UserPart createUserPart(UserRoot root) {
        return userPart(userPartState().build(), root);
    }

    /**
     * Creates a new instance of the {@link UserMembershipPart} with the filled state.
     */
    static UserMembershipPart createMembershipPart(UserRoot root) {
        return membershipPart(membershipState().build(), root);
    }

    private static UserPart userPart(User state, UserRoot root) {
        return Given.aggregatePartOfClass(UserPart.class)
                    .withRoot(root)
                    .withState(state)
                    .withId(userId())
                    .build();
    }

    private static UserMembershipPart membershipPart(UserMembership state, UserRoot root) {
        return Given.aggregatePartOfClass(UserMembershipPart.class)
                    .withRoot(root)
                    .withState(state)
                    .withId(userId())
                    .build();
    }

    private static UserVBuilder userPartState() {
        return UserVBuilder.newBuilder()
                           .setId(userId())
                           .setOrgEntity(userOrgEntity())
                           .setDisplayName(userDisplayName())
                           .setPrimaryIdentity(googleIdentity())
                           .setProfile(profile())
                           .setStatus(NOT_READY)
                           .addSecondaryIdentity(googleIdentity())
                           .setNature(PERSON)
                           .addRole(adminRoleId());
    }

    private static UserMembershipVBuilder membershipState() {
        return UserMembershipVBuilder.newBuilder()
                                     .setId(userId());
    }

    static UserMembershipPart createEmptyMembershipPart(UserRoot root) {
        return new UserMembershipPart(root);
    }

    static UserPart createEmptyUserPart(UserRoot root) {
        return new UserPart(root);
    }
}
