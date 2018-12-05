/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import io.spine.base.CommandMessage;
import io.spine.core.UserId;
import io.spine.server.entity.Repository;
import io.spine.testing.server.aggregate.AggregateCommandTest;

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
        return createMembershipPart(root(USER_ID));
    }

    protected UserMembershipPart createPart() {
        return createEmptyMembershipPart(root(USER_ID));
    }

    private static UserRoot root(UserId id) {
        return new UserRoot(create(), id);
    }
}
