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

import static io.spine.users.c.organization.TestOrganizationFactory.createAggregate;
import static io.spine.users.c.organization.given.OrganizationTestCommands.deleteOrganization;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("DeleteOrganization command should")
class DeleteOrganizationTest extends OrgCommandTest<DeleteOrganization> {

    DeleteOrganizationTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("produce OrganizationDeleted event")
    void produceEvent() {
        OrganizationAggregate aggregate = createAggregate(ORG_ID);
        expectThat(aggregate).producesEvent(OrganizationDeleted.class, event -> {
            assertEquals(message().getId(), event.getId());
        });
    }

    @Test
    @DisplayName("delete the organization")
    void changeState() {
        OrganizationAggregate aggregate = createAggregate(ORG_ID);

        expectThat(aggregate).hasState(state -> assertTrue(aggregate.getLifecycleFlags()
                                                                    .getDeleted()));
    }

    private static DeleteOrganization createMessage() {
        return deleteOrganization(ORG_ID);
    }
}
