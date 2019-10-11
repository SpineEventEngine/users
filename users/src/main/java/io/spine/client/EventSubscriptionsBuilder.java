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

package io.spine.client;

import io.spine.base.EventMessage;
import io.spine.core.Command;
import io.spine.core.EventContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Allows the client code to subscribe to events originating from a command.
 */
public final class EventSubscriptionsBuilder {

    private final Client client;
    private final Command command;

    private final Map<? super EventMessage, Consumer<? extends EventMessage>>
            consumers = new HashMap<>();
    private final Map<? extends EventMessage, BiConsumer<? extends EventMessage, EventContext>>
            biConsumers = new HashMap<>();

    EventSubscriptionsBuilder(Client client, Command command) {
        this.client = client;
        this.command = command;
    }

    public <E extends EventMessage> EventSubscriptionsBuilder
    observe(Class<E> type, Consumer<E> consumer) {
        checkNotNull(consumer);
        consumers.put(type, consumer);
        return this;
    }

    public <E extends EventMessage> EventSubscriptionsBuilder
    observeWithContext(Class<E> type, BiConsumer<E, EventContext> consumer) {
        checkNotNull(consumer);
        biConsumers.add(consumer);
        return this;
    }

    public void post() {
        consumers.forEach(consumer -> new EventSubscription<?>(client, command));

        //TODO:2019-10-11:alexander.yevsyukov: Subscribe to events using consumers and biconsumers.
        client.postCommand(command);
    }
}
