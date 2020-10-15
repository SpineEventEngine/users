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

package io.spine.users.server;

import io.spine.base.CommandMessage;
import io.spine.base.EntityState;
import io.spine.base.EventMessage;
import io.spine.server.entity.Entity;
import io.spine.testing.server.blackbox.BlackBoxContext;
import io.spine.testing.server.entity.EntitySubject;

/**
 * An abstract base for tests of {@code Users} Bounded Context, which test that a certain command
 * changes the state of some entity and leads to a certain event.
 *
 * @param <I>
 *         the type of the entity identifier
 * @param <C>
 *         the type of the command tested
 * @param <E>
 *         the type of the expected event
 * @param <S>
 *         the type of the entity state
 * @param <Z>
 *         the class of the entity
 */
public abstract class CommandTest<I, C extends CommandMessage, E extends EventMessage,
        S extends EntityState, Z extends Entity<I, S>>
        extends UsersContextTest {

    /**
     * Obtains a tested entity ID.
     */
    protected abstract I entityId();

    /**
     * Obtains a command that is dispatched to the entity.
     */
    protected abstract C command(I id);

    /**
     * Obtains an event message which is expected to be emitted after the given command
     * is dispatched to the entity.
     */
    protected abstract E expectedEventAfter(C command);

    /**
     * Obtains the entity state which is expected after the given command
     * is dispatched to the entity.
     */
    protected abstract S expectedStateAfter(C command);

    /**
     * Tells whether the entity is expected to become deleted after the command is dispatched.
     *
     * <p>By default, returns {@code false}.
     */
    protected boolean isDeletedAfterCommand() {
        return false;
    }

    /**
     * Tells whether the entity is expected to become archived after the command is dispatched.
     *
     * <p>By default, returns {@code false}.
     */
    protected boolean isArchivedAfterCommand() {
        return false;
    }

    /**
     * Obtains the class of the entity.
     */
    protected abstract Class<Z> entityClass();

    protected void produceEventAndChangeState() {
        I id = entityId();
        C command = command(id);
        BlackBoxContext afterCommand = context().receivesCommand(command);
        E expectedEvent = expectedEventAfter(command);

        assertEvent(afterCommand, expectedEvent);

        S expectedState = expectedStateAfter(command);
        afterCommand.assertState(id, expectedState);

        EntitySubject assertEntity = afterCommand.assertEntity(id, entityClass());
        assertEntity.archivedFlag()
                    .isEqualTo(isArchivedAfterCommand());
        assertEntity.deletedFlag()
                    .isEqualTo(isDeletedAfterCommand());
    }

    protected void assertEvent(BlackBoxContext afterCommand, E expectedEvent) {
        afterCommand.assertEvents()
                    .withType(expectedEvent.getClass())
                    .message(0)
                    .comparingExpectedFieldsOnly()
                    .isEqualTo(expectedEvent);
    }
}
