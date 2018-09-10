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
import static io.spine.users.c.organization.given.OrganizationTestCommands.renameOrganization;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("RenameOrganization command should")
class RenameOrganizationTest extends OrgCommandTest<RenameOrganization> {

    RenameOrganizationTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("produce OrganizationRenamed event")
    void produceEvent() {
        OrganizationAggregate aggregate = createAggregate(ORG_ID);
        String oldName = aggregate.getState()
                                  .getDisplayName();
        expectThat(aggregate).producesEvent(OrganizationRenamed.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getNewName(), event.getNewName());
            assertEquals(oldName, event.getOldName());
        });
    }

    @Test
    @DisplayName("change the name")
    void changeState() {
        OrganizationAggregate aggregate = createAggregate(ORG_ID);
        expectThat(aggregate).hasState(state -> {
            assertEquals(state.getDisplayName(), message().getNewName());
        });
    }

    private static RenameOrganization createMessage() {
        return renameOrganization(ORG_ID);
    }
}
