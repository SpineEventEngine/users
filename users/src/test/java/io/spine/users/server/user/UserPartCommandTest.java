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

package io.spine.users.server.user;

import io.spine.base.CommandMessage;
import io.spine.base.EventMessage;
import io.spine.core.UserId;
import io.spine.users.server.CommandTest;
import io.spine.users.user.Identity;
import io.spine.users.user.User;
import io.spine.users.user.command.AddSecondaryIdentity;
import io.spine.users.user.command.AssignRoleToUser;
import io.spine.users.user.command.CreateUser;

import static io.spine.testing.server.TestBoundedContext.create;
import static io.spine.users.server.user.given.UserTestEnv.adminRoleId;
import static io.spine.users.server.user.given.UserTestEnv.googleIdentity;
import static io.spine.users.server.user.given.UserTestEnv.profile;
import static io.spine.users.server.user.given.UserTestEnv.userDisplayName;
import static io.spine.users.server.user.given.UserTestEnv.userId;
import static io.spine.users.server.user.given.UserTestEnv.userOrgEntity;
import static io.spine.users.user.User.Status.NOT_READY;
import static io.spine.users.user.UserNature.PERSON;

/**
 * An implementation base for the {@link User} aggregate command handler tests.
 *
 * @param <C>
 *         the type of the command being tested
 * @author Vladyslav Lubenskyi
 */
abstract class UserPartCommandTest<C extends CommandMessage, E extends EventMessage>
        extends CommandTest<UserId, C, E, User, UserPart> {

    private static final UserId USER_ID = userId();

    @Override
    protected UserId entityId() {
        return USER_ID;
    }

    @Override
    protected Class<UserPart> entityClass() {
        return UserPart.class;
    }

    protected void preCreateUser() {
        CreateUser createUser = CreateUser
                .newBuilder()
                .setId(USER_ID)
                .setOrgEntity(userOrgEntity())
                .setDisplayName(userDisplayName())
                .setPrimaryIdentity(googleIdentity())
                .setProfile(profile())
                .setStatus(NOT_READY)
                .setNature(PERSON)
                .vBuild();
        AddSecondaryIdentity addSecondaryIdentity = AddSecondaryIdentity
                .newBuilder()
                .setId(USER_ID)
                .setIdentity(secondaryIdentity())
                .vBuild();
        AssignRoleToUser assignRole = AssignRoleToUser
                .newBuilder()
                .setId(USER_ID)
                .setRoleId(adminRoleId())
                .vBuild();
        context().receivesCommands(createUser, addSecondaryIdentity, assignRole);
    }

    Identity secondaryIdentity() {
        return googleIdentity();
    }

    protected static UserRoot root(UserId id) {
        return new UserRoot(create(), id);
    }
}
