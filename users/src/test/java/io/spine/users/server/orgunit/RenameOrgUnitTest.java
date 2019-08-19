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
import io.spine.users.orgunit.command.RenameOrgUnit;
import io.spine.users.orgunit.event.OrgUnitRenamed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.orgunit.given.OrgUnitTestCommands.renameOrgUnit;

@DisplayName("`RenameOrgUnit` command should")
class RenameOrgUnitTest extends OrgUnitCommandTest<RenameOrgUnit, OrgUnitRenamed> {

    @Test
    @DisplayName("produce `OrgUnitRenamed` event and update the org.unit display name")
    @Override
    protected void produceEventAndChangeState() {
        preCreateOrgUnit();
        super.produceEventAndChangeState();
    }

    @Override
    protected RenameOrgUnit command(OrgUnitId id) {
        return renameOrgUnit(id);
    }

    @Override
    protected OrgUnitRenamed expectedEventAfter(RenameOrgUnit command) {
        return OrgUnitRenamed
                .newBuilder()
                .setId(command.getId())
                .setNewName(command.getNewName())
                .build();
    }

    @Override
    protected OrgUnit expectedStateAfter(RenameOrgUnit command) {
        return OrgUnit
                .newBuilder()
                .setId(command.getId())
                .setDisplayName(command.getNewName())
                .build();
    }
}
