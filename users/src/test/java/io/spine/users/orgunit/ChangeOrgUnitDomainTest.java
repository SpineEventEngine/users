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

import io.spine.net.InternetDomain;
import io.spine.users.orgunit.command.ChangeOrgUnitDomain;
import io.spine.users.orgunit.event.OrgUnitDomainChanged;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.orgunit.TestOrgUnitFactory.createAggregate;
import static io.spine.users.orgunit.given.OrgUnitTestCommands.changeOrgUnitDomain;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("ChangeOrgUnitDomain command should")
class ChangeOrgUnitDomainTest extends OrgUnitCommandTest<ChangeOrgUnitDomain> {

    ChangeOrgUnitDomainTest() {
        super(createMessage());
    }

    @Test
    @DisplayName("produce OrgUnitDomainChanged event")
    void produceEvent() {
        OrgUnitAggregate aggregate = createAggregate(ORG_UNIT_ID);
        InternetDomain oldDomain = aggregate.getState()
                                            .getDomain();
        expectThat(aggregate).producesEvent(OrgUnitDomainChanged.class, event -> {
            assertEquals(message().getId(), event.getId());
            assertEquals(message().getNewDomain(), event.getNewDomain());
            assertEquals(oldDomain, event.getOldDomain());
        });
    }

    @Test
    @DisplayName("change orgunit domain")
    void changeState() {
        OrgUnitAggregate aggregate = createAggregate(ORG_UNIT_ID);

        expectThat(aggregate).hasState(state -> {
            assertEquals(message().getNewDomain(), state.getDomain());
        });
    }

    private static ChangeOrgUnitDomain createMessage() {
        return changeOrgUnitDomain(ORG_UNIT_ID);
    }
}
