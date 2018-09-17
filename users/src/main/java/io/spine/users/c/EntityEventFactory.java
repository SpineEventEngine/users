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

package io.spine.users.c;

import io.spine.core.ActorContext;
import io.spine.time.LocalDateTimes;
import io.spine.time.ZoneId;
import io.spine.time.ZonedDateTime;
import io.spine.time.ZonedDateTimes;

/**
 * The implementation base for aggregate event factories.
 *
 * <p>An event factory is used to extract boilerplate code for creating events out of aggregates.
 *
 * @author Vladyslav Lubenskyi
 */
public class EntityEventFactory {

    private final ActorContext actorContext;

    protected EntityEventFactory(ActorContext actorContext) {
        this.actorContext = actorContext;
    }

    /**
     * Returns the current date and time in the timezone of the actor.
     *
     * @return the current date and time in the actor's timezone
     */
    protected ZonedDateTime now() {
        ZoneId zoneId = actorContext().getZoneId();
        return ZonedDateTimes.of(LocalDateTimes.now(), zoneId);
    }

    /**
     * Obtains the current {@linkplain ActorContext actor context}.
     */
    private ActorContext actorContext() {
        return actorContext;
    }
}
