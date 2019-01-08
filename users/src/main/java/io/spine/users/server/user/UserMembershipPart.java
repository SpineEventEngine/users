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

package io.spine.users.server.user;

import io.spine.core.UserId;
import io.spine.server.aggregate.AggregatePart;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.GroupId;
import io.spine.users.user.UserMembership;
import io.spine.users.user.UserMembershipRecord;
import io.spine.users.user.UserMembershipVBuilder;
import io.spine.users.user.command.JoinGroup;
import io.spine.users.user.command.LeaveGroup;
import io.spine.users.user.event.UserJoinedGroup;
import io.spine.users.user.event.UserJoinedGroupVBuilder;
import io.spine.users.user.event.UserLeftGroup;
import io.spine.users.user.event.UserLeftGroupVBuilder;

import java.util.Optional;

import static io.spine.users.user.RoleInGroup.MEMBER;

/**
 * A user membership in multiple {@linkplain io.spine.users.server.group.GroupRoot groups},
 * a part of {@linkplain UserRoot User} aggregate.
 *
 * <p>If a user shares its functions and roles with a number of other users they can join
 * one or more groups (please see {@link JoinGroup}, {@link LeaveGroup} commands).
 */
public class UserMembershipPart
        extends AggregatePart<UserId, UserMembership, UserMembershipVBuilder, UserRoot> {

    UserMembershipPart(UserRoot root) {
        super(root);
    }

    @Assign
    UserJoinedGroup handle(JoinGroup command) {
        UserJoinedGroup event =
                UserJoinedGroupVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setGroupId(command.getGroupId())
                        .setRole(MEMBER)
                        .build();
        return event;
    }

    @Assign
    UserLeftGroup handle(LeaveGroup command) {
        UserLeftGroup event =
                UserLeftGroupVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setGroupId(command.getGroupId())
                        .build();
        return event;
    }

    @Apply
    void on(UserJoinedGroup event) {
        UserMembershipRecord membershipRecord =
                UserMembershipRecord
                        .newBuilder()
                        .setGroupId(event.getGroupId())
                        .setRole(event.getRole())
                        .build();
        getBuilder()
                .setId(getId())
                .addMembership(membershipRecord);
    }

    @Apply
    void on(UserLeftGroup event) {
        Optional<UserMembershipRecord> membership = findMembership(event.getGroupId());
        membership.ifPresent(this::removeMembership);
    }

    private void removeMembership(UserMembershipRecord record) {
        int index = getBuilder().getMembership()
                                .indexOf(record);
        getBuilder().removeMembership(index);
    }

    private Optional<UserMembershipRecord> findMembership(GroupId groupId) {
        Optional<UserMembershipRecord> record =
                getBuilder().getMembership()
                            .stream()
                            .filter(membership -> membership.getGroupId()
                                                            .equals(groupId))
                            .findFirst();
        return record;
    }
}
