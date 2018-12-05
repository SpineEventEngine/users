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

package io.spine.users.orgunit;

import io.spine.users.orgunit.command.DeleteOrgUnit;
import io.spine.users.orgunit.event.OrgUnitDeleted;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.orgunit.TestOrgUnitFactory.createAggregate;
import static io.spine.users.orgunit.given.OrgUnitTestCommands.deleteOrgUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("DeleteOrgUnit command should")
class DeleteOrgUnitTest extends OrgUnitCommandTest<DeleteOrgUnit> {

    DeleteOrgUnitTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("produce OrgUnitDeleted event")
    void produceEvent() {
        OrgUnitAggregate aggregate = createAggregate(ORG_UNIT_ID);
        expectThat(aggregate).producesEvent(OrgUnitDeleted.class, event -> {
            assertEquals(message().getId(), event.getId());
        });
    }

    @Test
    @DisplayName("delete the orgunit")
    void changeState() {
        OrgUnitAggregate aggregate = createAggregate(ORG_UNIT_ID);

        expectThat(aggregate).hasState(state -> assertTrue(aggregate.getLifecycleFlags()
                                                                    .getDeleted()));
    }

    private static DeleteOrgUnit createMessage() {
        return deleteOrgUnit(ORG_UNIT_ID);
    }
}
