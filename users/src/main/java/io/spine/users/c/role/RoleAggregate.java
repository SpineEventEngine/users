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

package io.spine.users.c.role;

import io.spine.core.CommandContext;
import io.spine.server.aggregate.Aggregate;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.RoleId;
import io.spine.users.c.group.AssignRoleToGroup;
import io.spine.users.c.group.GroupAggregate;
import io.spine.users.c.user.AssignRoleToUser;
import io.spine.users.c.user.UserAggregate;

/**
 * A role that can be assigned to {@linkplain UserAggregate users} and
 * {@linkplain GroupAggregate groups}, to perform access control.
 *
 * <p>Roles are assigned to {@link UserAggregate users} and {@link GroupAggregate groups} directly
 * and explicitly (please, see {@link AssignRoleToUser} and {@link AssignRoleToGroup} commands).
 *
 * <p>A role exists in the scope of an organization or an orgunit, therefore it can be assigned
 * only to those users and groups that are in the same organization and/or orgunit
 * (or their child orgunits).
 *
 * <h3>Access Control</h3>
 *
 * The roles assigned to a {@linkplain GroupAggregate group} are recursively propagated to all
 * members of the group. This propagation is implicit and is not reflected in aggregate states.
 *
 * <p>Therefore, when carrying out role-based access control, consider that a
 * {@link UserAggregate User} and {@link GroupAggregate Group} aggregates have not only the roles
 * listed in their aggregate states, but effectively all the roles derived from parent groups.
 *
 * @author Vladyslav Lubenskyi
 */
public class RoleAggregate extends Aggregate<RoleId, Role, RoleVBuilder> {

    /**
     * @see Aggregate#Aggregate(Object)
     */
    protected RoleAggregate(RoleId id) {
        super(id);
    }

    @Assign
    RoleCreated handle(CreateRole command, CommandContext context) {
        return events(context).createRole(command);
    }

    @Assign
    RoleDeleted handle(DeleteRole command, CommandContext context) {
        return events(context).deleteRole(command);
    }

    @Assign
    RoleRenamed handle(RenameRole command, CommandContext context) {
        return events(context).renameRole(command, getState().getDisplayName());
    }

    @Assign
    RoleParentChanged handle(ChangeRoleParent command, CommandContext context) {
        return events(context).changeParent(command, getState().getOrgEntity());
    }

    @Apply
    void on(RoleCreated event) {
        getBuilder().setId(event.getId())
                    .setDisplayName(event.getDisplayName())
                    .setOrgEntity(event.getOrgEntity())
                    .build();
    }

    @Apply
    void on(RoleDeleted event) {
        setDeleted(true);
    }

    @Apply
    void on(RoleRenamed event) {
        getBuilder().setDisplayName(event.getNewName());
    }

    @Apply
    void on(RoleParentChanged event) {
        getBuilder().setOrgEntity(event.getNewOrgEntity());
    }

    private static RoleEventFactory events(CommandContext context) {
        return RoleEventFactory.instance(context);
    }
}
