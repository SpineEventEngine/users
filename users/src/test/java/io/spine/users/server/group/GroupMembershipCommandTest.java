/*
 * Copyright 2018, TeamDev. All rights reserved.
 *
 * Redistribution and use in source and/or binary forms, with or without
 * modification, must retain the above copyright notice and the following
 * disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.spine.users.server.group;

import io.spine.base.CommandMessage;
import io.spine.server.entity.Repository;
import io.spine.testing.server.aggregate.AggregateCommandTest;
import io.spine.users.GroupId;
import io.spine.users.group.GroupMembership;

import static io.spine.testing.server.TestBoundedContext.create;
import static io.spine.users.server.group.given.GroupTestEnv.createGroupId;

/**
 * An implementation base for the {@link Group} aggregate command handler tests.
 *
 * @param <C> the type of the command being tested
 * @author Vladyslav Lubenskyi
 */
abstract class GroupMembershipCommandTest<C extends CommandMessage>
        extends AggregateCommandTest<GroupId, C, GroupMembership, GroupMembershipPart> {

    static final GroupId GROUP_ID = createGroupId();

    GroupMembershipCommandTest(C commandMessage) {
        super(GROUP_ID, commandMessage);
    }

    @Override
    protected Repository<GroupId, GroupMembershipPart> createRepository() {
        return new GroupMembershipPartRepository();
    }

    protected GroupMembershipPart createPartWithState() {
        return TestGroupFactory.createMembershipPart(root(GROUP_ID));
    }

    private static GroupRoot root(GroupId id) {
        return new GroupRoot(create(), id);
    }
}
