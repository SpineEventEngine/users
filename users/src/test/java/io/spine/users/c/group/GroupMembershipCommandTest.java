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
import static io.spine.users.c.group.TestGroupFactory.createEmptyMembershipPart;
import static io.spine.users.c.group.TestGroupFactory.createMembershipPart;
import static io.spine.users.c.group.given.GroupTestEnv.createGroupId;

/**
 * An implementation base for the {@link Group} aggregate command handler tests.
 *
 * @param <C> the type of the command being tested
 * @author Vladyslav Lubenskyi
 */
abstract class GroupMembershipCommandTest<C extends CommandMessage>
        extends AggregatePartCommandTest<GroupId,
                                         C,
                                         GroupMembership,
                                         GroupMembershipPart,
                                         GroupRoot> {

    static final GroupId GROUP_ID = createGroupId();

    GroupMembershipCommandTest(C commandMessage) {
        super(GROUP_ID, commandMessage);
    }

    @Override
    protected Repository<GroupId, GroupMembershipPart> createEntityRepository() {
        return new GroupMembershipPartRepository();
    }


    @Override
    protected GroupRoot newRoot(GroupId id) {
        return new GroupRoot(create(), id);
    }

    @Override
    protected GroupMembershipPart newPart(GroupRoot root) {
        return createEmptyMembershipPart(root);
    }

    protected GroupMembershipPart createPartWithState() {
        return createMembershipPart(newRoot(GROUP_ID));
    }
}
