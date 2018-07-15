/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin.given;

import io.spine.users.signin.RemoteIdentitySignInProcMan;
import io.spine.users.signin.identity.UserDetailsFetched;
import io.spine.users.signin.identity.UserStatusChecked;
import io.spine.users.signin.identity.UserStatusCheckedVBuilder;
import io.spine.users.user.UserCreated;
import io.spine.users.user.UserCreatedVBuilder;

import static io.spine.time.OffsetDateTimes.now;
import static io.spine.time.ZoneOffsets.utc;
import static io.spine.users.User.Status.ACTIVE;
import static io.spine.users.User.Status.SUSPENDED;
import static io.spine.users.signin.given.SignInTestEnv.*;

/**
 * Test events for {@link RemoteIdentitySignInProcMan}.
 *
 * @author Vladyslav Lubenskyi
 */
public class SignInTestEvents {

    /**
     * Prevents direct instantiation.
     */
    private SignInTestEvents() {
    }

    public static UserDetailsFetched userDetailsFetched() {
        return UserDetailsFetched.newBuilder()
                                 .setId(googleProviderId())
                                 .setUserId(userId())
                                 .setIdentity(identity())
                                 .setProfile(profile())
                                 .addAttribute(attribute())
                                 .build();
    }

    public static UserStatusChecked userStatusChecked(boolean active) {
        return UserStatusCheckedVBuilder.newBuilder()
                                        .setId(googleProviderId())
                                        .setUserId(userId())
                                        .setIdentity(identity())
                                        .setStatus(active ? ACTIVE : SUSPENDED)
                                        .build();
    }

    public static UserCreated userCreated() {
        return UserCreatedVBuilder.newBuilder()
                                  .setId(userId())
                                  .setProfile(profile())
                                  .setStatus(ACTIVE)
                                  .setDisplayName(displayName())
                                  .setPrimaryIdentity(identity())
                                  .setWhenCreated(now(utc()))
                                  .build();
    }
}
