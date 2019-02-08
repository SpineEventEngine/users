/*
 * Copyright 2019, TeamDev. All rights reserved.
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

import io.spine.testing.server.entity.given.Given;
import io.spine.users.GroupId;
import io.spine.users.group.Group;
import io.spine.users.group.GroupMembership;
import io.spine.users.group.GroupMembershipVBuilder;
import io.spine.users.group.GroupVBuilder;

import static io.spine.users.server.group.given.GroupTestEnv.groupDescription;
import static io.spine.users.server.group.given.GroupTestEnv.groupEmail;
import static io.spine.users.server.group.given.GroupTestEnv.groupName;
import static io.spine.users.server.group.given.GroupTestEnv.groupOrgEntityOrganization;
import static io.spine.users.server.group.given.GroupTestEnv.groupRole;

/**
 * A factory for creating test {@linkplain GroupPart Group aggregates}.
 *
 * @author Vladyslav Lubenskyi
 */
final class TestGroupFactory {

    /**
     * Prevents instantiation.
     */
    private TestGroupFactory() {
    }

    /**
     * Creates a new instance of the aggregate with the default state.
     */
    static GroupPart createEmptyGroupPart(GroupRoot root) {
        return new GroupPart(root);
    }

    static GroupMembershipPart createEmptyMembershipPart(GroupRoot root) {
        return new GroupMembershipPart(root);
    }

    /**
     * Creates a new instance of the {@link GroupPart} with the filled state.
     */
    static GroupPart createGroupPart(GroupRoot root) {
        return groupPart(groupState(root.getId()).build(), root);
    }

    /**
     * Creates a new instance of the {@link GroupMembershipPart} with the filled state.
     */
    static GroupMembershipPart createMembershipPart(GroupRoot root) {
        return membershipPart(membershipState(root.getId()).build(), root);
    }

    private static GroupPart groupPart(Group state, GroupRoot root) {
        return Given.aggregatePartOfClass(GroupPart.class)
                    .withRoot(root)
                    .withState(state)
                    .withId(state.getId())
                    .build();
    }

    private static GroupMembershipPart membershipPart(GroupMembership state, GroupRoot root) {
        return Given.aggregatePartOfClass(GroupMembershipPart.class)
                    .withRoot(root)
                    .withState(state)
                    .withId(state.getId())
                    .build();
    }

    private static GroupVBuilder groupState(GroupId id) {
        return GroupVBuilder.newBuilder()
                            .setId(id)
                            .setOrgEntity(groupOrgEntityOrganization())
                            .setDisplayName(groupName())
                            .setEmail(groupEmail())
                            .setDescription(groupDescription())
                            .addRole(groupRole());
    }

    private static GroupMembershipVBuilder membershipState(GroupId id) {
        return GroupMembershipVBuilder.newBuilder()
                                      .setId(id);
    }
}
