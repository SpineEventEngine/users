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
import io.spine.users.orgunit.command.ChangeOrgUnitDomain;
import io.spine.users.orgunit.event.OrgUnitDomainChanged;
import io.spine.users.server.OrgUnitCommandTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.orgunit.given.OrgUnitTestCommands.changeOrgUnitDomain;

@DisplayName("`ChangeOrgUnitDomain` command should")
class ChangeOrgUnitDomainTest
        extends OrgUnitCommandTest<ChangeOrgUnitDomain, OrgUnitDomainChanged> {

    @Override
    @Test
    @DisplayName("produce `OrgUnitDomainChanged` event and update the org.unit domain")
    protected void produceEventAndChangeState() {
        preCreateOrgUnit();
        super.produceEventAndChangeState();
    }

    @Override
    protected ChangeOrgUnitDomain command(OrgUnitId id) {
        return changeOrgUnitDomain(id);
    }

    @Override
    protected OrgUnitDomainChanged expectedEventAfter(ChangeOrgUnitDomain command) {
        return OrgUnitDomainChanged
                .newBuilder()
                .setId(command.getId())
                .setNewDomain(command.getNewDomain())
                .build();
    }

    @Override
    protected OrgUnit expectedStateAfter(ChangeOrgUnitDomain command) {
        return OrgUnit
                .newBuilder()
                .setId(command.getId())
                .setDomain(command.getNewDomain())
                .build();
    }
}
