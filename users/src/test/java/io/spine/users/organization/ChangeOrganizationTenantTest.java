/*
 * Copyright 2018, TeamDev. All rights reserved.
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

package io.spine.users.organization;

import io.spine.core.TenantId;
import io.spine.users.organization.command.ChangeOrganizationTenant;
import io.spine.users.organization.event.OrganizationTenantChanged;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.organization.TestOrganizationFactory.createAggregate;
import static io.spine.users.organization.given.OrganizationTestCommands.changeOrganizationTenant;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("ChangeOrganizationTenant command should")
class ChangeOrganizationTenantTest extends OrgCommandTest<ChangeOrganizationTenant> {

    ChangeOrganizationTenantTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("produce OrganizationTenantChanged event")
    void produceEvent() {
        OrganizationAggregate aggregate = createAggregate(ORG_ID);
        TenantId oldTenant = aggregate.getState()
                                      .getTenant();
        expectThat(aggregate).producesEvent(OrganizationTenantChanged.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getNewTenant(), event.getNewTenant());
            assertEquals(oldTenant, event.getOldTenant());
        });
    }

    @Test
    @DisplayName("change the tenant")
    void changeState() {
        OrganizationAggregate aggregate = createAggregate(ORG_ID);

        expectThat(aggregate).hasState(state -> {
            assertEquals(message().getNewTenant(), state.getTenant());
        });
    }

    private static ChangeOrganizationTenant createMessage() {
        return changeOrganizationTenant(ORG_ID);
    }
}
