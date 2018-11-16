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

package io.spine.users.q.role;

import io.spine.core.Subscribe;
import io.spine.core.UserId;
import io.spine.server.projection.Projection;
import io.spine.users.GroupId;
import io.spine.users.RoleId;
import io.spine.users.c.group.RoleAssignedToGroup;
import io.spine.users.c.group.RoleUnassignedFromGroup;
import io.spine.users.c.role.RoleCreated;
import io.spine.users.c.role.RoleDeleted;
import io.spine.users.c.role.RoleParentChanged;
import io.spine.users.c.role.RoleRenamed;
import io.spine.users.c.user.RoleAssignedToUser;
import io.spine.users.c.user.RoleUnassignedFromUser;

import java.util.List;

/**
 * A projection of a {@link io.spine.users.c.role.RoleAggregate RoleAggregate}.
 */
public class RoleViewProjection extends Projection<RoleId, RoleView, RoleViewVBuilder> {

    protected RoleViewProjection(RoleId id) {
        super(id);
    }

    @Subscribe
    public void on(RoleCreated event) {
        getBuilder().setId(event.getId())
                    .setDisplayName(event.getDisplayName())
                    .setOrgEntity(event.getOrgEntity())
                    .build();
    }

    @Subscribe
    public void on(RoleDeleted event) {
        setDeleted(true);
    }

    @Subscribe
    public void on(RoleRenamed event) {
        getBuilder().setDisplayName(event.getNewName());
    }

    @Subscribe
    public void on(RoleParentChanged event) {
        getBuilder().setOrgEntity(event.getNewOrgEntity());
    }

    @Subscribe
    public void on(RoleAssignedToUser event) {
        UserId user = event.getId();
        getBuilder().addUser(user);
    }

    @Subscribe
    public void on(RoleUnassignedFromUser event) {
        UserId user = event.getId();
        removeUser(user);
    }

    @Subscribe
    public void on(RoleAssignedToGroup event) {
        GroupId group = event.getId();
        getBuilder().addGroup(group);
    }

    @Subscribe
    public void on(RoleUnassignedFromGroup event) {
        GroupId group = event.getId();
        removeGroup(group);
    }

    private void removeUser(UserId user) {
        List<UserId> users = getBuilder().getUser();
        if (users.contains(user)) {
            int roleIndex = users.indexOf(user);
            getBuilder().removeUser(roleIndex);
        }
    }

    private void removeGroup(GroupId group) {
        List<GroupId> groups = getBuilder().getGroup();
        if (groups.contains(group)) {
            int groupIndex = groups.indexOf(group);
            getBuilder().removeGroup(groupIndex);
        }
    }
}
