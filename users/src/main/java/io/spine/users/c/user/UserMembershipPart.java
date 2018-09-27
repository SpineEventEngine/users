/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user;

import io.spine.core.CommandContext;
import io.spine.core.UserId;
import io.spine.server.aggregate.Aggregate;
import io.spine.server.aggregate.AggregatePart;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.GroupId;
import io.spine.users.c.group.GroupRoot;
import io.spine.users.c.user.UserMembership.UserMembershipRecord;

import java.util.Optional;

/**
 * A user membership in multiple {@linkplain GroupRoot groups}, a part of {@linkplain UserRoot User}
 * aggregate.
 *
 * <p>If a user shares its functions and roles with a number of other users they can join
 * one or more groups (please, see {@link JoinGroup}, {@link LeaveGroup} commands).
 *
 * @author Vladyslav Lubenskyi
 */
public class UserMembershipPart
        extends AggregatePart<UserId, UserMembership, UserMembershipVBuilder, UserRoot> {

    /**
     * @see Aggregate#Aggregate(Object)
     */
    UserMembershipPart(UserRoot root) {
        super(root);
    }

    @Assign
    UserJoinedGroup handle(JoinGroup command, CommandContext context) {
        return events().joinGroup(command);
    }

    @Assign
    UserLeftGroup handle(LeaveGroup command, CommandContext context) {
        return events().leaveGroup(command);
    }

    @Apply
    void on(UserJoinedGroup event) {
        UserMembershipRecord membershipRecord =
                UserMembershipRecord.newBuilder()
                                    .setGroupId(event.getGroupId())
                                    .setRole(event.getRole())
                                    .build();
        getBuilder().addMembership(membershipRecord);

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

    private static UserEventFactory events() {
        return UserEventFactory.instance();
    }
}
