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

package io.spine.users.q.group.given;

import io.spine.users.c.group.GroupCreated;
import io.spine.users.c.group.GroupCreatedVBuilder;
import io.spine.users.c.group.JoinedParentGroup;
import io.spine.users.c.group.JoinedParentGroupVBuilder;
import io.spine.users.c.group.LeftParentGroup;
import io.spine.users.c.group.LeftParentGroupVBuilder;
import io.spine.users.c.group.RoleAssignedToGroup;
import io.spine.users.c.group.RoleAssignedToGroupVBuilder;
import io.spine.users.c.group.RoleUnassignedFromGroup;
import io.spine.users.c.group.RoleUnassignedFromGroupVBuilder;
import io.spine.users.c.user.UserJoinedGroup;
import io.spine.users.c.user.UserJoinedGroupVBuilder;
import io.spine.users.c.user.UserLeftGroup;
import io.spine.users.c.user.UserLeftGroupVBuilder;
import io.spine.users.q.group.GroupViewProjection;

import static io.spine.users.q.group.given.GroupViewTestEnv.childGroup;
import static io.spine.users.q.group.given.GroupViewTestEnv.email;
import static io.spine.users.q.group.given.GroupViewTestEnv.externalDomain;
import static io.spine.users.q.group.given.GroupViewTestEnv.groupDisplayName;
import static io.spine.users.q.group.given.GroupViewTestEnv.groupId;
import static io.spine.users.q.group.given.GroupViewTestEnv.member;
import static io.spine.users.q.group.given.GroupViewTestEnv.orgEntity;
import static io.spine.users.q.group.given.GroupViewTestEnv.role;
import static io.spine.users.q.group.given.GroupViewTestEnv.roleInGroup;

/**
 * Test events for testing {@link GroupViewProjection}.
 *
 * @author Vladyslav Lubenskyi
 */
public class GroupViewTestEvents {

    /**
     * Prevents instantiation.
     */
    private GroupViewTestEvents() {
    }

    /**
     * Returns a new {@link GroupCreated} event with {@code orgEntity} set.
     */
    public static GroupCreated internalGroupCreated() {
        return GroupCreatedVBuilder.newBuilder()
                                   .setId(groupId())
                                   .setEmail(email())
                                   .setDisplayName(groupDisplayName())
                                   .addRole(role())
                                   .setOrgEntity(orgEntity())
                                   .build();
    }

    /**
     * Returns a new {@link GroupCreated} event with {@code external_domain} set.
     */
    public static GroupCreated externalGroupCreated() {
        return GroupCreatedVBuilder.newBuilder()
                                   .setId(groupId())
                                   .setEmail(email())
                                   .setDisplayName(groupDisplayName())
                                   .addRole(role())
                                   .setExternalDomain(externalDomain())
                                   .build();
    }

    public static JoinedParentGroup joinedParentGroup() {
        return JoinedParentGroupVBuilder.newBuilder()
                                        .setId(childGroup())
                                        .setParentGroupId(groupId())
                                        .build();
    }

    public static LeftParentGroup leftParentGroup() {
        return LeftParentGroupVBuilder.newBuilder()
                                      .setId(childGroup())
                                      .setParentGroupId(groupId())
                                      .build();
    }

    public static RoleAssignedToGroup roleAssignedToGroup() {
        return RoleAssignedToGroupVBuilder.newBuilder()
                                          .setId(groupId())
                                          .setRoleId(role())
                                          .build();
    }

    public static RoleUnassignedFromGroup roleUnassignedFromGroup() {
        return RoleUnassignedFromGroupVBuilder.newBuilder()
                                              .setId(groupId())
                                              .setRoleId(role())
                                              .build();
    }

    public static UserJoinedGroup userJoinedGroup() {
        return UserJoinedGroupVBuilder.newBuilder()
                                      .setGroupId(groupId())
                                      .setId(member())
                                      .setRole(roleInGroup())
                                      .build();
    }

    public static UserLeftGroup userLeftGroup() {
        return UserLeftGroupVBuilder.newBuilder()
                                    .setGroupId(groupId())
                                    .setId(member())
                                    .build();
    }
}
