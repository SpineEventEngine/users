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

import io.spine.client.Query;
import io.spine.testing.client.TestActorRequestFactory;
import io.spine.testing.server.blackbox.SingleTenantBlackBoxContext;
import io.spine.users.OrgUnitId;
import io.spine.users.orgunit.OrgUnit;
import io.spine.users.orgunit.command.CreateOrgUnit;
import io.spine.users.orgunit.command.DeleteOrgUnit;
import io.spine.users.orgunit.event.OrgUnitDeleted;
import io.spine.users.server.UsersContextTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.client.Filters.all;
import static io.spine.client.Filters.eq;
import static io.spine.users.server.orgunit.given.OrgUnitTestCommands.createOrgUnit;
import static io.spine.users.server.orgunit.given.OrgUnitTestCommands.deleteOrgUnit;
import static io.spine.users.server.orgunit.given.OrgUnitTestEnv.createOrgUnitId;

@DisplayName("`DeleteOrgUnit` command should")
class DeleteOrgUnitTest extends UsersContextTest {

    @Test
    @DisplayName("produce `OrgUnitDeleted` event and delete the org.unit")
    void produceEventAndChangeState() {
        OrgUnitId id = createOrgUnitId();
        CreateOrgUnit createCmd = createOrgUnit(id);
        DeleteOrgUnit deleteCmd = deleteOrgUnit(id);
        SingleTenantBlackBoxContext afterCommand = context().receivesCommands(createCmd, deleteCmd);
        OrgUnitDeleted expectedEvent = expectedEvent(deleteCmd);
        afterCommand.assertEvents()
                    .message(0)
                    .comparingExpectedFieldsOnly()
                    .isEqualTo(expectedEvent);
        Query findDeleted = findDeleted(id);
        afterCommand
                .assertQueryResult(findDeleted)
                .containsSingleEntityStateThat()
                .comparingExpectedFieldsOnly()
                .isEqualTo(expectedState(deleteCmd));
    }

    private static Query findDeleted(OrgUnitId id) {
        TestActorRequestFactory factory = new TestActorRequestFactory(DeleteOrgUnitTest.class);
        return factory
                .query()
                .select(OrgUnit.class)
                .where(all(eq("id", id), eq("deleted", true)))
                .build();
    }

    private static OrgUnitDeleted expectedEvent(DeleteOrgUnit command) {
        return OrgUnitDeleted
                .newBuilder()
                .setId(command.getId())
                .build();
    }

    private static OrgUnit expectedState(DeleteOrgUnit command) {
        return OrgUnit
                .newBuilder()
                .setId(command.getId())
                .build();
    }
}
