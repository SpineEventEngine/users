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

package io.spine.users.server.organization;

import io.spine.core.CommandContext;
import io.spine.server.aggregate.Aggregate;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.OrganizationId;
import io.spine.users.organization.Organization;
import io.spine.users.organization.OrganizationVBuilder;
import io.spine.users.organization.command.ChangeOrganizationDomain;
import io.spine.users.organization.command.ChangeOrganizationTenant;
import io.spine.users.organization.command.CreateOrganization;
import io.spine.users.organization.command.DeleteOrganization;
import io.spine.users.organization.command.RenameOrganization;
import io.spine.users.organization.event.OrganizationCreated;
import io.spine.users.organization.event.OrganizationDeleted;
import io.spine.users.organization.event.OrganizationDomainChanged;
import io.spine.users.organization.event.OrganizationRenamed;
import io.spine.users.organization.event.OrganizationTenantChanged;
import io.spine.users.orgunit.OrgUnit;

/**
 * An organization of a tenant, the topmost entity in organizational structure.
 *
 * <p>An organization aggregates users and groups directly or in hierarchy of
 * {@linkplain OrgUnit organizational units}.
 *
 * @author Vladyslav Lubenskyi
 */
public class OrganizationAggregate
        extends Aggregate<OrganizationId, Organization, OrganizationVBuilder> {

    /**
     * @see Aggregate#Aggregate(Object)
     */
    OrganizationAggregate(OrganizationId id) {
        super(id);
    }

    @Assign
    OrganizationCreated handle(CreateOrganization command, CommandContext context) {
        OrganizationCreated event = OrganizationCreated
                .vBuilder()
                .setId(command.getId())
                .setDisplayName(command.getDisplayName())
                .setDomain(command.getDomain())
                .setTenant(command.getTenant())
                .build();
        return event;
    }

    @Assign
    OrganizationDeleted handle(DeleteOrganization command, CommandContext context) {
        OrganizationDeleted event = OrganizationDeleted
                .vBuilder()
                .setId(command.getId())
                .build();
        return event;
    }

    @Assign
    OrganizationRenamed handle(RenameOrganization command, CommandContext context) {
        OrganizationRenamed event = OrganizationRenamed
                .vBuilder()
                .setId(command.getId())
                .setNewName(command.getNewName())
                .setOldName(state().getDisplayName())
                .build();
        return event;
    }

    @Assign
    OrganizationDomainChanged handle(ChangeOrganizationDomain command, CommandContext context) {
        OrganizationDomainChanged event = OrganizationDomainChanged
                .vBuilder()
                .setId(command.getId())
                .setNewDomain(command.getNewDomain())
                .setOldDomain(state().getDomain())
                .build();
        return event;
    }

    @Assign
    OrganizationTenantChanged handle(ChangeOrganizationTenant command, CommandContext context) {
        OrganizationTenantChanged event = OrganizationTenantChanged
                .vBuilder()
                .setId(command.getId())
                .setNewTenant(command.getNewTenant())
                .setOldTenant(state().getTenant())
                .build();
        return event;
    }

    @Apply
    private void on(OrganizationCreated event) {
        builder().setId(event.getId())
                 .setDisplayName(event.getDisplayName())
                 .setDomain(event.getDomain())
                 .setTenant(event.getTenant());
    }

    @Apply
    private void on(@SuppressWarnings("unused") // Event data is not required.
                    OrganizationDeleted event) {
        setDeleted(true);
    }

    @Apply
    private void on(OrganizationRenamed event) {
        builder().setDisplayName(event.getNewName());
    }

    @Apply
    private void on(OrganizationDomainChanged event) {
        builder().setDomain(event.getNewDomain());
    }

    @Apply
    private void on(OrganizationTenantChanged event) {
        builder().setTenant(event.getNewTenant());
    }
}
