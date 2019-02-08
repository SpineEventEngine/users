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
import io.spine.core.UserId;
import io.spine.server.entity.Repository;
import io.spine.testing.server.aggregate.AggregateCommandTest;
import io.spine.users.user.UserMembership;

import static io.spine.testing.server.TestBoundedContext.create;
import static io.spine.users.server.user.given.UserTestEnv.userId;

/**
 * An implementation base for the {@link User} aggregate command handler tests.
 *
 * @param <C> the type of the command being tested
 * @author Vladyslav Lubenskyi
 */
abstract class UserMembershipCommandTest<C extends CommandMessage>
        extends AggregateCommandTest<UserId, C, UserMembership, UserMembershipPart> {

    protected static final UserId USER_ID = userId();

    protected UserMembershipCommandTest(C commandMessage) {
        super(USER_ID, commandMessage);
    }

    @Override
    protected Repository<UserId, UserMembershipPart> createRepository() {
        return new UserMembershipPartRepository();
    }

    protected UserMembershipPart createPartWithState() {
        return TestUserFactory.createMembershipPart(root(USER_ID));
    }

    protected UserMembershipPart createPart() {
        return TestUserFactory.createEmptyMembershipPart(root(USER_ID));
    }

    private static UserRoot root(UserId id) {
        return new UserRoot(create(), id);
    }
}
