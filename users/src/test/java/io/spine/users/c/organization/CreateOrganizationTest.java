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

package io.spine.users.c.organization;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.c.organization.TestOrganizationFactory.createEmptyAggregate;
import static io.spine.users.c.organization.given.OrganizationTestCommands.createOrganization;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings("Duplicates") // We perform the same assertions for resulting event and state
@DisplayName("CreateOrganization command should")
class CreateOrganizationTest extends OrgCommandTest<CreateOrganization> {

    CreateOrganizationTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("produce OrganizationCreated event")
    void produceEvent() {
        CreateOrganization command = message();
        expectThat(createEmptyAggregate(ORG_ID)).producesEvent(OrganizationCreated.class, event -> {
            assertEquals(command.getId(), event.getId());
            assertEquals(command.getDisplayName(), event.getDisplayName());
            assertEquals(command.getTenant(), event.getTenant());
            assertEquals(command.getAttributesMap(), event.getAttributesMap());
            assertEquals(command.getOwner(), event.getOwner());
            assertEquals(command.getDomain(), event.getDomain());
        });
    }

    @Test
    @DisplayName("create an organization")
    void changeState() {
        CreateOrganization command = message();
        expectThat(createEmptyAggregate(ORG_ID)).hasState(state -> {
            assertEquals(command.getId(), state.getId());
            assertEquals(command.getDisplayName(), state.getDisplayName());
            assertEquals(command.getTenant(), state.getTenant());
            assertEquals(command.getAttributesMap(), state.getAttributesMap());
            assertEquals(command.getOwner(), state.getOwner());
            assertEquals(command.getDomain(), state.getDomain());
        });
    }

    private static CreateOrganization createMessage() {
        return createOrganization(ORG_ID);
    }
}
