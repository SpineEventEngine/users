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

package io.spine.users.c.group;

import io.spine.core.CommandContext;
import io.spine.server.aggregate.AggregatePart;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.GroupId;

import java.util.List;

/**
 * A group's membership in other groups, a part of the  {@linkplain GroupRoot Group} aggregate.
 *
 * <p>It is forbidden for groups to directly or indirectly join themselves; in other words,
 * all nested group memberships must always form an acyclic graph.
 *
 * <p>Please, see {@link JoinParentGroup} and {@link LeaveParentGroup} commands.
 *
 * @author Vladyslav Lubenskyi
 */
public class GroupMembershipPart
        extends AggregatePart<GroupId, GroupMembership, GroupMembershipVBuilder, GroupRoot> {

    /**
     * Creates a new instance of the aggregate part.
     *
     * @param root a root of the aggregate to which this part belongs
     */
    protected GroupMembershipPart(GroupRoot root) {
        super(root);
    }

    @Assign
    JoinedParentGroup handle(JoinParentGroup command, CommandContext context) {
        return events(context).joinGroup(command);
    }

    @Assign
    LeftParentGroup handle(LeaveParentGroup command, CommandContext context) {
        return events(context).leaveGroup(command);
    }

    @Apply
    void on(JoinedParentGroup event) throws GroupsCanNotFormCycles {
        ensureNoCycles(event);
        getBuilder().addMembership(event.getParentGroupId());
    }

    @Apply
    void on(LeftParentGroup event) {
        removeMembership(event.getParentGroupId());
    }

    private void ensureNoCycles(JoinedParentGroup event) throws GroupsCanNotFormCycles {
        // TODO:2018-09-21:vladyslav.lubenskyi: https://github.com/SpineEventEngine/users/issues/23
    }

    private void removeMembership(GroupId parentGroup) {
        GroupMembershipVBuilder builder = getBuilder();
        List<GroupId> memberships = builder.getMembership();
        if (memberships.contains(parentGroup)) {
            int index = memberships.indexOf(parentGroup);
            builder.removeMembership(index);
        }
    }

    private static GroupEventFactory events(CommandContext context) {
        return GroupEventFactory.instance(context);
    }
}
