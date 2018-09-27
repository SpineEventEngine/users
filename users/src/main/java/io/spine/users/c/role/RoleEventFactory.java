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

package io.spine.users.c.role;

import io.spine.users.OrganizationOrUnit;

/**
 * An event factory for the {@linkplain RoleAggregate Role aggregate}.
 *
 * @author Vladyslav Lubenskyi
 */
final class RoleEventFactory {

    /**
     * Prevents direct instantiation.
     */
    private RoleEventFactory() {
    }

    /**
     * Retrieves an instance of {@link RoleEventFactory}.
     */
    static RoleEventFactory instance() {
        return new RoleEventFactory();
    }

    RoleCreated createRole(CreateRole command) {
        return RoleCreatedVBuilder.newBuilder()
                                  .setId(command.getId())
                                  .setDisplayName(command.getDisplayName())
                                  .setOrgEntity(command.getOrgEntity())
                                  .build();
    }

    RoleDeleted deleteRole(DeleteRole command) {
        return RoleDeletedVBuilder.newBuilder()
                                  .setId(command.getId())
                                  .build();
    }

    RoleRenamed renameRole(RenameRole command, String oldName) {
        return RoleRenamedVBuilder.newBuilder()
                                  .setId(command.getId())
                                  .setNewName(command.getNewName())
                                  .setOldName(oldName)
                                  .build();
    }

    RoleParentChanged changeParent(ChangeRoleParent command, OrganizationOrUnit oldParent) {
        return RoleParentChangedVBuilder.newBuilder()
                                        .setId(command.getId())
                                        .setNewOrgEntity(command.getNewOrgEntity())
                                        .setOldOrgEntity(oldParent)
                                        .build();
    }
}
