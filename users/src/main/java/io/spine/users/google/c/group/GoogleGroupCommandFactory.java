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

package io.spine.users.google.c.group;

import io.spine.users.OrganizationId;
import io.spine.users.OrganizationOrUnit;
import io.spine.users.OrganizationOrUnitVBuilder;
import io.spine.users.c.group.AssignRoleToGroup;
import io.spine.users.c.group.AssignRoleToGroupVBuilder;
import io.spine.users.c.group.ChangeGroupEmail;
import io.spine.users.c.group.ChangeGroupEmailVBuilder;
import io.spine.users.c.group.CreateGroup;
import io.spine.users.c.group.CreateGroupVBuilder;
import io.spine.users.c.group.DeleteGroup;
import io.spine.users.c.group.DeleteGroupVBuilder;
import io.spine.users.c.group.JoinParentGroup;
import io.spine.users.c.group.JoinParentGroupVBuilder;
import io.spine.users.c.group.LeaveParentGroup;
import io.spine.users.c.group.LeaveParentGroupVBuilder;
import io.spine.users.c.group.RenameGroup;
import io.spine.users.c.group.RenameGroupVBuilder;
import io.spine.users.c.group.UnassignRoleFromGroup;
import io.spine.users.c.group.UnassignRoleFromGroupVBuilder;

import static io.spine.util.Exceptions.newIllegalArgumentException;

/**
 * A command factory for {@link GoogleGroupPm}.
 *
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings("OverlyCoupledClass")
        // It is OK for a command factory.
class GoogleGroupCommandFactory {

    /**
     * Prevents direct instantiation.
     */
    private GoogleGroupCommandFactory() {
    }

    static GoogleGroupCommandFactory instance() {
        return new GoogleGroupCommandFactory();
    }

    /**
     * Creates {@link CreateGroup} command based on information from {@link GoogleGroupCreated}
     * event.
     */
    CreateGroup createGroup(GoogleGroupCreated event) {
        CreateGroupVBuilder builder = CreateGroupVBuilder.newBuilder()
                                                         .setId(event.getId())
                                                         .setEmail(event.getEmail())
                                                         .setDisplayName(event.getDisplayName());
        switch (event.getOriginCase()) {

            case ORGANIZATION:
                OrganizationOrUnit orgEntity = orgEntity(event.getOrganization());
                builder.setOrgEntity(orgEntity);
                break;
            case EXTERNAL_DOMAIN:
                builder.setExternalDomain(event.getExternalDomain());
                break;
            case ORIGIN_NOT_SET:
                throw newIllegalArgumentException(
                        "No `origin` found in `GoogleGroupCreated` event");
        }
        return builder.build();
    }

    /**
     * Creates {@link JoinParentGroup} command based on information from
     * {@link GoogleGroupJoinedParentGroup} event.
     */
    JoinParentGroup joinParentGroup(GoogleGroupJoinedParentGroup event) {
        return JoinParentGroupVBuilder.newBuilder()
                                      .setId(event.getId())
                                      .setParentGroupId(event.getNewParentId())
                                      .build();
    }

    /**
     * Creates {@link RenameGroup} command based on information from {@link GoogleGroupRenamed}
     * event.
     */
    RenameGroup renameGroup(GoogleGroupRenamed event) {
        return RenameGroupVBuilder.newBuilder()
                                  .setId(event.getId())
                                  .setNewName(event.getDisplayName())
                                  .build();
    }

    /**
     * Creates {@link LeaveParentGroup} command based on information from
     * {@link GoogleGroupLeftParentGroup} event.
     */
    LeaveParentGroup leaveParentGroup(GoogleGroupLeftParentGroup event) {
        return LeaveParentGroupVBuilder.newBuilder()
                                       .setId(event.getId())
                                       .setParentGroupId(event.getId())
                                       .build();
    }

    /**
     * Creates {@link DeleteGroup} command based on information from {@link GoogleGroupDeleted}
     * event.
     */
    DeleteGroup deleteGroup(GoogleGroupDeleted event) {
        return DeleteGroupVBuilder.newBuilder()
                                  .setId(event.getId())
                                  .build();
    }

    /**
     * Creates {@link ChangeGroupEmail} command based on information from
     * {@link GoogleGroupEmailChanged} event.
     */
    ChangeGroupEmail changeEmail(GoogleGroupEmailChanged event) {
        return ChangeGroupEmailVBuilder.newBuilder()
                                       .setId(event.getId())
                                       .setNewEmail(event.getNewEmail())
                                       .build();
    }

    /**
     * Creates {@link AssignRoleToGroup} command based on information from
     * {@link RoleAssignedToGoogleGroup} event.
     */
    AssignRoleToGroup assignRole(RoleAssignedToGoogleGroup event) {
        return AssignRoleToGroupVBuilder.newBuilder()
                                        .setId(event.getId())
                                        .setRoleId(event.getRoleId())
                                        .build();
    }

    /**
     * Creates {@link UnassignRoleFromGroup} command based on information from
     * {@link RoleUnassignedFromGoogleGroup} event.
     */
    UnassignRoleFromGroup unassignRole(RoleUnassignedFromGoogleGroup event) {
        return UnassignRoleFromGroupVBuilder.newBuilder()
                                            .setId(event.getId())
                                            .setRoleId(event.getRoleId())
                                            .build();
    }

    private static OrganizationOrUnit orgEntity(OrganizationId organizationId) {
        return OrganizationOrUnitVBuilder.newBuilder()
                                         .setOrganization(organizationId)
                                         .build();
    }
}
