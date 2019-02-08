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
import io.spine.users.organization.event.OrganizationCreatedVBuilder;
import io.spine.users.organization.event.OrganizationDeleted;
import io.spine.users.organization.event.OrganizationDeletedVBuilder;
import io.spine.users.organization.event.OrganizationDomainChanged;
import io.spine.users.organization.event.OrganizationDomainChangedVBuilder;
import io.spine.users.organization.event.OrganizationRenamed;
import io.spine.users.organization.event.OrganizationRenamedVBuilder;
import io.spine.users.organization.event.OrganizationTenantChanged;
import io.spine.users.organization.event.OrganizationTenantChangedVBuilder;
import io.spine.users.orgunit.OrgUnit;

/**
 * An organization of a tenant, the topmost entity in organizational structure.
 *
 * <p>An organization aggregates users and groups directly or in hierarchy of
 * {@linkplain OrgUnit organizational units}.
 *
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings("OverlyCoupledClass") // It is OK for an aggregate.
public class OrganizationAggregate
        extends Aggregate<OrganizationId, Organization, OrganizationVBuilder> {

    /**
     * @see OrganizationAggregate#OrganizationAggregate(OrganizationId)
     */
    OrganizationAggregate(OrganizationId id) {
        super(id);
    }

    @Assign
    OrganizationCreated handle(CreateOrganization command, CommandContext context) {
        OrganizationCreated event =
                OrganizationCreatedVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setDisplayName(command.getDisplayName())
                        .setDomain(command.getDomain())
                        .setTenant(command.getTenant())
                        .build();
        return event;
    }

    @Assign
    OrganizationDeleted handle(DeleteOrganization command, CommandContext context) {
        OrganizationDeleted event =
                OrganizationDeletedVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .build();
        return event;
    }

    @Assign
    OrganizationRenamed handle(RenameOrganization command, CommandContext context) {
        OrganizationRenamed event =
                OrganizationRenamedVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setNewName(command.getNewName())
                        .setOldName(getState().getDisplayName())
                        .build();
        return event;
    }

    @Assign
    OrganizationDomainChanged handle(ChangeOrganizationDomain command, CommandContext context) {
        OrganizationDomainChanged event =
                OrganizationDomainChangedVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setNewDomain(command.getNewDomain())
                        .setOldDomain(getState().getDomain())
                        .build();
        return event;
    }

    @Assign
    OrganizationTenantChanged handle(ChangeOrganizationTenant command, CommandContext context) {
        OrganizationTenantChanged event =
                OrganizationTenantChangedVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setNewTenant(command.getNewTenant())
                        .setOldTenant(getState().getTenant())
                        .build();
        return event;
    }

    @Apply
    void on(OrganizationCreated event) {
        getBuilder().setId(event.getId())
                    .setDisplayName(event.getDisplayName())
                    .setDomain(event.getDomain())
                    .setTenant(event.getTenant());
    }

    @Apply
    void on(OrganizationDeleted event) {
        setDeleted(true);
    }

    @Apply
    void on(OrganizationRenamed event) {
        getBuilder().setDisplayName(event.getNewName());
    }

    @Apply
    void on(OrganizationDomainChanged event) {
        getBuilder().setDomain(event.getNewDomain());
    }

    @Apply
    void on(OrganizationTenantChanged event) {
        getBuilder().setTenant(event.getNewTenant());
    }
}
