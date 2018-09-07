/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.group;

import com.google.protobuf.Message;
import io.spine.server.entity.Repository;
import io.spine.testing.server.aggregate.AggregateCommandTest;
import io.spine.users.GroupId;

import static io.spine.users.c.group.given.GroupTestEnv.newGroupId;

/**
 * An implementation base for the {@link Group} aggregate command handler tests.
 *
 * @param <C>
 *         the type of the command being tested
 * @author Vladyslav Lubenskyi
 */
abstract class GroupCommandTest<C extends Message>
        extends AggregateCommandTest<GroupId, C, Group, GroupAggregate> {

    static final GroupId GROUP_ID = newGroupId();

    GroupCommandTest(C commandMessage) {
        super(GROUP_ID, commandMessage);
    }

    @Override
    protected Repository<GroupId, GroupAggregate> createEntityRepository() {
        return new GroupAggregateRepository();
    }
}
