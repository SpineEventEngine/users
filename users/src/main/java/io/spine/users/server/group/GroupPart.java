/*
 * Copyright 2019, TeamDev. All rights reserved.
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

package io.spine.users.server.group;

import io.spine.server.aggregate.Aggregate;
import io.spine.server.aggregate.AggregatePart;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.GroupId;
import io.spine.users.RoleId;
import io.spine.users.group.Group;
import io.spine.users.group.GroupVBuilder;
import io.spine.users.group.command.AssignRoleToGroup;
import io.spine.users.group.command.ChangeGroupDescription;
import io.spine.users.group.command.ChangeGroupEmail;
import io.spine.users.group.command.CreateGroup;
import io.spine.users.group.command.DeleteGroup;
import io.spine.users.group.command.MoveGroup;
import io.spine.users.group.command.RenameGroup;
import io.spine.users.group.command.UnassignRoleFromGroup;
import io.spine.users.group.event.GroupCreated;
import io.spine.users.group.event.GroupCreatedVBuilder;
import io.spine.users.group.event.GroupDeleted;
import io.spine.users.group.event.GroupDeletedVBuilder;
import io.spine.users.group.event.GroupDescriptionChanged;
import io.spine.users.group.event.GroupDescriptionChangedVBuilder;
import io.spine.users.group.event.GroupEmailChanged;
import io.spine.users.group.event.GroupEmailChangedVBuilder;
import io.spine.users.group.event.GroupMoved;
import io.spine.users.group.event.GroupMovedVBuilder;
import io.spine.users.group.event.GroupRenamed;
import io.spine.users.group.event.GroupRenamedVBuilder;
import io.spine.users.group.event.RoleAssignedToGroup;
import io.spine.users.group.event.RoleAssignedToGroupVBuilder;
import io.spine.users.group.event.RoleUnassignedFromGroup;
import io.spine.users.group.event.RoleUnassignedFromGroupVBuilder;
import io.spine.users.group.rejection.CannotMoveExternalGroup;
import io.spine.users.group.rejection.RoleIsNotAssignedToGroup;
import io.spine.users.organization.Organization;
import io.spine.users.orgunit.OrgUnit;
import io.spine.users.server.user.UserPart;

import java.util.List;

import static io.spine.users.group.Group.OriginCase.EXTERNAL_DOMAIN;
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
 */
@SuppressWarnings("OverlyCoupledClass") // It is OK for an aggregate.
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
                .setDescription(command.getDescription())
                .build();
    }

    @Assign
    GroupMoved handle(MoveGroup command) throws CannotMoveExternalGroup {
        if (state().getOriginCase() == EXTERNAL_DOMAIN) {
            throw CannotMoveExternalGroup
                    .newBuilder()
                    .setId(command.getId())
                    .setExternalDomain(state().getExternalDomain())
                    .build();
        }
        return GroupMovedVBuilder
                .newBuilder()
                .setId(command.getId())
                .setNewOrgEntity(command.getNewOrgEntity())
                .setOldOrgEntity(state().getOrgEntity())
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
        List<RoleId> roles = state().getRoleList();
        RoleId roleId = command.getRoleId();
        if (!roles.contains(roleId)) {
            throw RoleIsNotAssignedToGroup
                    .newBuilder()
                    .setId(id())
                    .setRoleId(roleId)
                    .build();
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
                .setOldName(state().getDisplayName())
                .build();

    }

    @Assign
    GroupEmailChanged handle(ChangeGroupEmail command) {
        return GroupEmailChangedVBuilder
                .newBuilder()
                .setId(command.getId())
                .setNewEmail(command.getNewEmail())
                .setOldEmail(state().getEmail())
                .build();
    }

    @Assign
    GroupDescriptionChanged handle(ChangeGroupDescription command) {
        return GroupDescriptionChangedVBuilder
                .newBuilder()
                .setId(command.getId())
                .setNewDescription(command.getDescription())
                .setOldDescription(state().getDescription())
                .build();
    }

    @Apply
    private void on(GroupCreated event) {
        GroupVBuilder builder = builder();
        builder.setId(event.getId())
               .setDisplayName(event.getDisplayName())
               .setEmail(event.getEmail())
               .setDescription(event.getDescription());

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
    private void on(GroupMoved event) {
        builder().setOrgEntity(event.getNewOrgEntity());
    }

    @Apply
    private void on(@SuppressWarnings("unused") // Event data is not required.
                    GroupDeleted event) {
        setDeleted(true);
    }

    @Apply
    private void on(RoleAssignedToGroup event) {
        builder().addRole(event.getRoleId());
    }

    @Apply
    private void on(RoleUnassignedFromGroup event) {
        RoleId roleId = event.getRoleId();
        GroupVBuilder builder = builder();
        List<RoleId> roles = builder.getRole();
        if (roles.contains(roleId)) {
            int index = roles.indexOf(roleId);
            builder.removeRole(index);
        }
    }

    @Apply
    private void on(GroupRenamed event) {
        builder().setDisplayName(event.getNewName());
    }

    @Apply
    private void on(GroupEmailChanged event) {
        builder().setEmail(event.getNewEmail());
    }

    @Apply
    private void on(GroupDescriptionChanged event) {
        builder().setDescription(event.getNewDescription());
    }
}
