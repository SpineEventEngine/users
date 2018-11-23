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

package io.spine.users.q.user;

import io.spine.core.Enrichments;
import io.spine.core.EventContext;
import io.spine.core.Subscribe;
import io.spine.core.UserId;
import io.spine.server.projection.Projection;
import io.spine.users.GroupId;
import io.spine.users.RoleId;
import io.spine.users.RoleName;
import io.spine.users.c.role.RoleAssignmentEnrichment;
import io.spine.users.c.user.RoleAssignedToUser;
import io.spine.users.c.user.RoleUnassignedFromUser;
import io.spine.users.c.user.UserCreated;
import io.spine.users.c.user.UserLeftGroup;
import io.spine.users.q.group.GroupView;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * A projection of all user roles (both explicitly and implicitly assigned).
 */
public class UserRolesProjection extends Projection<UserId, UserRoles, UserRolesVBuilder> {

    protected UserRolesProjection(UserId id) {
        super(id);
    }

    @Subscribe
    public void on(UserCreated event) {
        UserId userId = event.getId();
        getBuilder().setId(userId);
    }

    /**
     * Updates roles of a group using the state update of {@link GroupView}.
     */
    @Subscribe
    public void onUpdate(GroupView group) {
        removeGroupRoles(group.getId());
        boolean groupHasRoles = !group.getRoleList()
                                      .isEmpty();
        if (groupHasRoles) {
            GroupRoles updatedRoles = rolesOf(group);
            getBuilder().addGroupRole(updatedRoles);
        }
    }

    /**
     * Removes the roles for the left group.
     *
     * @implNote this subscription is required because {@link GroupView} update
     *           won't be routed if a user left a group
     */
    @Subscribe
    public void on(UserLeftGroup event) {
        GroupId groupId = event.getGroupId();
        removeGroupRoles(groupId);
    }

    @Subscribe
    public void on(RoleAssignedToUser event, EventContext context) {
        RoleName role = roleEnrichment(context).getRoleName();
        getBuilder().addRole(role);
    }

    @Subscribe
    public void on(RoleUnassignedFromUser event) {
        removeRole(event.getRoleId());
    }

    private void removeGroupRoles(GroupId groupId) {
        Optional<GroupRoles> roles = findRoles(groupId);
        roles.ifPresent(this::removeGroupRoles);
    }

    private void removeRole(RoleId roleId) {
        List<RoleName> updatedRoles = getBuilder().getRole()
                                                  .stream()
                                                  .filter(role -> !role.getId()
                                                                       .equals(roleId))
                                                  .collect(toList());
        getBuilder().clearRole()
                    .addAllRole(updatedRoles);

    }

    private Optional<GroupRoles> findRoles(GroupId groupId) {
        Optional<GroupRoles> record =
                getBuilder().getGroupRole()
                            .stream()
                            .filter(roles -> roles.getGroup()
                                                  .equals(groupId))
                            .findFirst();
        return record;
    }

    private void removeGroupRoles(GroupRoles roles) {
        int index = getBuilder().getGroupRole()
                                .indexOf(roles);
        getBuilder().removeGroupRole(index);
    }

    private static GroupRoles rolesOf(GroupView group) {
        GroupRoles roles = GroupRolesVBuilder.newBuilder()
                                             .setGroup(group.getId())
                                             .addAllRole(group.getRoleList())
                                             .build();
        return roles;
    }

    private static RoleAssignmentEnrichment roleEnrichment(EventContext context) {
        RoleAssignmentEnrichment enrichment =
                Enrichments.getEnrichment(RoleAssignmentEnrichment.class, context)
                           .orElseThrow(IllegalStateException::new);
        return enrichment;
    }
}
