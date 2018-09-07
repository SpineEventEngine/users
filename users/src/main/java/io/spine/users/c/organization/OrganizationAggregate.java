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
import io.spine.core.CommandContext;
import io.spine.server.aggregate.Aggregate;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.OrganizationId;
import io.spine.users.c.orgunit.OrgUnit;

import java.util.Map;

import static io.spine.util.Exceptions.newIllegalArgumentException;

/**
 * An organization of a tenant, the topmost entity in organizational structure.
 *
 * <p>An organization aggregates users and groups directly or in hierarchy of
 * {@linkplain OrgUnit organizational units}.
 *
 * <h3>Organization Attributes
 *
 * <p>To make {@link OrganizationAggregate} meet specific requirements of the application,
 * it can be extended by custom attributes.
 *
 * <p>The following commands are available to work with the organization attributes:
 *
 * <ul>
 * <li>{@link AddOrganizationAttribute} to add a new attribute, or replace the existing one;
 * <li>{@link RemoveOrganizationAttribute} to remove an attribute;
 * <li>{@link UpdateOrganizationAttribute} to update an existing attribute.
 * </ul>
 *
 * @author Vladyslav Lubenskyi
 */
public class OrganizationAggregate
        extends Aggregate<OrganizationId, Organization, OrganizationVBuilder> {

    private static final String ATTRIBUTE_DOES_NOT_EXIST = "Attribute doesn't exist";

    /**
     * @see OrganizationAggregate#OrganizationAggregate(OrganizationId)
     */
    OrganizationAggregate(OrganizationId id) {
        super(id);
    }

    @Assign
    OrganizationCreated handle(CreateOrganization command, CommandContext context) {
        return events(context).createOrganization(command);
    }

    @Assign
    OrganizationOwnerChanged handle(ChangeOrganizationOwner command, CommandContext context) {
        return events(context).changeOwner(command, getState().getOwner());
    }

    @Assign
    OrganizationDeleted handle(DeleteOrganization command, CommandContext context) {
        return events(context).deleteOrganization(command);
    }

    @Assign
    OrganizationAttributeAdded handle(AddOrganizationAttribute command, CommandContext context) {
        return events(context).addAttribute(command);
    }

    @Assign
    OrganizationAttributeRemoved handle(RemoveOrganizationAttribute command,
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
    OrganizationAttributeUpdated handle(UpdateOrganizationAttribute command,
                                        CommandContext context) {
        Map<String, Any> attributes = getState().getAttributesMap();
        String attributeName = command.getName();
        if (attributes.containsKey(attributeName)) {
            return events(context).updateAttribute(command, attributes.get(attributeName));
        } else {
            throw newIllegalArgumentException(ATTRIBUTE_DOES_NOT_EXIST);
        }
    }

    @Apply
    void on(OrganizationCreated event) {
        getBuilder().setId(event.getId())
                    .setDisplayName(event.getDisplayName())
                    .setDomain(event.getDomain())
                    .setOwner(event.getOwner())
                    .setTenant(event.getTenant())
                    .putAllAttributes(event.getAttributesMap());
    }

    @Apply
    void on(OrganizationOwnerChanged event) {
        getBuilder().setOwner(event.getNewOwner());
    }

    @Apply
    void on(OrganizationDeleted event) {
        setDeleted(true);
    }

    @Apply
    void on(OrganizationAttributeAdded event) {
        getBuilder().putAttributes(event.getName(), event.getValue());
    }

    @Apply
    void on(OrganizationAttributeRemoved event) {
        removeAttribute(event.getName());
    }

    @Apply
    void on(OrganizationAttributeUpdated event) {
        removeAttribute(event.getName());
        getBuilder().putAttributes(event.getName(), event.getNewValue());
    }

    private void removeAttribute(String attributeName) {
        OrganizationVBuilder builder = getBuilder();
        Map<String, Any> attributes = builder.getAttributes();
        if (attributes.containsKey(attributeName)) {
            builder.removeAttributes(attributeName);
        }
    }

    private static OrganizationEventFactory events(CommandContext context) {
        return OrganizationEventFactory.instance(context);
    }
}
