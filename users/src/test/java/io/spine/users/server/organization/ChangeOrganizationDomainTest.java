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

import io.spine.users.OrganizationId;
import io.spine.users.organization.Organization;
import io.spine.users.organization.command.ChangeOrganizationDomain;
import io.spine.users.organization.event.OrganizationDomainChanged;
import io.spine.users.server.OrganizationCommandTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.organization.given.OrganizationTestCommands.changeOrganizationDomain;

@DisplayName("`ChangeOrganizationDomain` command should")
class ChangeOrganizationDomainTest
        extends OrganizationCommandTest<ChangeOrganizationDomain, OrganizationDomainChanged> {

    @Test
    @DisplayName("produce `OrganizationDomainChanged` event and update the organization domain")
    @Override
    protected void produceEventAndChangeState() {
        preCreateOrganization();
        super.produceEventAndChangeState();
    }

    @Override
    protected ChangeOrganizationDomain command(OrganizationId id) {
        return changeOrganizationDomain(id);
    }

    @Override
    protected OrganizationDomainChanged expectedEventAfter(ChangeOrganizationDomain command) {
        return OrganizationDomainChanged
                .newBuilder()
                .setId(command.getId())
                .setNewDomain(command.getNewDomain())
                .build();
    }

    @Override
    protected Organization expectedStateAfter(ChangeOrganizationDomain command) {
        return Organization
                .newBuilder()
                .setId(command.getId())
                .setDomain(command.getNewDomain())
                .build();
    }
}
