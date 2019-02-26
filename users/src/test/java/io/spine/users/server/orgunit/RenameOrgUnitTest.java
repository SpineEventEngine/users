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

import io.spine.users.orgunit.command.RenameOrgUnit;
import io.spine.users.orgunit.event.OrgUnitRenamed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.orgunit.given.OrgUnitTestCommands.renameOrgUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("RenameOrgUnit command should")
class RenameOrgUnitTest extends OrgUnitCommandTest<RenameOrgUnit> {

    RenameOrgUnitTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("produce OrgUnitRenamed event")
    void produceEvent() {
        OrgUnitAggregate aggregate = TestOrgUnitFactory.createAggregate(ORG_UNIT_ID);
        String oldName = aggregate.state()
                                  .getDisplayName();
        expectThat(aggregate).producesEvent(OrgUnitRenamed.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getNewName(), event.getNewName());
            assertEquals(oldName, event.getOldName());
        });
    }

    @Test
    @DisplayName("rename orgunit")
    void changeState() {
        OrgUnitAggregate aggregate = TestOrgUnitFactory.createAggregate(ORG_UNIT_ID);

        expectThat(aggregate).hasState(state -> {
            assertEquals(message().getNewName(), state.getDisplayName());
        });
    }

    private static RenameOrgUnit createMessage() {
        return renameOrgUnit(ORG_UNIT_ID);
    }
}
