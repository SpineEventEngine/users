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

package io.spine.users.group;

import io.spine.core.UserId;
import io.spine.server.event.React;
import io.spine.server.procman.ProcessManager;
import io.spine.users.GroupId;
import io.spine.users.RoleId;
import io.spine.users.group.event.RoleAssignedToGroup;
import io.spine.users.group.event.RoleDisinheritedByUser;
import io.spine.users.group.event.RoleInheritedByUser;
import io.spine.users.group.event.RoleInheritedByUserVBuilder;
import io.spine.users.group.event.RoleUnassignedFromGroup;
import io.spine.users.user.event.UserJoinedGroup;
import io.spine.users.user.event.UserLeftGroup;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * A process manager performing the propagation of group roles.
 *
 * <p>The process manager emits events, which explicitly state the fact of a role propagation.
 */
public class GroupRolesPropagationPm
        extends ProcessManager<GroupId, GroupRolesPropagation, GroupRolesPropagationVBuilder> {

    protected GroupRolesPropagationPm(GroupId id) {
        super(id);
    }

    @React
    Collection<RoleInheritedByUser> on(UserJoinedGroup event) {
        UserId newMember = event.getId();
        GroupId groupId = event.getGroupId();
        getBuilder().setId(groupId)
                    .addUserMember(newMember);
        List<RoleInheritedByUser> commands = roles()
                .stream()
                .map(role -> roleInherited(groupId, newMember, role))
                .collect(toList());
        return commands;
    }

    @React
    Collection<RoleDisinheritedByUser> on(UserLeftGroup event) {
        UserId leftMember = event.getId();
        GroupId groupId = event.getGroupId();
        removeMember(leftMember);
        List<RoleDisinheritedByUser> commands = roles()
                .stream()
                .map(role -> roleDisinherited(groupId, leftMember, role))
                .collect(toList());
        return commands;
    }

    @React
    Collection<RoleInheritedByUser> on(RoleAssignedToGroup event) {
        RoleId assignedRole = event.getRoleId();
        GroupId groupId = event.getId();
        getBuilder().setId(groupId)
                    .addRole(assignedRole);
        List<RoleInheritedByUser> commands = members()
                .stream()
                .map(member -> roleInherited(groupId, member, assignedRole))
                .collect(toList());
        return commands;
    }

    @React
    List<RoleDisinheritedByUser> on(RoleUnassignedFromGroup event) {
        RoleId unassignedRole = event.getRoleId();
        GroupId groupId = event.getId();
        removeRole(unassignedRole);
        List<RoleDisinheritedByUser> commands = members()
                .stream()
                .map(member -> roleDisinherited(groupId, member, unassignedRole))
                .collect(toList());
        return commands;
    }

    private void removeMember(UserId member) {
        List<UserId> members = getBuilder().getUserMember();
        int memberIndex = members.indexOf(member);
        getBuilder().removeUserMember(memberIndex);
    }

    private void removeRole(RoleId role) {
        List<RoleId> roles = getBuilder().getRole();
        int roleIndex = roles.indexOf(role);
        getBuilder().removeRole(roleIndex);
    }

    private List<UserId> members() {
        return getBuilder().getUserMember();
    }

    /**
     * Obtains the roles currently assigned to the group.
     */
    private List<RoleId> roles() {
        return getBuilder().getRole();
    }

    private static RoleInheritedByUser roleInherited(GroupId group,
                                                     UserId member,
                                                     RoleId role) {
        return RoleInheritedByUserVBuilder
                .newBuilder()
                .setId(group)
                .setRoleId(role)
                .setUserId(member)
                .build();
    }

    private static RoleDisinheritedByUser roleDisinherited(GroupId group,
                                                           UserId member,
                                                           RoleId role) {
        return RoleDisinheritedByUser
                .newBuilder()
                .setId(group)
                .setRoleId(role)
                .setUserId(member)
                .build();
    }
}
