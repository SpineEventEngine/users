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

package io.spine.users.server.role;

import io.spine.server.aggregate.Aggregate;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.RoleId;
import io.spine.users.role.Role;
import io.spine.users.role.RoleVBuilder;
import io.spine.users.role.command.CreateRole;
import io.spine.users.role.command.DeleteRole;
import io.spine.users.role.event.RoleCreated;
import io.spine.users.role.event.RoleCreatedVBuilder;
import io.spine.users.role.event.RoleDeleted;
import io.spine.users.role.event.RoleDeletedVBuilder;

/**
 * A role that can be assigned to {@linkplain io.spine.users.server.user.UserPart users} and
 * {@linkplain io.spine.users.server.group.GroupPart groups}, to perform access control.
 *
 * <p>Roles are assigned to {@link io.spine.users.server.user.UserPart users} and
 * {@link io.spine.users.server.group.GroupPart groups} directly and explicitly (please see
 * {@link io.spine.users.user.command.AssignRoleToUser AssignRoleToUser} and
 * {@link io.spine.users.group.command.AssignRoleToGroup AssignRoleToGroup} commands).
 *
 * <p>A role exists in the scope of an organization or an orgunit, therefore it can be assigned
 * only to those users and groups that are in the same organization and/or orgunit
 * (or their child orgunits).
 *
 * <h1>Access Control</h1>
 *
 * The roles assigned to a {@linkplain io.spine.users.server.group.GroupPart group} are recursively
 * propagated to all members of the group. This propagation is implicit and is not reflected in
 * aggregate states.
 *
 * <p>Therefore, when carrying out role-based access control, consider that a
 * {@link io.spine.users.server.user.UserPart User} and {@link io.spine.users.server.group.GroupPart
 * Group} aggregates have not only the roles listed in their aggregate states, but effectively all
 * the roles derived from parent groups.
 */
public class RoleAggregate extends Aggregate<RoleId, Role, RoleVBuilder> {

    /**
     * @see Aggregate#Aggregate(Object)
     */
    protected RoleAggregate(RoleId id) {
        super(id);
    }

    @Assign
    RoleCreated handle(CreateRole command) {
        RoleCreated event =
                RoleCreatedVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .build();
        return event;
    }

    @Assign
    RoleDeleted handle(DeleteRole command) {
        RoleDeleted event =
                RoleDeletedVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .build();
        return event;
    }

    @Apply
    private void on(RoleCreated event) {
        RoleId id = event.getId();
        builder().setId(id)
                    .setDisplayName(id.getName())
                    .setOrgEntity(id.getOrgEntity())
                    .build();
    }

    @Apply
    private void on(RoleDeleted event) {
        setDeleted(true);
    }
}
