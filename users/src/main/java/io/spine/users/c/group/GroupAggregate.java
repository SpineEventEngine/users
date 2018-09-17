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
import io.spine.core.UserId;
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
 * <p>It is forbidden for groups to directly or indirectly join themselves; in other words,
 * all nested group memberships must always form an acyclic graph.
 *
 * <h3>Group Attributes</h3>
 *
 * <p>To make {@link GroupAggregate} meet specific requirements of the application, it can be
 * extended using custom attributes.
 *
 * <p>The following commands are available to work with the group attributes:
 *
 * <ul>
 *     <li>{@link AddGroupAttribute} to add a new attribute;
 *     <li>{@link UpdateGroupAttribute} to update an existing attribute;
 *     <li>{@link RemoveGroupAttribute} to remove an attribute.
 * </ul>
 *
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings({"OverlyCoupledClass", "ClassWithTooManyMethods"}) // It is OK for aggregate.
public class GroupAggregate extends Aggregate<GroupId, Group, GroupVBuilder> {

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
    GroupOwnerAdded handle(AddGroupOwner command, CommandContext context) {
        return events(context).addOwner(command);
    }

    @Assign
    GroupOwnerRemoved handle(RemoveGroupOwner command, CommandContext context)
            throws NoSuchOwnerInGroup, GroupMustHaveAnOwner {
        List<UserId> owners = getState().getOwnersList();
        UserId ownerToRemove = command.getOwner();
        if (!owners.contains(ownerToRemove)) {
            throw new NoSuchOwnerInGroup(command.getId(), ownerToRemove);
        }
        if (owners.size() == 1) {
            throw new GroupMustHaveAnOwner(command.getId());
        }
        return events(context).removeOwner(command);
    }

    @Assign
    GroupMoved handle(MoveGroup command, CommandContext context) {
        return events(context).moveGroup(command, getState().getOrgEntity());
    }

    @Assign
    GroupDeleted handle(DeleteGroup command, CommandContext context) {
        return events(context).deleteGroup(command);
    }

    @Assign
    ParentGroupJoined handle(JoinParentGroup command, CommandContext context) {
        return events(context).joinGroup(command);
    }

    @Assign
    ParentGroupLeft handle(LeaveParentGroup command, CommandContext context) {
        return events(context).leaveGroup(command);
    }

    @Assign
    RoleAssignedToGroup handle(AssignRoleToGroup command, CommandContext context) {
        return events(context).assignRole(command);
    }

    @Assign
    RoleUnassignedFromGroup handle(UnassignRoleFromGroup command, CommandContext context)
            throws RoleIsNotAssignedToGroup {
        List<RoleId> roles = getState().getRoleList();
        RoleId roleId = command.getRoleId();
        if (!roles.contains(roleId)) {
            throw new RoleIsNotAssignedToGroup(getId(), roleId);
        }
        return events(context).unassignRole(command);
    }

    @Assign
    GroupAttributeAdded handle(AddGroupAttribute command, CommandContext context) {
        return events(context).addAttribute(command);
    }

    @Assign
    GroupAttributeRemoved handle(RemoveGroupAttribute command, CommandContext context)
            throws GroupAttributeDoesNotExist {
        Map<String, Any> attributes = getState().getAttributesMap();
        String attributeName = command.getName();
        if (attributes.containsKey(attributeName)) {
            return events(context).removeAttribute(command, attributes.get(attributeName));
        } else {
            throw new GroupAttributeDoesNotExist(getId(), attributeName);
        }
    }

    @Assign
    GroupAttributeUpdated handle(UpdateGroupAttribute command, CommandContext context)
            throws GroupAttributeDoesNotExist {
        Map<String, Any> attributes = getState().getAttributesMap();
        String attributeName = command.getName();
        if (attributes.containsKey(attributeName)) {
            return events(context).updateAttribute(command, attributes.get(attributeName));
        } else {
            throw new GroupAttributeDoesNotExist(getId(), attributeName);
        }
    }

    @Assign
    GroupRenamed handle(RenameGroup command, CommandContext context) {
        return events(context).rename(command, getState().getDisplayName());
    }

    @Assign
    GroupEmailChanged handle(ChangeGroupEmail command, CommandContext context) {
        return events(context).changeEmail(command, getState().getEmail());
    }

    @Apply
    void on(GroupCreated event) {
        getBuilder().setId(event.getId())
                    .setDisplayName(event.getDisplayName())
                    .setEmail(event.getEmail())
                    .addAllOwners(event.getOwnersList())
                    .setOrgEntity(event.getOrgEntity())
                    .putAllAttributes(event.getAttributesMap())
                    .addAllRole(event.getRoleList())
                    .build();
    }

    @Apply
    void on(GroupOwnerAdded event) {
        getBuilder().addOwners(event.getNewOwner());
    }

    @Apply
    void on(GroupOwnerRemoved event) {
        List<UserId> owners = getBuilder().getOwners();
        UserId ownerToRemove = event.getRemovedOwner();
        if (owners.contains(ownerToRemove)) {
            int ownerIndex = owners.indexOf(ownerToRemove);
            getBuilder().removeOwners(ownerIndex);
        }
    }

    @Apply
    void on(GroupMoved event) {
        getBuilder().setOrgEntity(event.getNewOrgEntity());
    }

    @Apply
    void on(GroupDeleted event) {
        setDeleted(true);
    }

    @Apply
    void on(ParentGroupJoined event) {
        getBuilder().addMembership(event.getParentGroupId());
    }

    @Apply
    void on(ParentGroupLeft event) {
        GroupId upperGroup = event.getParentGroupId();
        GroupVBuilder builder = getBuilder();
        List<GroupId> memberships = builder.getMembership();
        if (memberships.contains(upperGroup)) {
            int index = memberships.indexOf(upperGroup);
            builder.removeMembership(index);
        }
    }

//    GroupOwnerAdded
//    GroupOwnerRemoved

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
            builder.removeRole(index);
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

    @Apply
    void on(GroupRenamed event) {
        getBuilder().setDisplayName(event.getNewName());
    }

    @Apply
    void on(GroupEmailChanged event) {
        getBuilder().setEmail(event.getNewEmail());
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
