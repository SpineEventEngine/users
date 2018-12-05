/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user;

import io.spine.base.CommandMessage;
import io.spine.core.UserId;
import io.spine.server.entity.Repository;
import io.spine.testing.server.aggregate.AggregateCommandTest;

import static io.spine.testing.server.TestBoundedContext.create;
import static io.spine.users.user.TestUserFactory.createUserPart;
import static io.spine.users.user.given.UserTestEnv.userId;

/**
 * An implementation base for the {@link User} aggregate command handler tests.
 *
 * @param <C> the type of the command being tested
 * @author Vladyslav Lubenskyi
 */
abstract class UserPartCommandTest<C extends CommandMessage>
        extends AggregateCommandTest<UserId, C, User, UserPart> {

    protected static final UserId USER_ID = userId();

    protected UserPartCommandTest(C commandMessage) {
        super(USER_ID, commandMessage);
    }

    @Override
    protected Repository<UserId, UserPart> createRepository() {
        return new UserPartRepository();
    }

    protected UserPart createPartWithState() {
        return createUserPart(root(USER_ID));
    }

    protected static UserRoot root(UserId id) {
        return new UserRoot(create(), id);
    }
}
