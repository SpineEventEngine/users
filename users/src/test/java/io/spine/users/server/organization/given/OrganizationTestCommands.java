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

package io.spine.users.server.organization.given;

import io.spine.users.OrganizationId;
import io.spine.users.organization.command.ChangeOrganizationDomain;
import io.spine.users.organization.command.ChangeOrganizationTenant;
import io.spine.users.organization.command.CreateOrganization;
import io.spine.users.organization.command.DeleteOrganization;
import io.spine.users.organization.command.RenameOrganization;
import io.spine.users.server.organization.OrganizationAggregate;

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
        return CreateOrganization
                .newBuilder()
                .setId(id)
                .setDisplayName(OrganizationTestEnv.orgName())
                .setDomain(OrganizationTestEnv.orgDomain())
                .setTenant(OrganizationTestEnv.orgTenant())
                .build();

    }

    public static DeleteOrganization deleteOrganization(OrganizationId id) {
        return DeleteOrganization
                .newBuilder()
                .setId(id)
                .build();
    }

    public static ChangeOrganizationDomain changeOrganizationDomain(OrganizationId id) {
        return ChangeOrganizationDomain
                .newBuilder()
                .setId(id)
                .setNewDomain(OrganizationTestEnv.newOrgDomain())
                .build();
    }

    public static ChangeOrganizationTenant changeOrganizationTenant(OrganizationId id) {
        return ChangeOrganizationTenant
                .newBuilder()
                .setId(id)
                .setNewTenant(OrganizationTestEnv.newOrgTenant())
                .build();
    }

    public static RenameOrganization renameOrganization(OrganizationId id) {
        return RenameOrganization
                .newBuilder()
                .setId(id)
                .setNewName(OrganizationTestEnv.orgNewName())
                .build();
    }
}
