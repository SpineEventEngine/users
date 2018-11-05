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

import io.spine.core.CommandContext;
import io.spine.server.aggregate.Aggregate;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.OrgUnitId;
import io.spine.users.c.organization.OrganizationAggregate;

/**
 * An organizational unit (aka orgunit).
 *
 * <p>An orgunit is the part of the {@linkplain OrganizationAggregate organization} or a larger
 * {@linkplain OrgUnitAggregate organizational unit} that aggregates users and groups.'
 *
 * <p>It is forbidden to include an organizational unit into itself directly or indirectly. In other
 * words, the organizational structure must always be an acyclic graph.
 *
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings("OverlyCoupledClass") // It is OK for an aggregate.
public class OrgUnitAggregate
        extends Aggregate<OrgUnitId, OrgUnit, OrgUnitVBuilder> {

    /**
     * @see OrgUnitAggregate#OrgUnitAggregate(OrgUnitId)
     */
    OrgUnitAggregate(OrgUnitId id) {
        super(id);
    }

    @Assign
    OrgUnitCreated handle(CreateOrgUnit command, CommandContext context) {
        OrgUnitCreated event =
                OrgUnitCreatedVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setDisplayName(command.getDisplayName())
                        .setDomain(command.getDomain())
                        .setParentEntity(command.getParentEntity())
                        .build();
        return event;
    }

    @Assign
    OrgUnitDeleted handle(DeleteOrgUnit command, CommandContext context) {
        OrgUnitDeleted event =
                OrgUnitDeletedVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .build();
        return event;
    }

    @Assign
    OrgUnitMoved handle(MoveOrgUnit command, CommandContext context) {
        OrgUnitMoved event =
                OrgUnitMovedVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setNewParentEntity(command.getNewParentEntity())
                        .setOldParentEntity(getState().getParentEntity())
                        .build();
        return event;
    }

    @Assign
    OrgUnitRenamed handle(RenameOrgUnit command, CommandContext context) {
        OrgUnitRenamed event =
                OrgUnitRenamedVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setNewName(command.getNewName())
                        .setOldName(getState().getDisplayName())
                        .build();
        return event;
    }

    @Assign
    OrgUnitDomainChanged handle(ChangeOrgUnitDomain command, CommandContext context) {
        OrgUnitDomainChanged event =
                OrgUnitDomainChangedVBuilder
                        .newBuilder()
                        .setId(command.getId())
                        .setNewDomain(command.getNewDomain())
                        .setOldDomain(getState().getDomain())
                        .build();
        return event;
    }

    @Apply
    void on(OrgUnitCreated event) {
        getBuilder().setId(event.getId())
                    .setDisplayName(event.getDisplayName())
                    .setDomain(event.getDomain())
                    .setParentEntity(event.getParentEntity());
    }

    @Apply
    void on(OrgUnitDeleted event) {
        setDeleted(true);
    }

    @Apply
    void on(OrgUnitMoved event) {
        getBuilder().setParentEntity(event.getNewParentEntity());
    }

    @Apply
    void on(OrgUnitRenamed event) {
        getBuilder().setDisplayName(event.getNewName());
    }

    @Apply
    void on(OrgUnitDomainChanged event) {
        getBuilder().setDomain(event.getNewDomain());
    }
}
