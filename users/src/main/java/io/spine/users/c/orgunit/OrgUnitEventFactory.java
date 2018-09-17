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

import com.google.protobuf.Any;
import io.spine.core.ActorContext;
import io.spine.core.CommandContext;
import io.spine.core.UserId;
import io.spine.net.InternetDomain;
import io.spine.users.OrganizationalEntity;
import io.spine.users.c.EntityEventFactory;

/**
 * An event factory for the {@linkplain OrgUnit OrgUnit aggregate}.
 *
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings("OverlyCoupledClass") // It is OK for event factory.
final class OrgUnitEventFactory extends EntityEventFactory {

    /**
     * @see EntityEventFactory#EntityEventFactory(ActorContext)
     */
    private OrgUnitEventFactory(ActorContext actorContext) {
        super(actorContext);
    }

    /**
     * Retrieves an instance of {@link OrgUnitEventFactory}.
     *
     * @param context
     *         the {@link CommandContext} of the command to handle
     * @return new instance of {@link OrgUnitEventFactory}
     */
    static OrgUnitEventFactory instance(CommandContext context) {
        ActorContext actorContext = context.getActorContext();
        return new OrgUnitEventFactory(actorContext);
    }

    OrgUnitCreated createOrgUnit(CreateOrgUnit command) {
        return OrgUnitCreatedVBuilder.newBuilder()
                                     .setId(command.getId())
                                     .setDisplayName(command.getDisplayName())
                                     .setDomain(command.getDomain())
                                     .setOwner(command.getOwner())
                                     .setParentEntity(command.getParentEntity())
                                     .putAllAttributes(command.getAttributesMap())
                                     .build();
    }

    OrgUnitOwnerChanged changeOwner(ChangeOrgUnitOwner command, UserId oldOwner) {
        return OrgUnitOwnerChangedVBuilder.newBuilder()
                                          .setId(command.getId())
                                          .setNewOwner(command.getNewOwner())
                                          .setOldOwner(oldOwner)
                                          .build();
    }

    OrgUnitDeleted deleteOrgUnit(DeleteOrgUnit command) {
        return OrgUnitDeletedVBuilder.newBuilder()
                                     .setId(command.getId())
                                     .build();
    }

    OrgUnitAttributeAdded addAttribute(AddOrgUnitAttribute command) {
        return OrgUnitAttributeAddedVBuilder.newBuilder()
                                            .setId(command.getId())
                                            .setName(command.getName())
                                            .setValue(command.getValue())
                                            .build();
    }

    OrgUnitAttributeRemoved removeAttribute(RemoveOrgUnitAttribute command,
                                            Any oldValue) {
        return OrgUnitAttributeRemovedVBuilder.newBuilder()
                                              .setId(command.getId())
                                              .setName(command.getName())
                                              .setValue(oldValue)
                                              .build();
    }

    OrgUnitAttributeUpdated updateAttribute(UpdateOrgUnitAttribute command,
                                            Any oldValue) {
        return OrgUnitAttributeUpdatedVBuilder.newBuilder()
                                              .setId(command.getId())
                                              .setName(command.getName())
                                              .setNewValue(command.getNewValue())
                                              .setOldValue(oldValue)
                                              .build();
    }

    OrgUnitMoved moveOrgUnit(MoveOrgUnit command, OrganizationalEntity oldParent) {
        return OrgUnitMovedVBuilder.newBuilder()
                                   .setId(command.getId())
                                   .setNewParentEntity(command.getNewParentEntity())
                                   .setOldParentEntity(oldParent)
                                   .build();
    }

    OrgUnitRenamed rename(RenameOrgUnit command, String oldName) {
        return OrgUnitRenamedVBuilder.newBuilder()
                                     .setId(command.getId())
                                     .setNewName(command.getNewName())
                                     .setOldName(oldName)
                                     .build();
    }

    OrgUnitDomainChanged changeDomain(ChangeOrgUnitDomain command, InternetDomain oldDomain) {
        return OrgUnitDomainChangedVBuilder.newBuilder()
                                           .setId(command.getId())
                                           .setNewDomain(command.getNewDomain())
                                           .setOldDomain(oldDomain)
                                           .build();
    }
}
