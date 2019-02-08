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

package io.spine.users.server.orgunit;

import io.spine.users.orgunit.command.CreateOrgUnit;
import io.spine.users.orgunit.event.OrgUnitCreated;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.orgunit.given.OrgUnitTestCommands.createOrgUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings("Duplicates") // We perform the same assertions for resulting event and state
@DisplayName("CreateOrgUnit command should")
class CreateOrgUnitTest extends OrgUnitCommandTest<CreateOrgUnit> {

    CreateOrgUnitTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("produce OrgUnitCreated event")
    void produceEvent() {
        CreateOrgUnit command = message();
        expectThat(
                TestOrgUnitFactory.createEmptyAggregate(ORG_UNIT_ID)).producesEvent(OrgUnitCreated.class, event -> {
            assertEquals(command.getId(), event.getId());
            assertEquals(command.getDisplayName(), event.getDisplayName());
            assertEquals(command.getParentEntity(), event.getParentEntity());
            assertEquals(command.getDomain(), event.getDomain());
        });
    }

    @Test
    @DisplayName("create an orgunit")
    void changeState() {
        CreateOrgUnit command = message();
        expectThat(TestOrgUnitFactory.createEmptyAggregate(ORG_UNIT_ID)).hasState(state -> {
            assertEquals(command.getId(), state.getId());
            assertEquals(command.getDisplayName(), state.getDisplayName());
            assertEquals(command.getParentEntity(), state.getParentEntity());
            assertEquals(command.getDomain(), state.getDomain());
        });
    }

    private static CreateOrgUnit createMessage() {
        return createOrgUnit(ORG_UNIT_ID);
    }
}
