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
import io.spine.users.c.organization.OrganizationAggregate;
import io.spine.users.c.orgunit.OrgUnitAggregate;

/**
 * A role that can be assigned to users and groups for access control purposes.
 *
 * <p>A {@link RoleAggregate} reflects a particular function or a functional role of users or/and
 * groups inside of an {@linkplain OrganizationAggregate organization} or
 * {@linkplain OrgUnitAggregate organizational unit}.
 *
 * <h3>Usage examples
 *
 * <ul>
 * <li>{@code Developers} group may have the following roles:
 * {@code github-contributor, test-server-admin};
 * <li>{@code John 'The External Auditor' Smith} user may have the following role:
 * {@code accounting-papers-reader}.
 * </ul>
 *
 * @author Vladyslav Lubenskyi
 */
public class RoleAggregate extends Aggregate<RoleId, Role, RoleVBuilder> {

    /**
     * @see Aggregate#Aggregate(Object) 1
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
        return events(context).changeParent(command, getState().getBelongsTo());
    }

    @Apply
    void on(RoleCreated event) {
        getBuilder().setId(event.getId())
                    .setDisplayName(event.getDisplayName())
                    .setBelongsTo(event.getBelongsTo())
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
        getBuilder().setBelongsTo(event.getNewParent());
    }

    private static RoleEventFactory events(CommandContext context) {
        return RoleEventFactory.instance(context);
    }
}
