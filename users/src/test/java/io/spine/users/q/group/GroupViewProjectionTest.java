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

package io.spine.users.q.group;

import com.google.common.collect.ImmutableList;
import io.spine.core.Enrichment;
import io.spine.core.UserId;
import io.spine.users.GroupId;
import io.spine.users.RoleId;
import io.spine.users.RoleName;
import io.spine.users.c.group.GroupCreated;
import io.spine.users.c.group.JoinedParentGroup;
import io.spine.users.c.group.LeftParentGroup;
import io.spine.users.c.group.RoleAssignedToGroup;
import io.spine.users.c.group.RoleUnassignedFromGroup;
import io.spine.users.c.role.RoleAssignmentEnrichment;
import io.spine.users.c.role.RoleAssignmentEnrichmentVBuilder;
import io.spine.users.c.user.UserJoinedGroup;
import io.spine.users.c.user.UserLeftGroup;
import io.spine.users.q.group.given.GroupViewTestEnv;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.spine.users.q.group.GroupViewTestProjections.emptyProjection;
import static io.spine.users.q.group.GroupViewTestProjections.groupAfterCreation;
import static io.spine.users.q.group.GroupViewTestProjections.groupWithChildGroups;
import static io.spine.users.q.group.GroupViewTestProjections.groupWithRole;
import static io.spine.users.q.group.GroupViewTestProjections.groupWithUserMember;
import static io.spine.users.q.group.given.GroupViewTestEnv.expectedRoles;
import static io.spine.users.q.group.given.GroupViewTestEnv.groupId;
import static io.spine.users.q.group.given.GroupViewTestEnv.member;
import static io.spine.users.q.group.given.GroupViewTestEnv.roleNamesEnrichment;
import static io.spine.users.q.group.given.GroupViewTestEvents.externalGroupCreated;
import static io.spine.users.q.group.given.GroupViewTestEvents.internalGroupCreated;
import static io.spine.users.q.group.given.GroupViewTestEvents.joinedParentGroup;
import static io.spine.users.q.group.given.GroupViewTestEvents.leftParentGroup;
import static io.spine.users.q.group.given.GroupViewTestEvents.roleAssignedToGroup;
import static io.spine.users.q.group.given.GroupViewTestEvents.roleUnassignedFromGroup;
import static io.spine.users.q.group.given.GroupViewTestEvents.userJoinedGroup;
import static io.spine.users.q.group.given.GroupViewTestEvents.userLeftGroup;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("GroupView projection should")
class GroupViewProjectionTest {

    @Nested
    @DisplayName("when internal GroupCreated")
    class OnInternalGroupCreated extends GroupViewTest<GroupCreated> {

        OnInternalGroupCreated() {
            super(internalGroupCreated());
        }

        @Test
        @DisplayName("initialize state")
        void testState() {
            expectThat(emptyProjection(groupId())).hasState(state -> {
                assertEquals(message().getDisplayName(), state.getDisplayName());
                assertEquals(message().getEmail(), state.getEmail());
                assertEquals(message().getOrgEntity(), state.getOrgEntity());
                assertEquals(expectedRoles(message()), state.getRoleList());
                assertFalse(state.getExternal());
            });
        }

        @Override
        protected Enrichment enrichment() {
            Enrichment enrichment = super.enrichment();
            return enrichWith(enrichment, roleNamesEnrichment(message()));
        }
    }

    @Nested
    @DisplayName("when external GroupCreated")
    class OnExternalGroupCreated extends GroupViewTest<GroupCreated> {

        OnExternalGroupCreated() {
            super(externalGroupCreated());
        }

        @Test
        @DisplayName("initialize state")
        void testState() {
            expectThat(emptyProjection(groupId())).hasState(state -> {
                assertEquals(message().getDisplayName(), state.getDisplayName());
                assertEquals(message().getEmail(), state.getEmail());
                assertEquals(message().getExternalDomain(), state.getExternalDomain());
                assertEquals(expectedRoles(message()), state.getRoleList());
                assertTrue(state.getExternal());
            });
        }

