/*
 * Copyright 2020, TeamDev. All rights reserved.
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

package io.spine.users.server;

import io.spine.base.CommandMessage;
import io.spine.base.EventMessage;
import io.spine.users.GroupId;
import io.spine.users.group.Group;
import io.spine.users.group.GroupMembership;
import io.spine.users.group.command.JoinParentGroup;
import io.spine.users.server.given.TestIdentifiers;

/**
 * An implementation base for the {@link Group} aggregate command handler tests.
 *
 * @param <C>
 *         the type of the command being tested
 */
public abstract class GroupMembershipCommandTest<C extends CommandMessage, E extends EventMessage>
        extends CommandTest<GroupId, C, E, GroupMembership, GroupMembershipPart> {

    protected static final GroupId GROUP_ID = TestIdentifiers.groupId();
    protected static final GroupId PARENT_GROUP_ID = TestIdentifiers.groupId();

    @Override
    protected GroupId entityId() {
        return GROUP_ID;
    }

    @Override
    protected Class<GroupMembershipPart> entityClass() {
        return GroupMembershipPart.class;
    }

    /**
     * Creates the {@link GroupMembershipPart} with for the group with the {@link #GROUP_ID}
     * identifier and a parent group with  {@link #PARENT_GROUP_ID} identifier.
     */
    protected void preCreateGroupMembership() {
        preCreateGroupMembership(GROUP_ID, PARENT_GROUP_ID);
    }

    /**
     * Creates the {@link GroupAccount} with the some predefined state and the specified identifier.
     */
    protected void preCreateGroupMembership(GroupId group, GroupId parentGroup) {
        JoinParentGroup joinParentGroup = JoinParentGroup
                .newBuilder()
                .setGroup(group)
                .setParentGroup(parentGroup)
                .vBuild();
        context().receivesCommand(joinParentGroup);
    }
}
