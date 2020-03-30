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

package io.spine.users.server;

import com.google.common.collect.ImmutableList;
import io.spine.base.CommandMessage;
import io.spine.base.EventMessage;
import io.spine.core.UserId;
import io.spine.users.user.User;
import io.spine.users.user.command.CreateUser;

import static io.spine.users.server.user.given.UserTestEnv.profile;
import static io.spine.users.server.user.given.UserTestEnv.userDisplayName;
import static io.spine.users.server.user.given.UserTestEnv.userId;
import static io.spine.users.user.UserNature.PERSON;

/**
 * An implementation base for the {@link User} aggregate command handler tests.
 *
 * @param <C>
 *         the type of the command being tested
 */
public abstract class UserPartCommandTest<C extends CommandMessage, E extends EventMessage>
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

    /**
     * Obtains commands that {@link #context()} should execute in order to set up
     * the test environment for the user.
     */
    protected Iterable<CommandMessage> setupCommands() {
        CreateUser createUser = CreateUser
                .newBuilder()
                .setId(USER_ID)
                .setDisplayName(userDisplayName())
                .setProfile(profile())
                .setNature(PERSON)
                .vBuild();
        return ImmutableList.of(createUser);
    }

    protected void preCreateUser() {
        setupCommands().forEach(context()::receivesCommand);
    }
}
