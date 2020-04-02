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

import io.spine.core.UserId;
import io.spine.server.aggregate.AggregatePart;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.GroupId;
import io.spine.users.group.Membership;
import io.spine.users.group.UserMembership;
import io.spine.users.group.command.AddUserToGroup;
import io.spine.users.group.command.RemoveUserFromGroup;
import io.spine.users.group.event.UserJoinedGroup;
import io.spine.users.group.event.UserLeftGroup;

import java.util.Optional;


/**
 * A user membership in multiple {@linkplain GroupRoot groups},
 * a part of {@linkplain UserRoot User} aggregate.
 *
 * <p>If a user shares its functions and roles with a number of other users they can join
 * one or more groups (please see {@link AddUserToGroup}, {@link RemoveUserFromGroup} commands).
 */
final class UserMembershipPart
        extends AggregatePart<UserId, UserMembership, UserMembership.Builder, UserRoot> {

    UserMembershipPart(UserRoot root) {
        super(root);
    }

    @Assign
    UserJoinedGroup handle(AddUserToGroup command) {
        UserJoinedGroup event = UserJoinedGroup
                .newBuilder()
                .setUser(command.getUser())
                .setGroup(command.getGroup())
                .setRole(command.getRole())
                .vBuild();
        return event;
    }

    @Assign
    UserLeftGroup handle(RemoveUserFromGroup command) {
        UserLeftGroup event = UserLeftGroup
                .newBuilder()
                .setUser(command.getUser())
                .setGroup(command.getGroup())
                .vBuild();
        return event;
    }

    @Apply
    private void on(UserJoinedGroup event) {
        builder().addMembership(
                Membership
                        .newBuilder()
                        .setGroup(event.getGroup())
                        .setRole(event.getRole())
                        .vBuild()
        );
    }

    @Apply
    private void on(UserLeftGroup event) {
        Optional<Membership> membership = findMembership(event.getGroup());
        membership.ifPresent(this::removeMembership);
    }

    private void removeMembership(Membership record) {
        int index = builder().getMembershipList()
                             .indexOf(record);
        builder().removeMembership(index);
    }

    private Optional<Membership> findMembership(GroupId groupId) {
        Optional<Membership> found =
                builder().getMembershipList()
                         .stream()
                         .filter(membership -> groupId.equals(membership.getGroup()))
                         .findFirst();
        return found;
    }
}
