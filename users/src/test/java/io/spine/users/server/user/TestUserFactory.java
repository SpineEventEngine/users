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

package io.spine.users.server.user;

import io.spine.testing.server.entity.given.Given;
import io.spine.users.user.User;
import io.spine.users.user.UserMembership;
import io.spine.users.user.UserMembershipVBuilder;
import io.spine.users.user.UserVBuilder;

import static io.spine.users.server.user.given.UserTestEnv.adminRoleId;
import static io.spine.users.server.user.given.UserTestEnv.googleIdentity;
import static io.spine.users.server.user.given.UserTestEnv.profile;
import static io.spine.users.server.user.given.UserTestEnv.userDisplayName;
import static io.spine.users.server.user.given.UserTestEnv.userId;
import static io.spine.users.server.user.given.UserTestEnv.userOrgEntity;
import static io.spine.users.user.User.Status.NOT_READY;
import static io.spine.users.user.UserNature.PERSON;

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
