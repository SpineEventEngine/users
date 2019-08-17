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

package io.spine.users.server.organization;

import io.spine.testing.server.blackbox.SingleTenantBlackBoxContext;
import io.spine.users.OrganizationId;
import io.spine.users.organization.Organization;
import io.spine.users.organization.command.RenameOrganization;
import io.spine.users.organization.event.OrganizationRenamed;
import io.spine.users.server.UsersContextTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.organization.given.OrganizationTestCommands.renameOrganization;
import static io.spine.users.server.organization.given.OrganizationTestEnv.createOrganizationId;

@DisplayName("`RenameOrganization` command should")
class RenameOrganizationTest extends UsersContextTest {

    @Test
    @DisplayName("produce `OrganizationRenamed` event and update the organization display name")
    void produceEventAndChangeState() {
        OrganizationId id = createOrganizationId();
        RenameOrganization command = renameOrganization(id);
        SingleTenantBlackBoxContext afterCommand = context().receivesCommand(command);
        OrganizationRenamed expectedEvent = expectedEvent(command);
        afterCommand.assertEvents()
                    .message(0)
                    .comparingExpectedFieldsOnly()
                    .isEqualTo(expectedEvent);

        Organization expectedState = expectedState(command);
        afterCommand.assertEntity(OrganizationAggregate.class, id)
                    .hasStateThat()
                    .comparingExpectedFieldsOnly()
                    .isEqualTo(expectedState);
    }

    private static Organization expectedState(RenameOrganization command) {
        return Organization
                .newBuilder()
                .setId(command.getId())
                .setDisplayName(command.getNewName())
                .build();
    }

    private static OrganizationRenamed expectedEvent(RenameOrganization command) {
        return OrganizationRenamed
                .newBuilder()
                .setId(command.getId())
                .setNewName(command.getNewName())
                .build();
    }
}
