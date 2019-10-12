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

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.spine.base.EventMessage;
import io.spine.core.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Allows the client code to subscribe to events originating from a command.
 */
public final class EventSubscriptionsBuilder {

    private final Client client;
    private final Command command;

    private final
    Map<Class<? extends EventMessage>, EventConsumer<? extends EventMessage>>
            consumers = new HashMap<>();

    EventSubscriptionsBuilder(Client client, Command command) {
        this.client = client;
        this.command = command;
    }

    /**
     * Adds the passed consumer to the subscribers of the event of the passed type.
     *
     * @param type
     *          the type of the event message to be received by the consumer
     * @param consumer
     *          the consumer
     * @param <E>
     *          the type of the event
     */
    public <E extends EventMessage> EventSubscriptionsBuilder
    observe(Class<E> type, Consumer<E> consumer) {
        checkNotNull(consumer);
        consumers.put(type, EventConsumer.fromConsumer(consumer));
        return this;
    }

    /**
     * Adds the passed bi-consumer to the subscribers of the event of the passed type.
     *
     * @param type
     *          the type of the event message to be received by the consumer
     * @param consumer
     *          the consumer of the event message and its context
     * @param <E>
     *          the type of the event
     */
    public <E extends EventMessage> EventSubscriptionsBuilder
    observe(Class<E> type, EventConsumer<E> consumer) {
        checkNotNull(consumer);
        consumers.put(type, consumer);
        return this;
    }

    /**
     * Creates subscriptions for all the consumers and then posts the command.
     */
    public void post() {
        consumers.forEach(this::createSubscription);
        client.postCommand(command);
    }

    @SuppressWarnings("unchecked")
    /* The type of the event and is matched to the consumer when adding map entries. */
    @CanIgnoreReturnValue
    private EventSubscription
    createSubscription(Class<? extends EventMessage> eventType,
                       EventConsumer<? extends EventMessage> consumer) {
        return new EventSubscription(client, command, eventType, consumer);
    }
}
