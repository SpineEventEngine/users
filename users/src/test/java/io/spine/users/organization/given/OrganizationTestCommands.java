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

package io.spine.users.organization.given;

import io.spine.users.OrganizationId;
import io.spine.users.organization.OrganizationAggregate;
import io.spine.users.organization.command.ChangeOrganizationDomain;
import io.spine.users.organization.command.ChangeOrganizationDomainVBuilder;
import io.spine.users.organization.command.ChangeOrganizationTenant;
import io.spine.users.organization.command.ChangeOrganizationTenantVBuilder;
import io.spine.users.organization.command.CreateOrganization;
import io.spine.users.organization.command.CreateOrganizationVBuilder;
import io.spine.users.organization.command.DeleteOrganization;
import io.spine.users.organization.command.DeleteOrganizationVBuilder;
import io.spine.users.organization.command.RenameOrganization;
import io.spine.users.organization.command.RenameOrganizationVBuilder;

import static io.spine.users.organization.given.OrganizationTestEnv.newOrgDomain;
import static io.spine.users.organization.given.OrganizationTestEnv.newOrgTenant;
import static io.spine.users.organization.given.OrganizationTestEnv.orgDomain;
import static io.spine.users.organization.given.OrganizationTestEnv.orgName;
import static io.spine.users.organization.given.OrganizationTestEnv.orgNewName;
import static io.spine.users.organization.given.OrganizationTestEnv.orgTenant;

/**
 * Test commands for {@link OrganizationAggregate}.
 *
 * @author Vladyslav Lubenskyi
 */
public final class OrganizationTestCommands {

    /**
     * Prevents instantiation.
     */
    private OrganizationTestCommands() {
    }

    public static CreateOrganization createOrganization(OrganizationId id) {
        return CreateOrganizationVBuilder.newBuilder()
                                         .setId(id)
                                         .setDisplayName(orgName())
                                         .setDomain(orgDomain())
                                         .setTenant(orgTenant())
                                         .build();

    }

    public static DeleteOrganization deleteOrganization(OrganizationId id) {
        return DeleteOrganizationVBuilder.newBuilder()
                                         .setId(id)
                                         .build();
    }

    public static ChangeOrganizationDomain changeOrganizationDomain(OrganizationId id) {
        return ChangeOrganizationDomainVBuilder.newBuilder()
                                               .setId(id)
                                               .setNewDomain(newOrgDomain())
                                               .build();
    }

    public static ChangeOrganizationTenant changeOrganizationTenant(OrganizationId id) {
        return ChangeOrganizationTenantVBuilder.newBuilder()
                                               .setId(id)
                                               .setNewTenant(newOrgTenant())
                                               .build();
    }

    public static RenameOrganization renameOrganization(OrganizationId id) {
        return RenameOrganizationVBuilder.newBuilder()
                                         .setId(id)
                                         .setNewName(orgNewName())
                                         .build();
    }
}
