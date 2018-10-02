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

package io.spine.users.c.organization;

import io.spine.core.CommandContext;
import io.spine.server.aggregate.Aggregate;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.OrganizationId;
import io.spine.users.c.orgunit.OrgUnit;

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
        return events().organizationCreated(command);
    }

    @Assign
    OrganizationDeleted handle(DeleteOrganization command, CommandContext context) {
        return events().organizationDeleted(command);
    }

    @Assign
    OrganizationRenamed handle(RenameOrganization command, CommandContext context) {
        return events().organizationRenamed(command, getState().getDisplayName());
    }

    @Assign
    OrganizationDomainChanged handle(ChangeOrganizationDomain command, CommandContext context) {
        return events().domainChanged(command, getState().getDomain());
    }

    @Assign
    OrganizationTenantChanged handle(ChangeOrganizationTenant command, CommandContext context) {
        return events().tenantChanged(command, getState().getTenant());
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

    private static OrganizationEventFactory events() {
        return OrganizationEventFactory.instance();
    }
}
