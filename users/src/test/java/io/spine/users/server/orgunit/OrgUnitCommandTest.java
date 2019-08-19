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

import io.spine.base.CommandMessage;
import io.spine.base.EventMessage;
import io.spine.users.OrgUnitId;
import io.spine.users.orgunit.OrgUnit;
import io.spine.users.server.CommandTest;

import static io.spine.users.server.orgunit.given.OrgUnitTestCommands.createOrgUnit;
import static io.spine.users.server.orgunit.given.OrgUnitTestEnv.createOrgUnitId;

/**
 * An abstract base for the tests of {@code OrgUnit} commands and their implications.
 *
 * @param <C>
 *         the type of the tested command
 * @param <E>
 *         the type of the expected event
 */
abstract class OrgUnitCommandTest<C extends CommandMessage, E extends EventMessage>
        extends CommandTest<OrgUnitId, C, E, OrgUnit, OrgUnitAggregate> {

    private static final OrgUnitId ORG_UNIT_ID = createOrgUnitId();

    @Override
    protected OrgUnitId entityId() {
        return ORG_UNIT_ID;
    }

    @Override
    protected Class<OrgUnitAggregate> entityClass() {
        return OrgUnitAggregate.class;
    }

    protected void preCreateOrgUnit() {
        context().receivesCommand(createOrgUnit(ORG_UNIT_ID));
    }
}
