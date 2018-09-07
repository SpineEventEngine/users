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

package io.spine.users.c.group;

import com.google.protobuf.Any;
import io.spine.core.CommandContext;
import io.spine.server.aggregate.Aggregate;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.GroupId;
import io.spine.users.RoleId;
import io.spine.users.c.organization.Organization;
import io.spine.users.c.orgunit.OrgUnit;
import io.spine.users.c.user.UserAggregate;

import java.util.List;
import java.util.Map;

import static io.spine.util.Exceptions.newIllegalArgumentException;

/**
 * An aggregate for {@link Group}.
 *
 * <p>A {@code Group} is the way to group {@linkplain UserAggregate users} by their common functions
 * and functional roles inside of an {@linkplain Organization organization} or
 * {@linkplain OrgUnit organizational unit}.
 *
 * <p>The roles, assigned to a group are implicitly inherited by all members of the group,
 * including sub-groups.
 *
 * <h3>Group Attributes
 *
 * <p>To make {@link GroupAggregate} meet specific requirements of the application, it can be extended
 * using custom attributes.
 *
 * <p>The following commands are available to work with the group attributes:
 *
 * <ul>
 * <li>{@link AddGroupAttribute} to add a new attribute;
 * <li>{@link UpdateGroupAttribute} to update an existing attribute;
 * <li>{@link RemoveGroupAttribute} to remove an attribute.
 * </ul>
 *
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings({"OverlyCoupledClass", "ClassWithTooManyMethods"}) // It is OK for aggregate.
public class GroupAggregate extends Aggregate<GroupId, Group, GroupVBuilder> {

    private static final String ATTRIBUTE_DOES_NOT_EXIST = "Attribute doesn't exist";

    /**
     * @see Aggregate#Aggregate(Object)
     */
    GroupAggregate(GroupId id) {
        super(id);
    }

    @Assign
    GroupCreated handle(CreateGroup command, CommandContext context) {
        return events(context).createGroup(command);
    }

    @Assign
    GroupOwnerChanged handle(ChangeGroupOwner command, CommandContext context) {
        return events(context).changeOwner(command, getState().getOwner());
    }

    @Assign
    GroupMoved handle(MoveGroup command, CommandContext context) {
        return events(context).moveGroup(command, getState().getParentEntity());
    }

    @Assign
    GroupDeleted handle(DeleteGroup command, CommandContext context) {
        return events(context).deleteGroup(command);
    }

    @Assign
    SuperGroupAdded handle(AddSuperGroup command,
                           CommandContext context) {
        return events(context).addSuperGroup(command);
    }

    @Assign
    SuperGroupRemoved handle(RemoveSuperGroup command, CommandContext context) {
        return events(context).removeSuperGroup(command);
    }

    @Assign
    RoleAssignedToGroup handle(AssignRoleToGroup command, CommandContext context) {
        return events(context).assignRole(command);
    }

    @Assign
    RoleUnassignedFromGroup handle(UnassignRoleFromGroup command, CommandContext context) {
        return events(context).unassignRole(command);
    }

    @Assign
    GroupAttributeAdded handle(AddGroupAttribute command, CommandContext context) {
        return events(context).addAttribute(command);
    }

    @Assign
    GroupAttributeRemoved handle(RemoveGroupAttribute command, CommandContext context) {
        Map<String, Any> attributes = getState().getAttributesMap();
        String attributeName = command.getName();
        if (attributes.containsKey(attributeName)) {
            return events(context).removeAttribute(command, attributes.get(attributeName));
        } else {
            throw newIllegalArgumentException(ATTRIBUTE_DOES_NOT_EXIST);
        }
    }

    @Assign
    GroupAttributeUpdated handle(UpdateGroupAttribute command, CommandContext context) {
        Map<String, Any> attributes = getState().getAttributesMap();
        String attributeName = command.getName();
        if (attributes.containsKey(attributeName)) {
            return events(context).updateAttribute(command, attributes.get(attributeName));
        } else {
            throw newIllegalArgumentException(ATTRIBUTE_DOES_NOT_EXIST);
        }
    }

    @Apply
    void on(GroupCreated event) {
        getBuilder().setId(event.getId())
                    .setDisplayName(event.getDisplayName())
                    .setEmail(event.getEmail())
                    .setOwner(event.getOwner())
                    .setParentEntity(event.getParentEntity())
                    .addAllRole(event.getRoleList())
                    .build();
    }

    @Apply
    void on(GroupOwnerChanged event) {
        getBuilder().setOwner(event.getNewOwner());
    }

    @Apply
    void on(GroupMoved event) {
        getBuilder().setParentEntity(event.getNewParentEntity());
    }

    @Apply
    void on(GroupDeleted event) {
        setDeleted(true);
    }

    @Apply
    void on(SuperGroupAdded event) {
        getBuilder().addMembership(event.getSuperGroupId());
    }

    @Apply
    void on(SuperGroupRemoved event) {
        GroupId upperGroup = event.getSuperGroupId();
        GroupVBuilder builder = getBuilder();
        List<GroupId> memberships = builder.getMembership();
        if (memberships.contains(upperGroup)) {
            int index = memberships.indexOf(upperGroup);
            builder.removeMembership(index);
        }
    }

    @Apply
    void on(RoleAssignedToGroup event) {
        getBuilder().addRole(event.getRoleId());
    }

    @Apply
    void on(RoleUnassignedFromGroup event) {
        RoleId roleId = event.getRoleId();
        GroupVBuilder builder = getBuilder();
        List<RoleId> roles = builder.getRole();
        if (roles.contains(roleId)) {
            int index = roles.indexOf(roleId);
            builder.removeMembership(index);
        }
    }

    @Apply
    void on(GroupAttributeAdded event) {
        getBuilder().putAttributes(event.getName(), event.getValue());
    }

    @Apply
    void on(GroupAttributeRemoved event) {
        String attributeName = event.getName();
        removeAttribute(attributeName);
    }

    @Apply
    void on(GroupAttributeUpdated event) {
        String attributeName = event.getName();
        removeAttribute(attributeName);
        getBuilder().putAttributes(attributeName, event.getNewValue());
    }

    private void removeAttribute(String attributeName) {
        GroupVBuilder builder = getBuilder();
        Map<String, Any> attributes = builder.getAttributes();
        if (attributes.containsKey(attributeName)) {
            builder.removeAttributes(attributeName);
        }
    }

    private static GroupEventFactory events(CommandContext context) {
        return GroupEventFactory.instance(context);
    }
}
