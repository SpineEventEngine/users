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

import io.spine.core.CommandContext;
import io.spine.server.aggregate.AggregatePart;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.GroupId;
import io.spine.users.group.GroupMembership;
import io.spine.users.group.Membership;
import io.spine.users.group.command.AddGroupToGroup;
import io.spine.users.group.command.LeaveParentGroup;
import io.spine.users.group.event.GroupRemovedFromGroup;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A group's membership in other groups, a part of the  {@linkplain GroupRoot Group} aggregate.
 *
 * <p>It is forbidden for groups to directly or indirectly join themselves; in other words,
 * all nested group memberships must always form an acyclic graph.
 *
 * <p>Please see {@link AddGroupToGroup} and {@link LeaveParentGroup} commands.
 */
final class GroupMembershipPart
        extends AggregatePart<GroupId, GroupMembership, GroupMembership.Builder, GroupRoot> {

    /**
     * Creates a new instance of the aggregate part.
     *
     * @param root
     *         a root of the aggregate to which this part belongs
     */
    GroupMembershipPart(GroupRoot root) {
        super(root);
    }


    @Assign
    GroupRemovedFromGroup handle(LeaveParentGroup command, CommandContext context) {
        return GroupRemovedFromGroup
                .newBuilder()
                .setGroup(command.getGroup())
                .setParentGroup(command.getParentGroup())
                .build();
    }


    @Apply
    private void on(GroupRemovedFromGroup event) {
        removeMembership(event.getParentGroup());
    }


    private void removeMembership(GroupId parentGroup) {
        GroupMembership.Builder builder = builder();
        List<Membership> memberships = builder.getMembershipList();
        //TODO:2020-04-02:alexander.yevsyukov: Implement.
        if (memberships.contains(parentGroup)) {
            int index = memberships.indexOf(parentGroup);
            builder.removeMembership(index);
        }
    }
}
