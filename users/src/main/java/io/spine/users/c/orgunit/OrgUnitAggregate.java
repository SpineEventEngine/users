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
import io.spine.core.CommandContext;
import io.spine.server.aggregate.Aggregate;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.OrgUnitId;
import io.spine.users.c.organization.OrganizationAggregate;

import java.util.Map;

import static io.spine.util.Exceptions.newIllegalArgumentException;

/**
 * An organizational unit (aka orgunit).
 *
 * <p>An orgunit is the part of the {@linkplain OrganizationAggregate organization} or a larger
 * {@linkplain OrgUnitAggregate organizational unit} that aggregates users and groups.
 *
 * <h3>Orgunit Attributes
 *
 * <p>To make organizational unit meet specific requirements of the application, it can be extended
 * using custom attributes.
 *
 * <p>The following commands are available to work with the orgunit attributes:
 *
 * <ul>
 *   <li>{@link AddOrgUnitAttribute} to add a new attribute, or replace the existing one;
 *   <li>{@link RemoveOrgUnitAttribute} to remove an attribute;
 *   <li>{@link UpdateOrgUnitAttribute} to update an existing attribute.
 * </ul>
 *
 * @author Vladyslav Lubenskyi
 */
public class OrgUnitAggregate
        extends Aggregate<OrgUnitId, OrgUnit, OrgUnitVBuilder> {

    private static final String ATTRIBUTE_DOES_NOT_EXIST = "Attribute doesn't exist";

    /**
     * @see OrgUnitAggregate#OrgUnitAggregate(OrgUnitId)
     */
    OrgUnitAggregate(OrgUnitId id) {
        super(id);
    }

    @Assign
    OrgUnitCreated handle(CreateOrgUnit command, CommandContext context) {
        return events(context).createOrgUnit(command);
    }

    @Assign
    OrgUnitOwnerChanged handle(ChangeOrgUnitOwner command, CommandContext context) {
        return events(context).changeOwner(command, getState().getOwner());
    }

    @Assign
    OrgUnitDeleted handle(DeleteOrgUnit command, CommandContext context) {
        return events(context).deleteOrgUnit(command);
    }

    @Assign
    OrgUnitAttributeAdded handle(AddOrgUnitAttribute command, CommandContext context) {
        return events(context).addAttribute(command);
    }

    @Assign
    OrgUnitAttributeRemoved handle(RemoveOrgUnitAttribute command,
                                   CommandContext context) {
        Map<String, Any> attributes = getState().getAttributesMap();
        String attributeName = command.getName();
        if (attributes.containsKey(attributeName)) {
            return events(context).removeAttribute(command, attributes.get(attributeName));
        } else {
            throw newIllegalArgumentException(ATTRIBUTE_DOES_NOT_EXIST);
        }
    }

    @Assign
    OrgUnitAttributeUpdated handle(UpdateOrgUnitAttribute command,
                                   CommandContext context) {
        Map<String, Any> attributes = getState().getAttributesMap();
        String attributeName = command.getName();
        if (attributes.containsKey(attributeName)) {
            return events(context).updateAttribute(command, attributes.get(attributeName));
        } else {
            throw newIllegalArgumentException(ATTRIBUTE_DOES_NOT_EXIST);
        }
    }

    @Assign
    OrgUnitMoved handle(MoveOrgUnit command, CommandContext context) {
        return events(context).moveOrgUnit(command, getState().getParentEntity());
    }

    @Apply
    void on(OrgUnitCreated event) {
        getBuilder().setId(event.getId())
                    .setDisplayName(event.getDisplayName())
                    .setDomain(event.getDomain())
                    .setOwner(event.getOwner())
                    .setParentEntity(event.getParentEntity())
                    .putAllAttributes(event.getAttributesMap());
    }

    @Apply
    void on(OrgUnitOwnerChanged event) {
        getBuilder().setOwner(event.getNewOwner());
    }

    @Apply
    void on(OrgUnitDeleted event) {
        setDeleted(true);
    }

    @Apply
    void on(OrgUnitAttributeAdded event) {
        getBuilder().putAttributes(event.getName(), event.getValue());
    }

    @Apply
    void on(OrgUnitAttributeRemoved event) {
        removeAttribute(event.getName());
    }

    @Apply
    void on(OrgUnitAttributeUpdated event) {
        removeAttribute(event.getName());
        getBuilder().putAttributes(event.getName(), event.getNewValue());
    }

    @Apply
    void on(OrgUnitMoved event) {
        getBuilder().setParentEntity(event.getNewParentEntity());
    }

    private void removeAttribute(String attributeName) {
        OrgUnitVBuilder builder = getBuilder();
        Map<String, Any> attributes = builder.getAttributes();
        if (attributes.containsKey(attributeName)) {
            builder.removeAttributes(attributeName);
        }
    }

    private static OrgUnitEventFactory events(CommandContext context) {
        return OrgUnitEventFactory.instance(context);
    }
}
