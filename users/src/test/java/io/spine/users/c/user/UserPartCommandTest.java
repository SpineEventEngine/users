/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import io.spine.base.CommandMessage;
import io.spine.core.UserId;
import io.spine.server.entity.Repository;
import io.spine.testing.server.aggregate.AggregatePartCommandTest;

import static io.spine.testing.server.TestBoundedContext.create;
import static io.spine.users.c.user.TestUserFactory.createEmptyUserPart;
import static io.spine.users.c.user.TestUserFactory.createUserPart;
import static io.spine.users.c.user.given.UserTestEnv.userId;

/**
 * An implementation base for the {@link User} aggregate command handler tests.
 *
 * @param <C> the type of the command being tested
 * @author Vladyslav Lubenskyi
 */
abstract class UserPartCommandTest<C extends CommandMessage>
        extends AggregatePartCommandTest<UserId, C, User, UserPart, UserRoot> {

    protected static final UserId USER_ID = userId();

    protected UserPartCommandTest(C commandMessage) {
        super(USER_ID, commandMessage);
    }

    @Override
    protected Repository<UserId, UserPart> createEntityRepository() {
        return new UserPartRepository();
    }

    @Override
    protected UserRoot newRoot(UserId id) {
        return new UserRoot(create(), id);
    }

    @Override
    protected UserPart newPart(UserRoot root) {
        return createEmptyUserPart(root);
    }

    protected UserPart createPartWithState() {
        return createUserPart(newRoot(USER_ID));
    }
}
