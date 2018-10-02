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

import io.spine.core.TenantId;
import io.spine.net.InternetDomain;

/**
 * An event factory for the {@linkplain Organization Organization aggregate}.
 *
 * @author Vladyslav Lubenskyi
 */
final class OrganizationEventFactory {

    /**
     * Prevents direct instantiation.
     */
    private OrganizationEventFactory() {
    }

    /**
     * Retrieves an instance of {@link OrganizationEventFactory}.
     */
    static OrganizationEventFactory instance() {
        return new OrganizationEventFactory();
    }

    OrganizationCreated createOrganization(CreateOrganization command) {
        return OrganizationCreatedVBuilder
                .newBuilder()
                .setId(command.getId())
                .setDisplayName(command.getDisplayName())
                .setDomain(command.getDomain())
                .setTenant(command.getTenant())
                .build();
    }

    OrganizationDeleted deleteOrganization(DeleteOrganization command) {
        return OrganizationDeletedVBuilder
                .newBuilder()
                .setId(command.getId())
                .build();
    }

    OrganizationRenamed renameOrganization(RenameOrganization command, String oldName) {
        return OrganizationRenamedVBuilder
                .newBuilder()
                .setId(command.getId())
                .setNewName(command.getNewName())
                .setOldName(oldName)
                .build();
    }

    OrganizationDomainChanged changeDomain(ChangeOrganizationDomain command,
                                           InternetDomain oldDomain) {
        return OrganizationDomainChangedVBuilder
                .newBuilder()
                .setId(command.getId())
                .setNewDomain(command.getNewDomain())
                .setOldDomain(oldDomain)
                .build();
    }

    OrganizationTenantChanged changeTenant(ChangeOrganizationTenant command,
                                           TenantId oldTenant) {
        return OrganizationTenantChangedVBuilder
                .newBuilder()
                .setId(command.getId())
                .setNewTenant(command.getNewTenant())
                .setOldTenant(oldTenant)
                .build();
    }
}
