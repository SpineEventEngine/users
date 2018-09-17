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

import com.google.protobuf.Any;
import io.spine.core.ActorContext;
import io.spine.core.CommandContext;
import io.spine.core.TenantId;
import io.spine.core.UserId;
import io.spine.net.InternetDomain;
import io.spine.users.c.EntityEventFactory;

/**
 * An event factory for the {@linkplain Organization Organization aggregate}.
 *
 * @author Vladyslav Lubenskyi
 */
final class OrganizationEventFactory extends EntityEventFactory {

    /**
     * @see EntityEventFactory#EntityEventFactory(ActorContext)
     */
    private OrganizationEventFactory(ActorContext actorContext) {
        super(actorContext);
    }

    /**
     * Retrieves an instance of {@link OrganizationEventFactory}.
     *
     * @param context
     *         the {@link CommandContext} of the command to handle
     * @return new instance of {@link OrganizationEventFactory}
     */
    static OrganizationEventFactory instance(CommandContext context) {
        ActorContext actorContext = context.getActorContext();
        return new OrganizationEventFactory(actorContext);
    }

    OrganizationCreated createOrganization(CreateOrganization command) {
        return OrganizationCreatedVBuilder.newBuilder()
                                          .setId(command.getId())
                                          .setDisplayName(command.getDisplayName())
                                          .setDomain(command.getDomain())
                                          .setOwner(command.getOwner())
                                          .setTenant(command.getTenant())
                                          .putAllAttributes(command.getAttributesMap())
                                          .build();
    }

    OrganizationOwnerChanged changeOwner(ChangeOrganizationOwner command, UserId oldOwner) {
        return OrganizationOwnerChangedVBuilder.newBuilder()
                                               .setId(command.getId())
                                               .setNewOwner(command.getNewOwner())
                                               .setOldOwner(oldOwner)
                                               .build();
    }

    OrganizationDeleted deleteOrganization(DeleteOrganization command) {
        return OrganizationDeletedVBuilder.newBuilder()
                                          .setId(command.getId())
                                          .build();
    }

    OrganizationAttributeAdded addAttribute(AddOrganizationAttribute command) {
        return OrganizationAttributeAddedVBuilder.newBuilder()
                                                 .setId(command.getId())
                                                 .setName(command.getName())
                                                 .setValue(command.getValue())
                                                 .build();
    }

    OrganizationAttributeRemoved removeAttribute(RemoveOrganizationAttribute command,
                                                 Any oldValue) {
        return OrganizationAttributeRemovedVBuilder.newBuilder()
                                                   .setId(command.getId())
                                                   .setName(command.getName())
                                                   .setValue(oldValue)
                                                   .build();
    }

    OrganizationAttributeUpdated updateAttribute(UpdateOrganizationAttribute command,
                                                 Any oldValue) {
        return OrganizationAttributeUpdatedVBuilder.newBuilder()
                                                   .setId(command.getId())
                                                   .setName(command.getName())
                                                   .setNewValue(command.getNewValue())
                                                   .setOldValue(oldValue)
                                                   .build();
    }

    OrganizationRenamed renameOrganization(RenameOrganization command, String oldName) {
        return OrganizationRenamedVBuilder.newBuilder()
                                          .setId(command.getId())
                                          .setNewName(command.getNewName())
                                          .setOldName(oldName)
                                          .build();
    }

    OrganizationDomainChanged changeDomain(ChangeOrganizationDomain command,
                                           InternetDomain oldDomain) {
        return OrganizationDomainChangedVBuilder.newBuilder()
                                                .setId(command.getId())
                                                .setNewDomain(command.getNewDomain())
                                                .setOldDomain(oldDomain)
                                                .build();
    }

    OrganizationTenantChanged changeTenant(ChangeOrganizationTenant command,
                                           TenantId oldTenant) {
        return OrganizationTenantChangedVBuilder.newBuilder()
                                                .setId(command.getId())
                                                .setNewTenant(command.getNewTenant())
                                                .setOldTenant(oldTenant)
                                                .build();
    }
}
