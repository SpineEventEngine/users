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
import io.spine.users.organization.command.CreateOrganization;
import io.spine.users.organization.event.OrganizationCreated;
import io.spine.users.server.OrganizationCommandTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.organization.given.OrganizationTestCommands.createOrganization;

@DisplayName("`CreateOrganization` command should")
class CreateOrganizationTest
        extends OrganizationCommandTest<CreateOrganization, OrganizationCreated> {

    @Override
    protected CreateOrganization command(OrganizationId id) {
        return createOrganization(id);
    }

    @Override
    protected OrganizationCreated expectedEventAfter(CreateOrganization command) {
        return OrganizationCreated
                .newBuilder()
                .setId(command.getId())
                .setDisplayName(command.getDisplayName())
                .setDomain(command.getDomain())
                .setTenant(command.getTenant())
                .build();
    }

    @Override
    protected Organization expectedStateAfter(CreateOrganization command) {
        return Organization
                .newBuilder()
                .setId(command.getId())
                .setDisplayName(command.getDisplayName())
                .setDomain(command.getDomain())
                .setTenant(command.getTenant())
                .build();
    }

    @Test
    @DisplayName("produce `OrganizationCreated` event and create the organization")
    @Override
    protected void produceEventAndChangeState() {
        super.produceEventAndChangeState();
    }
}
