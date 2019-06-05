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

package io.spine.users.server.group.google;

import io.spine.users.OrganizationId;
import io.spine.users.OrganizationOrUnit;
import io.spine.users.google.group.event.GoogleGroupCreated;
import io.spine.users.google.group.event.GoogleGroupDeleted;
import io.spine.users.google.group.event.GoogleGroupDescriptionChanged;
import io.spine.users.google.group.event.GoogleGroupEmailChanged;
import io.spine.users.google.group.event.GoogleGroupJoinedParentGroup;
import io.spine.users.google.group.event.GoogleGroupLeftParentGroup;
import io.spine.users.google.group.event.GoogleGroupRenamed;
import io.spine.users.group.command.ChangeGroupDescription;
import io.spine.users.group.command.ChangeGroupEmail;
import io.spine.users.group.command.CreateGroup;
import io.spine.users.group.command.DeleteGroup;
import io.spine.users.group.command.JoinParentGroup;
import io.spine.users.group.command.LeaveParentGroup;
import io.spine.users.group.command.RenameGroup;

/**
 * A command factory for {@link GoogleGroupLifecyclePm}.
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
     * Creates {@link CreateGroup} command for a group from an external domain.
     */
    CreateGroup createExternalGroup(GoogleGroupCreated event) {
        CreateGroup result = CreateGroup
                .newBuilder()
                .setId(event.getId())
                .setEmail(event.getEmail())
                .setDisplayName(event.getDisplayName())
                .setExternalDomain(event.getDomain())
                .build();
        return result;
    }

    /**
     * Creates {@link CreateGroup} command for a group from a tenant's domain.
     */
    CreateGroup createInternalGroup(GoogleGroupCreated event, OrganizationId organization) {
        CreateGroup result = CreateGroup
                .newBuilder()
                .setId(event.getId())
                .setEmail(event.getEmail())
                .setDisplayName(event.getDisplayName())
                .setOrgEntity(orgEntity(organization))
                .build();
        return result;
    }

    /**
     * Creates {@link JoinParentGroup} command based on information from
     * {@link GoogleGroupJoinedParentGroup} event.
     */
    JoinParentGroup joinParentGroup(GoogleGroupJoinedParentGroup event) {
        return JoinParentGroup
                .newBuilder()
                .setId(event.getId())
                .setParentGroupId(event.getNewParentId())
                .build();
    }

    /**
     * Creates {@link RenameGroup} command based on information from {@link GoogleGroupRenamed}
     * event.
     */
    RenameGroup renameGroup(GoogleGroupRenamed event) {
        return RenameGroup
                .newBuilder()
                .setId(event.getId())
                .setNewName(event.getDisplayName())
                .build();
    }

    /**
     * Creates {@link LeaveParentGroup} command based on information from
     * {@link GoogleGroupLeftParentGroup} event.
     */
    LeaveParentGroup leaveParentGroup(GoogleGroupLeftParentGroup event) {
        return LeaveParentGroup
                .newBuilder()
                .setId(event.getId())
                .setParentGroupId(event.getParentGroupId())
                .build();
    }

    /**
     * Creates {@link DeleteGroup} command based on information from {@link GoogleGroupDeleted}
     * event.
     */
    DeleteGroup deleteGroup(GoogleGroupDeleted event) {
        return DeleteGroup
                .newBuilder()
                .setId(event.getId())
                .build();
    }

    /**
     * Creates {@link ChangeGroupEmail} command based on information from
     * {@link GoogleGroupEmailChanged} event.
     */
    ChangeGroupEmail changeEmail(GoogleGroupEmailChanged event) {
        return ChangeGroupEmail
                .newBuilder()
                .setId(event.getId())
                .setNewEmail(event.getNewEmail())
                .build();
    }

    ChangeGroupDescription changeDescription(GoogleGroupDescriptionChanged event) {
        return ChangeGroupDescription
                .newBuilder()
                .setId(event.getId())
                .setDescription(event.getNewDescription())
                .build();
    }

    private static OrganizationOrUnit orgEntity(OrganizationId organizationId) {
        return OrganizationOrUnit
                .newBuilder()
                .setOrganization(organizationId)
                .build();
    }
}
