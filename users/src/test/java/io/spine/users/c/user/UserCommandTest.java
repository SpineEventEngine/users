/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import com.google.protobuf.Message;
import io.spine.core.UserId;
import io.spine.testing.server.aggregate.AggregateCommandTest;
import io.spine.server.entity.Repository;

import static io.spine.users.c.user.given.UserTestEnv.userId;

/**
 * An implementation base for the {@link User} aggregate command handler tests.
 *
 * @param <C> the type of the command being tested
 * @author Vladyslav Lubenskyi
 */
abstract class UserCommandTest<C extends Message>
        extends AggregateCommandTest<UserId, C, User, UserAggregate> {

    protected static final UserId USER_ID = userId();

    protected UserCommandTest(C commandMessage) {
        super(USER_ID, commandMessage);
    }

    @Override
    protected Repository<UserId, UserAggregate> createEntityRepository() {
        return new UserAggregateRepository();
    }
}
