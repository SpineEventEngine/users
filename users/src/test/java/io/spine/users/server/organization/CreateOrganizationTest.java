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
import io.spine.users.organization.command.CreateOrganization;
import io.spine.users.organization.event.OrganizationCreated;
import io.spine.users.server.UsersContextTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.organization.given.OrganizationTestCommands.createOrganization;
import static io.spine.users.server.organization.given.OrganizationTestEnv.createOrganizationId;

@DisplayName("`CreateOrganization` command should")
class CreateOrganizationTest extends UsersContextTest {

    @Test
    @DisplayName("produce `OrganizationCreated` event and create the organization")
    void produceEventAndChangeState() {
        OrganizationId id = createOrganizationId();
        CreateOrganization command = createOrganization(id);
        SingleTenantBlackBoxContext afterCommand = context().receivesCommand(command);
        OrganizationCreated expectedEvent = expectedEvent(command);
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

    private static Organization expectedState(CreateOrganization command) {
        return Organization
                .newBuilder()
                .setId(command.getId())
                .setDisplayName(command.getDisplayName())
                .setDomain(command.getDomain())
                .setTenant(command.getTenant())
                .build();
    }

    private static OrganizationCreated expectedEvent(CreateOrganization command) {
        return OrganizationCreated
                .newBuilder()
                .setId(command.getId())
                .setDisplayName(command.getDisplayName())
                .setDomain(command.getDomain())
                .setTenant(command.getTenant())
                .build();
    }
}
