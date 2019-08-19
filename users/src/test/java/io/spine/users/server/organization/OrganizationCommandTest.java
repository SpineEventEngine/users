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

import io.spine.base.CommandMessage;
import io.spine.base.EventMessage;
import io.spine.users.OrganizationId;
import io.spine.users.organization.Organization;
import io.spine.users.server.CommandTest;

import static io.spine.users.server.organization.given.OrganizationTestCommands.createOrganization;
import static io.spine.users.server.organization.given.OrganizationTestEnv.createOrganizationId;

/**
 * An abstract base for the tests verifying that the commands are handled by
 * {@link OrganizationAggregate} properly.
 *
 * @param <C>
 *         the type of the command dispatched to the {@code OrganizationAggregate}
 * @param <E>
 *         the type of the event expected to be emitted
 */
abstract class OrganizationCommandTest<C extends CommandMessage, E extends EventMessage>
        extends CommandTest<OrganizationId, C, E, Organization, OrganizationAggregate> {

    private static final OrganizationId ORGANIZATION_ID = createOrganizationId();

    @Override
    protected OrganizationId entityId() {
        return ORGANIZATION_ID;
    }

    @Override
    protected Class<OrganizationAggregate> entityClass() {
        return OrganizationAggregate.class;
    }

    void preCreateOrganization() {
        context().receivesCommand(createOrganization(entityId()));
    }
}
