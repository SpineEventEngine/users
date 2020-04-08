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
import io.spine.core.UserId;
import io.spine.server.aggregate.AggregatePart;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.GroupId;
import io.spine.users.group.GroupMembers;
import io.spine.users.group.Member;
import io.spine.users.group.command.AddGroupToGroup;
import io.spine.users.group.command.AddUserToGroup;
import io.spine.users.group.command.RemoveGroupFromGroup;
import io.spine.users.group.command.RemoveUserFromGroup;
import io.spine.users.group.event.GroupAddedToGroup;
import io.spine.users.group.event.GroupRemovedFromGroup;
import io.spine.users.group.event.UserAddedToGroup;
import io.spine.users.group.event.UserRemovedFromGroup;

import java.util.Optional;
import java.util.stream.Stream;

import static io.spine.users.group.Member.KindCase.GROUP;
import static io.spine.users.group.Member.KindCase.USER;

/**
 * Manages adding members to a group.
 */
final class GroupMembersPart
        extends AggregatePart<GroupId, GroupMembers, GroupMembers.Builder, GroupRoot> {

    GroupMembersPart(GroupRoot root) {
        super(root);
    }

    @Assign
    UserAddedToGroup handle(AddUserToGroup command) {
        UserAddedToGroup event = UserAddedToGroup
                .newBuilder()
                .setUser(command.getUser())
                .setGroup(command.getGroup())
                .setRole(command.getRole())
                .vBuild();
        return event;
    }

    @Assign
    UserRemovedFromGroup handle(RemoveUserFromGroup command) {
        //TODO:2020-04-04:alexander.yevsyukov: Throw rejection if a user is not a member.
        UserRemovedFromGroup event = UserRemovedFromGroup
                .newBuilder()
                .setUser(command.getUser())
                .setGroup(command.getGroup())
                .vBuild();
        return event;
    }

    @Assign
    GroupAddedToGroup handle(AddGroupToGroup command, CommandContext context) {
        return GroupAddedToGroup
                .newBuilder()
                .setGroup(command.getGroup())
                .setParentGroup(command.getParentGroup())
                .build();
    }

    @Assign
    GroupRemovedFromGroup handle(RemoveGroupFromGroup command) {
        return GroupRemovedFromGroup
                .newBuilder()
                .setGroup(command.getGroup())
                .setParentGroup(command.getParentGroup())
                .build();
    }

    @Apply
    private void on(UserAddedToGroup event) {
        builder().addMember(
                Member.newBuilder()
                      .setUser(event.getUser())
                      .setRole(event.getRole())
                      .vBuild()
        );
    }

    @Apply
    private void on(UserRemovedFromGroup event) {
        UserId user = event.getUser();
        Optional<Member> member = findMember(user);
        member.ifPresent(this::removeMember);
    }

    @Apply
    private void on(GroupAddedToGroup event) {
        builder().addMember(
                Member.newBuilder()
                      .setGroup(event.getGroup())
                      .setRole(event.getRole())
                      .vBuild()
        );
    }

    @Apply
    private void on(GroupRemovedFromGroup event) {
        GroupId group = event.getGroup();
        Optional<Member> member = findMember(group);
        member.ifPresent(this::removeMember);
    }

    private Optional<Member> findMember(UserId user) {
        Optional<Member> found =
                members().filter(m -> m.getKindCase() == USER)
                         .filter(m -> user.equals(m.getUser()))
                         .findFirst();
        return found;
    }

    private Optional<Member> findMember(GroupId group) {
        Optional<Member> found =
                members().filter(m -> m.getKindCase() == GROUP)
                         .filter(m -> group.equals(m.getGroup()))
                         .findFirst();
        return found;
    }

    private Stream<Member> members() {
        return state().getMemberList()
               .stream();
    }

    private void removeMember(Member member) {
        int index = builder().getMemberList()
                             .indexOf(member);
        builder().removeMember(index);
    }
}

