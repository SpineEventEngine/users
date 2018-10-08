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

import io.spine.server.aggregate.Aggregate;
import io.spine.server.aggregate.AggregatePart;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.GroupId;
import io.spine.users.RoleId;
import io.spine.users.c.organization.Organization;
import io.spine.users.c.orgunit.OrgUnit;
import io.spine.users.c.user.UserPart;

import java.util.List;

import static io.spine.users.c.group.Group.OriginCase.EXTERNAL_DOMAIN;
import static io.spine.util.Exceptions.newIllegalArgumentException;

/**
 * An aggregate part of a {@link GroupRoot} that handles basic lifecycle events of a group.
 *
 * <p>A {@code Group} is the way to group {@linkplain UserPart users} by their common functions
 * and functional roles inside of an {@linkplain Organization organization} or
 * {@linkplain OrgUnit organizational unit}.
 *
 * <p>The roles, assigned to a group are implicitly inherited by all members of the group,
 * including sub-groups.
 *
 * @see GroupMembershipPart for the part that handle group memberships
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings({"OverlyCoupledClass"}) // It is OK for an aggregate.
public class GroupPart extends AggregatePart<GroupId, Group, GroupVBuilder, GroupRoot> {

    /**
     * @see Aggregate#Aggregate(Object)
     */
    GroupPart(GroupRoot root) {
        super(root);
    }

    @Assign
    GroupCreated handle(CreateGroup command) {
        return GroupCreatedVBuilder
                .newBuilder()
                .setId(command.getId())
                .setDisplayName(command.getDisplayName())
                .setEmail(command.getEmail())
                .setExternalDomain(command.getExternalDomain())
                .setOrgEntity(command.getOrgEntity())
                .addAllRole(command.getRoleList())
                .setDescription(command.getDescription())
                .build();
    }

    @Assign
    GroupMoved handle(MoveGroup command) throws CannotMoveExternalGroup {
        if (getState().getOriginCase() == EXTERNAL_DOMAIN) {
            throw new CannotMoveExternalGroup(command.getId(), getState().getExternalDomain());
        }
        return GroupMovedVBuilder
                .newBuilder()
                .setId(command.getId())
                .setNewOrgEntity(command.getNewOrgEntity())
                .setOldOrgEntity(getState().getOrgEntity())
                .build();
    }

    @Assign
    GroupDeleted handle(DeleteGroup command) {
        return GroupDeletedVBuilder
                .newBuilder()
                .setId(command.getId())
                .build();
    }

    @Assign
    RoleAssignedToGroup handle(AssignRoleToGroup command) {
        return RoleAssignedToGroupVBuilder
                .newBuilder()
                .setId(command.getId())
                .setRoleId(command.getRoleId())
                .build();
    }

    @Assign
    RoleUnassignedFromGroup handle(UnassignRoleFromGroup command)
            throws RoleIsNotAssignedToGroup {
        List<RoleId> roles = getState().getRoleList();
        RoleId roleId = command.getRoleId();
        if (!roles.contains(roleId)) {
            throw new RoleIsNotAssignedToGroup(getId(), roleId);
        }
        return RoleUnassignedFromGroupVBuilder
                .newBuilder()
                .setId(command.getId())
                .setRoleId(command.getRoleId())
                .build();
    }

    @Assign
    GroupRenamed handle(RenameGroup command) {
        return GroupRenamedVBuilder
                .newBuilder()
                .setId(command.getId())
                .setNewName(command.getNewName())
                .setOldName(getState().getDisplayName())
                .build();

    }

    @Assign
    GroupEmailChanged handle(ChangeGroupEmail command) {
        return GroupEmailChangedVBuilder
                .newBuilder()
                .setId(command.getId())
                .setNewEmail(command.getNewEmail())
                .setOldEmail(getState().getEmail())
                .build();
    }

    @Assign
    GroupDescriptionChanged handle(ChangeGroupDescription command) {
        return GroupDescriptionChangedVBuilder
                .newBuilder()
                .setId(command.getId())
                .setNewDescription(command.getDescription())
                .setOldDescription(getState().getDescription())
                .build();
    }

    @Apply
    void on(GroupCreated event) {
        GroupVBuilder builder = getBuilder();
        builder.setId(event.getId())
               .setDisplayName(event.getDisplayName())
               .setEmail(event.getEmail())
               .addAllRole(event.getRoleList())
               .setDescription(event.getDescription())
               .build();

        switch (event.getOriginCase()) {
            case ORG_ENTITY:
                builder.setOrgEntity(event.getOrgEntity());
                break;
            case EXTERNAL_DOMAIN:
                builder.setExternalDomain(event.getExternalDomain());
                break;
            case ORIGIN_NOT_SET: // Fallthrough intended.
            default:
                throw newIllegalArgumentException("No `origin` found in GroupCreated event");
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
    void on(GroupRenamed event) {
        getBuilder().setDisplayName(event.getNewName());
    }

    @Apply
    void on(GroupEmailChanged event) {
        getBuilder().setEmail(event.getNewEmail());
    }

    @Apply
    void on(GroupDescriptionChanged event) {
        getBuilder().setDescription(event.getNewDescription());
    }
}
