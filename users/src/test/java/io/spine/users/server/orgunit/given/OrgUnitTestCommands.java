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

package io.spine.users.server.orgunit.given;

import io.spine.users.OrgUnitId;
import io.spine.users.orgunit.command.ChangeOrgUnitDomain;
import io.spine.users.orgunit.command.CreateOrgUnit;
import io.spine.users.orgunit.command.DeleteOrgUnit;
import io.spine.users.orgunit.command.MoveOrgUnit;
import io.spine.users.orgunit.command.RenameOrgUnit;

/**
 * Test commands for {@code OrgUnitAggregate}.
 */
public final class OrgUnitTestCommands {

    /**
     * Prevents instantiation.
     */
    private OrgUnitTestCommands() {
    }

    public static CreateOrgUnit createOrgUnit(OrgUnitId id) {
        return CreateOrgUnit
                .newBuilder()
                .setId(id)
                .setDisplayName(OrgUnitTestEnv.orgUnitName())
                .setDomain(OrgUnitTestEnv.orgUnitDomain())
                .setParentEntity(OrgUnitTestEnv.orgUnitParentEntity())
                .build();
    }

    public static DeleteOrgUnit deleteOrgUnit(OrgUnitId id) {
        return DeleteOrgUnit
                .newBuilder()
                .setId(id)
                .build();
    }

    public static MoveOrgUnit moveOrgUnit(OrgUnitId id) {
        return MoveOrgUnit
                .newBuilder()
                .setId(id)
                .setNewParentEntity(OrgUnitTestEnv.newOrgUnitParentEntity())
                .build();
    }

    public static RenameOrgUnit renameOrgUnit(OrgUnitId id) {
        return RenameOrgUnit
                .newBuilder()
                .setId(id)
                .setNewName(OrgUnitTestEnv.orgUnitNewName())
                .build();
    }

    public static ChangeOrgUnitDomain changeOrgUnitDomain(OrgUnitId id) {
        return ChangeOrgUnitDomain
                .newBuilder()
                .setId(id)
                .setNewDomain(OrgUnitTestEnv.orgUnitNewDomain())
                .build();
    }
}
