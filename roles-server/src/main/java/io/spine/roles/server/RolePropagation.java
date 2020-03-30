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

package io.spine.roles.server;

import io.spine.core.UserId;
import io.spine.roles.RoleId;
import io.spine.roles.event.RoleAssignedToGroup;
import io.spine.roles.event.RoleAssignmentRemovedFromGroup;
import io.spine.roles.server.event.RolePropagatedToUser;
import io.spine.roles.server.event.RolePropagationCanceledForUser;
import io.spine.server.event.React;
import io.spine.server.procman.ProcessManager;
import io.spine.users.GroupId;
import io.spine.users.group.event.UserJoinedGroup;
import io.spine.users.group.event.UserLeftGroup;

import java.util.Collection;
import java.util.List;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Propagates roles assigned to the group to enclosed users and groups.
 *
 * <p>The process manager emits events, which explicitly state the fact of a role propagation.
 */
final class RolePropagation
        extends ProcessManager<GroupId, GroupRolesPropagation, GroupRolesPropagation.Builder> {

    @React(external = true)
    Collection<RolePropagatedToUser> on(UserJoinedGroup event) {
        UserId newMember = event.getId();
        GroupId group = event.getGroupId();
        builder().setGroup(group)
                 .addUser(newMember);
        List<RolePropagatedToUser> commands = roles()
                .stream()
                .map(role -> rolePropagated(group, newMember, role))
                .collect(toImmutableList());
        return commands;
    }

    @React(external = true)
    Collection<RolePropagationCanceledForUser> on(UserLeftGroup event) {
        UserId leftMember = event.getId();
        GroupId group = event.getGroupId();
        removeUser(leftMember);
        List<RolePropagationCanceledForUser> commands = roles()
                .stream()
                .map(role -> rolePropagationCanceled(group, leftMember, role))
                .collect(toImmutableList());
        return commands;
    }

    @React
    Collection<RolePropagatedToUser> on(RoleAssignedToGroup event) {
        RoleId assignedRole = event.getRole();
        GroupId group = event.getGroup();
        builder().setGroup(group)
                 .addRole(assignedRole);
        List<RolePropagatedToUser> commands = users()
                .stream()
                .map(member -> rolePropagated(group, member, assignedRole))
                .collect(toImmutableList());
        return commands;
    }

    @React
    Collection<RolePropagationCanceledForUser> on(RoleAssignmentRemovedFromGroup event) {
        RoleId roleToRemove = event.getRole();
        GroupId group = event.getGroup();
        removeRole(roleToRemove);
        List<RolePropagationCanceledForUser> commands = users()
                .stream()
                .map(member -> rolePropagationCanceled(group, member, roleToRemove))
                .collect(toImmutableList());
        return commands;
    }

    private void removeUser(UserId member) {
        GroupRolesPropagation.Builder builder = builder();
        List<UserId> users = builder.getUserList();
        int memberIndex = users.indexOf(member);
        builder().removeUser(memberIndex);
    }

    private void removeRole(RoleId role) {
        GroupRolesPropagation.Builder builder = builder();
        List<RoleId> roles = builder.getRoleList();
        int roleIndex = roles.indexOf(role);
        builder.removeRole(roleIndex);
    }

    private List<UserId> users() {
        return state().getUserList();
    }

    /**
     * Obtains the roles currently assigned to the group.
     */
    private List<RoleId> roles() {
        return state().getRoleList();
    }

    private static RolePropagatedToUser
    rolePropagated(GroupId group, UserId member, RoleId role) {
        return RolePropagatedToUser
                .newBuilder()
                .setGroup(group)
                .setRole(role)
                .setUser(member)
                .vBuild();
    }

    private static RolePropagationCanceledForUser
    rolePropagationCanceled(GroupId group, UserId member, RoleId role) {
        return RolePropagationCanceledForUser
                .newBuilder()
                .setGroup(group)
                .setRole(role)
                .setUser(member)
                .vBuild();
    }
}
