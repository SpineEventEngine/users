/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user;

import com.google.protobuf.Message;
import io.spine.core.UserId;
import io.spine.testing.server.aggregate.AggregateEventReactionTest;
import io.spine.server.entity.Repository;
import io.spine.users.User;

import static io.spine.users.user.given.UserTestEnv.userId;

/**
 * An implementation base for the {@link User} aggregate event reactors tests.
 *
 * @author Vladyslav Lubenskyi
 */
abstract class UserEventTest<E extends Message>
        extends AggregateEventReactionTest<UserId, E, User, UserAggregate> {

    @Override
    protected final UserId newId() {
        return userId();
    }

    @Override
    protected Repository<UserId, UserAggregate> createEntityRepository() {
        return new UserAggregateRepository();
    }
}
