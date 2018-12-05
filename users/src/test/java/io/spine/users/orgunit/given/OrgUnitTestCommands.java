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

package io.spine.users.orgunit.given;

import io.spine.users.OrgUnitId;
import io.spine.users.orgunit.OrgUnitAggregate;
import io.spine.users.orgunit.command.ChangeOrgUnitDomain;
import io.spine.users.orgunit.command.ChangeOrgUnitDomainVBuilder;
import io.spine.users.orgunit.command.CreateOrgUnit;
import io.spine.users.orgunit.command.CreateOrgUnitVBuilder;
import io.spine.users.orgunit.command.DeleteOrgUnit;
import io.spine.users.orgunit.command.DeleteOrgUnitVBuilder;
import io.spine.users.orgunit.command.MoveOrgUnit;
import io.spine.users.orgunit.command.MoveOrgUnitVBuilder;
import io.spine.users.orgunit.command.RenameOrgUnit;
import io.spine.users.orgunit.command.RenameOrgUnitVBuilder;

import static io.spine.users.orgunit.given.OrgUnitTestEnv.newOrgUnitParentEntity;
import static io.spine.users.orgunit.given.OrgUnitTestEnv.orgUnitDomain;
import static io.spine.users.orgunit.given.OrgUnitTestEnv.orgUnitName;
import static io.spine.users.orgunit.given.OrgUnitTestEnv.orgUnitNewDomain;
import static io.spine.users.orgunit.given.OrgUnitTestEnv.orgUnitNewName;
import static io.spine.users.orgunit.given.OrgUnitTestEnv.orgUnitParentEntity;

/**
 * Test commands for {@link OrgUnitAggregate}.
 *
 * @author Vladyslav Lubenskyi
 */
public final class OrgUnitTestCommands {

    /**
     * Prevents instantiation.
     */
    private OrgUnitTestCommands() {
    }

    public static CreateOrgUnit createOrgUnit(OrgUnitId id) {
        return CreateOrgUnitVBuilder.newBuilder()
                                    .setId(id)
                                    .setDisplayName(orgUnitName())
                                    .setDomain(orgUnitDomain())
                                    .setParentEntity(orgUnitParentEntity())
                                    .build();

    }

    public static DeleteOrgUnit deleteOrgUnit(OrgUnitId id) {
        return DeleteOrgUnitVBuilder.newBuilder()
                                    .setId(id)
                                    .build();
    }

    public static MoveOrgUnit moveOrgUnit(OrgUnitId id) {
        return MoveOrgUnitVBuilder.newBuilder()
                                  .setId(id)
                                  .setNewParentEntity(newOrgUnitParentEntity())
                                  .build();
    }

    public static RenameOrgUnit renameOrgUnit(OrgUnitId id) {
        return RenameOrgUnitVBuilder.newBuilder()
                                    .setId(id)
                                    .setNewName(orgUnitNewName())
                                    .build();
    }

    public static ChangeOrgUnitDomain changeOrgUnitDomain(OrgUnitId id) {
        return ChangeOrgUnitDomainVBuilder.newBuilder()
                                          .setId(id)
                                          .setNewDomain(orgUnitNewDomain())
                                          .build();
    }
}
