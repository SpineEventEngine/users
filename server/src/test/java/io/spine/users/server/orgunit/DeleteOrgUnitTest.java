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

package io.spine.users.server.orgunit;

import io.spine.users.OrgUnitId;
import io.spine.users.orgunit.OrgUnit;
import io.spine.users.orgunit.command.DeleteOrgUnit;
import io.spine.users.orgunit.event.OrgUnitDeleted;
import io.spine.users.server.OrgUnitCommandTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.orgunit.given.OrgUnitTestCommands.deleteOrgUnit;

@DisplayName("`DeleteOrgUnit` command should")
class DeleteOrgUnitTest extends OrgUnitCommandTest<DeleteOrgUnit, OrgUnitDeleted> {

    @Test
    @DisplayName("produce `OrgUnitDeleted` event and delete the org.unit")
    @Override
    protected void produceEventAndChangeState() {
        preCreateOrgUnit();
        super.produceEventAndChangeState();
    }

    @Override
    protected DeleteOrgUnit command(OrgUnitId id) {
        return deleteOrgUnit(id);
    }

    @Override
    protected OrgUnitDeleted expectedEventAfter(DeleteOrgUnit command) {
        return OrgUnitDeleted
                .newBuilder()
                .setId(command.getId())
                .build();
    }

    @Override
    protected OrgUnit expectedStateAfter(DeleteOrgUnit command) {
        return OrgUnit
                .newBuilder()
                .setId(command.getId())
                .build();
    }

    @Override
    protected boolean isDeletedAfterCommand() {
        return true;
    }
}