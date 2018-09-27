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

import io.spine.core.Subscribe;
import io.spine.server.projection.Projection;
import io.spine.users.GroupId;
import io.spine.users.c.group.GroupCreated;
import io.spine.users.c.group.JoinedParentGroup;
import io.spine.users.c.group.LeftParentGroup;

import java.util.List;

import static io.spine.users.c.group.GroupCreated.OriginCase.EXTERNAL_DOMAIN;

/**
 * A projection of a single group to be displayed on UI.
 *
 * @author Vladyslav Lubenskyi
 */
public class GroupViewProjection extends Projection<GroupId, GroupView, GroupViewVBuilder> {

    /**
     * @see Projection#Projection(Object)
     */
    protected GroupViewProjection(GroupId id) {
        super(id);
    }

    @Subscribe
    public void on(GroupCreated event) {
        boolean external = event.getOriginCase() == EXTERNAL_DOMAIN;
        GroupViewVBuilder builder = getBuilder();
        builder.setId(event.getId())
               .setDisplayName(event.getDisplayName())
               .setEmail(event.getEmail())
               .addAllRole(event.getRoleList())
               .setExternal(external);
        if (external) {
            builder.setExternalDomain(event.getExternalDomain());
        } else {
            builder.setOrgEntity(event.getOrgEntity());
        }
    }

    @Subscribe
    public void on(JoinedParentGroup event) {
        if (event.getParentGroupId()
                 .equals(getId())) {
            getBuilder().addChildGroups(event.getId());
        }
    }

    @Subscribe
    public void on(LeftParentGroup event) {
        GroupId memberId = event.getId();
        GroupViewVBuilder builder = getBuilder();
        List<GroupId> members = builder.getChildGroups();
        if (members.contains(memberId)) {
            builder.removeChildGroups(members.indexOf(memberId));
        }
    }
}
