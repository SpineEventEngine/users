/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.group;

import io.spine.base.CommandMessage;
import io.spine.server.entity.Repository;
import io.spine.testing.server.aggregate.AggregatePartCommandTest;
import io.spine.users.GroupId;

import static io.spine.testing.server.TestBoundedContext.create;
import static io.spine.users.c.group.TestGroupFactory.createEmptyGroupPart;
import static io.spine.users.c.group.TestGroupFactory.createGroupPart;
import static io.spine.users.c.group.given.GroupTestEnv.createGroupId;

/**
 * An implementation base for the {@link Group} aggregate command handler tests.
 *
 * @param <C> the type of the command being tested
 * @author Vladyslav Lubenskyi
 */
abstract class GroupCommandTest<C extends CommandMessage>
        extends AggregatePartCommandTest<GroupId, C, Group, GroupPart, GroupRoot> {

    static final GroupId GROUP_ID = createGroupId();

    GroupCommandTest(C commandMessage) {
        super(GROUP_ID, commandMessage);
    }

    @Override
    protected Repository<GroupId, GroupPart> createEntityRepository() {
        return new GroupPartRepository();
    }

    @Override
    protected GroupRoot newRoot(GroupId id) {
        return new GroupRoot(create(), id);
    }

    @Override
    protected GroupPart newPart(GroupRoot root) {
        return createEmptyGroupPart(root);
    }

    protected GroupPart createPartWithState() {
        return createGroupPart(newRoot(GROUP_ID));
    }
}
