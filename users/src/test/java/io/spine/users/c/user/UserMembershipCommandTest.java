/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import com.google.protobuf.Message;
import io.spine.core.UserId;
import io.spine.server.entity.Repository;
import io.spine.testing.server.aggregate.AggregatePartCommandTest;

import static io.spine.testing.server.TestBoundedContext.create;
import static io.spine.users.c.user.TestUserFactory.createEmptyMembershipPart;
import static io.spine.users.c.user.TestUserFactory.createMembershipPart;
import static io.spine.users.c.user.given.UserTestEnv.userId;

/**
 * An implementation base for the {@link User} aggregate command handler tests.
 *
 * @param <C> the type of the command being tested
 * @author Vladyslav Lubenskyi
 */
abstract class UserMembershipCommandTest<C extends Message>
        extends AggregatePartCommandTest<UserId, C, UserMembership, UserMembershipPart, UserRoot> {

    protected static final UserId USER_ID = userId();

    protected UserMembershipCommandTest(C commandMessage) {
        super(USER_ID, commandMessage);
    }

    @Override
    protected Repository<UserId, UserMembershipPart> createEntityRepository() {
        return new UserMembershipPartRepository();
    }

    @Override
    protected UserRoot newRoot(UserId id) {
        return new UserRoot(create(), id);
    }

    @Override
    protected UserMembershipPart newPart(UserRoot root) {
        return createEmptyMembershipPart(root);
    }

    protected UserMembershipPart createPartWithState() {
        return createMembershipPart(newRoot(USER_ID));
    }
}