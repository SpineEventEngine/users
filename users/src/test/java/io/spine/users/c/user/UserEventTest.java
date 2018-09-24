/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import com.google.protobuf.Message;
import io.spine.core.UserId;
import io.spine.server.entity.Repository;
import io.spine.testing.server.aggregate.AggregateEventReactionTest;

import static io.spine.users.c.user.given.UserTestEnv.userId;

/**
 * An implementation base for the {@link User} aggregate event reactors tests.
 *
 * @param <E> the type of the event being tested
 * @author Vladyslav Lubenskyi
 */
abstract class UserEventTest<E extends Message>
        extends AggregateEventReactionTest<UserId, E, User, UserAggregate> {

    protected static final UserId USER_ID = userId();

    UserEventTest(E eventMessage) {
        super(USER_ID, eventMessage);
    }

    @Override
    protected Repository<UserId, UserPart> createEntityRepository() {
        return new UserPartRepository();
    }
}
