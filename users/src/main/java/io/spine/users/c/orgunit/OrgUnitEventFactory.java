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

package io.spine.users.c.orgunit;

import io.spine.net.InternetDomain;
import io.spine.users.OrganizationOrUnit;

/**
 * An event factory for the {@linkplain OrgUnit OrgUnit aggregate}.
 *
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings("OverlyCoupledClass") // It is OK for event factory.
final class OrgUnitEventFactory {

    /**
     * Prevents direct instantiation.
     */
    private OrgUnitEventFactory() {

    }
    /**
     * Retrieves an instance of {@link OrgUnitEventFactory}.
     */
    static OrgUnitEventFactory instance() {
        return new OrgUnitEventFactory();
    }

    OrgUnitCreated createOrgUnit(CreateOrgUnit command) {
        return OrgUnitCreatedVBuilder
                .newBuilder()
                .setId(command.getId())
                .setDisplayName(command.getDisplayName())
                .setDomain(command.getDomain())
                .setParentEntity(command.getParentEntity())
                .build();
    }

    OrgUnitDeleted deleteOrgUnit(DeleteOrgUnit command) {
        return OrgUnitDeletedVBuilder
                .newBuilder()
                .setId(command.getId())
                .build();
    }

    OrgUnitMoved moveOrgUnit(MoveOrgUnit command, OrganizationOrUnit oldParent) {
        return OrgUnitMovedVBuilder
                .newBuilder()
                .setId(command.getId())
                .setNewParentEntity(command.getNewParentEntity())
                .setOldParentEntity(oldParent)
                .build();
    }

    OrgUnitRenamed rename(RenameOrgUnit command, String oldName) {
        return OrgUnitRenamedVBuilder
                .newBuilder()
                .setId(command.getId())
                .setNewName(command.getNewName())
                .setOldName(oldName)
                .build();
    }

    OrgUnitDomainChanged changeDomain(ChangeOrgUnitDomain command, InternetDomain oldDomain) {
        return OrgUnitDomainChangedVBuilder
                .newBuilder()
                .setId(command.getId())
                .setNewDomain(command.getNewDomain())
                .setOldDomain(oldDomain)
                .build();
    }
}