        @Override
        protected Enrichment enrichment() {
            Enrichment enrichment = super.enrichment();
            return enrichWith(enrichment, roleNamesEnrichment(message()));
        }
    }

    @Nested
    @DisplayName("when a group LeftParentGroup")
    class OnLeftParentGroup extends GroupViewTest<LeftParentGroup> {

        OnLeftParentGroup() {
            super(leftParentGroup());
        }

        @Test
        @DisplayName("parent group should remove a member")
        void testState() {
            expectThat(groupWithChildGroups(groupId())).hasState(state -> {
                List<GroupId> membersList = state.getChildGroupList();
                assertTrue(membersList.isEmpty());
            });
        }
    }

    @Nested
    @DisplayName("when a group JoinedParentGroup")
    class OnJoinedParentGroup extends GroupViewTest<JoinedParentGroup> {

        OnJoinedParentGroup() {
            super(joinedParentGroup());
        }

        @Test
        @DisplayName("parent group should add a member")
        void testState() {
            expectThat(groupAfterCreation(groupId())).hasState(state -> {
                List<GroupId> membersList = state.getChildGroupList();
                assertFalse(membersList.isEmpty());
                assertEquals(membersList.get(0), message().getId());
            });
        }
    }

    @Nested
    @DisplayName("when RoleAssignedToGroup")
    class OnRoleAssignedToGroup extends GroupViewTest<RoleAssignedToGroup> {

        OnRoleAssignedToGroup() {
            super(roleAssignedToGroup());
        }

        @Test
        @DisplayName("add to roles")
        void addRole() {
            List<RoleName> expectedRoles = ImmutableList.of(roleName());
            expectThat(groupAfterCreation(groupId()))
                    .hasState(state -> assertEquals(expectedRoles, state.getRoleList()));
        }

        @Override
        protected Enrichment enrichment() {
            RoleAssignmentEnrichment enrichmentMessage =
                    RoleAssignmentEnrichmentVBuilder.newBuilder()
                                                    .setRoleName(roleName())
                                                    .build();
            Enrichment enrichment = super.enrichment();
            return enrichWith(enrichment, enrichmentMessage);
        }

        private RoleName roleName() {
            RoleId roleId = message().getRoleId();
            return GroupViewTestEnv.roleName(roleId);
        }
    }

    @Nested
    @DisplayName("when RoleUnassignedFromGroup")
    class OnRoleUnassignedFromGroup extends GroupViewTest<RoleUnassignedFromGroup> {

        OnRoleUnassignedFromGroup() {
            super(roleUnassignedFromGroup());
        }

        @Test
        @DisplayName("remove the role")
        void removeRole() {
            expectThat(groupWithRole(groupId()))
                    .hasState(state -> assertTrue(state.getRoleList()
                                                       .isEmpty()));
        }
    }

    @Nested
    @DisplayName("when UserJoinedGroup")
    class OnUserJoinedGroup extends GroupViewTest<UserJoinedGroup> {

        OnUserJoinedGroup() {
            super(userJoinedGroup());
        }

        @Test
        @DisplayName("add to users")
        void addUser() {
            List<UserId> expectedMembers = ImmutableList.of(member());
            expectThat(groupAfterCreation(groupId()))
                    .hasState(state -> assertEquals(expectedMembers, state.getUserMemberList()));
        }
    }

    @Nested
    @DisplayName("when UserLeftGroup")
    class OnUserLeftGroup extends GroupViewTest<UserLeftGroup> {

        OnUserLeftGroup() {
            super(userLeftGroup());
        }

        @Test
        @DisplayName("remove from users")
        void removeUser() {
            expectThat(groupWithUserMember(groupId()))
                    .hasState(state -> assertTrue(state.getUserMemberList()
                                                       .isEmpty()));
        }
    }
}
