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
import io.spine.users.orgunit.command.CreateOrgUnit;
import io.spine.users.orgunit.event.OrgUnitCreated;
import io.spine.users.server.OrgUnitCommandTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.orgunit.given.OrgUnitTestCommands.createOrgUnit;

@DisplayName("`CreateOrgUnit` command should")
class CreateOrgUnitTest
        extends OrgUnitCommandTest<CreateOrgUnit, OrgUnitCreated> {

    @Override
    @Test
    @DisplayName("produce `OrgUnitCreated` event and create the org.unit")
    protected void produceEventAndChangeState() {
        super.produceEventAndChangeState();
    }

    @Override
    protected CreateOrgUnit command(OrgUnitId id) {
        return createOrgUnit(id);
    }

    @Override
    protected OrgUnitCreated expectedEventAfter(CreateOrgUnit command) {
        return OrgUnitCreated
                .newBuilder()
                .setId(command.getId())
                .setDisplayName(command.getDisplayName())
                .setDomain(command.getDomain())
                .setParentEntity(command.getParentEntity())
                .build();
    }

    @Override
    protected OrgUnit expectedStateAfter(CreateOrgUnit command) {
        return OrgUnit
                .newBuilder()
                .setId(command.getId())
                .setDisplayName(command.getDisplayName())
                .setDomain(command.getDomain())
                .setParentEntity(command.getParentEntity())
                .build();
    }
}