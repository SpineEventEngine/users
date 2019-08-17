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

import io.spine.core.TenantId;
import io.spine.testing.server.blackbox.SingleTenantBlackBoxContext;
import io.spine.users.OrganizationId;
import io.spine.users.organization.Organization;
import io.spine.users.organization.command.ChangeOrganizationDomain;
import io.spine.users.organization.command.ChangeOrganizationTenant;
import io.spine.users.organization.event.OrganizationDomainChanged;
import io.spine.users.organization.event.OrganizationTenantChanged;
import io.spine.users.server.UsersContextTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.organization.given.OrganizationTestCommands.changeOrganizationDomain;
import static io.spine.users.server.organization.given.OrganizationTestCommands.changeOrganizationTenant;
import static io.spine.users.server.organization.given.OrganizationTestEnv.createOrganizationId;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("`ChangeOrganizationTenant` command should")
class ChangeOrganizationTenantTest extends UsersContextTest {

    @Test
    @DisplayName("produce `OrganizationTenantChanged` event and update the organization tenant")
    void produceEventAndChangeState() {
        OrganizationId id = createOrganizationId();
        ChangeOrganizationTenant command = changeOrganizationTenant(id);
        SingleTenantBlackBoxContext afterCommand = context().receivesCommand(command);
        OrganizationTenantChanged expectedEvent = expectedEvent(command);
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

    private static Organization expectedState(ChangeOrganizationTenant command) {
        return Organization
                .newBuilder()
                .setId(command.getId())
                .setTenant(command.getNewTenant())
                .build();
    }

    private static OrganizationTenantChanged expectedEvent(ChangeOrganizationTenant command) {
        return OrganizationTenantChanged
                .newBuilder()
                .setId(command.getId())
                .setNewTenant(command.getNewTenant())
                .build();
    }
}
