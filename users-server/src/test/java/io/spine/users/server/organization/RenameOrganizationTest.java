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
import io.spine.users.organization.command.RenameOrganization;
import io.spine.users.organization.event.OrganizationRenamed;
import io.spine.users.server.OrganizationCommandTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.organization.given.OrganizationTestCommands.renameOrganization;

@DisplayName("`RenameOrganization` command should")
class RenameOrganizationTest
        extends OrganizationCommandTest<RenameOrganization, OrganizationRenamed> {

    @Test
    @DisplayName("produce `RenameOrganization` event and update the organization display name")
    @Override
    protected void produceEventAndChangeState() {
        preCreateOrganization();
        super.produceEventAndChangeState();
    }

    @Override
    protected RenameOrganization command(OrganizationId id) {
        return renameOrganization(id);
    }

    @Override
    protected OrganizationRenamed expectedEventAfter(RenameOrganization command) {
        return OrganizationRenamed
                .newBuilder()
                .setId(command.getId())
                .setNewName(command.getNewName())
                .build();
    }

    @Override
    protected Organization expectedStateAfter(RenameOrganization command) {
        return Organization
                .newBuilder()
                .setId(command.getId())
                .setDisplayName(command.getNewName())
                .build();
    }
}
