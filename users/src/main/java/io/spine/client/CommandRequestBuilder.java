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

import io.spine.annotation.Experimental;
import io.spine.base.CommandMessage;
import io.spine.base.EventMessage;
import io.spine.core.Command;

import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Allows to post a command optionally subscribing to events that are immediate results
 * of handling this command.
 */
@Experimental
public final class CommandRequestBuilder extends RequestBuilder {

    private final CommandMessage commandMessage;
    private final EventConsumers.Builder builder;

    CommandRequestBuilder(RequestBuilder parent, CommandMessage c) {
        super(parent.user(), parent.client());
        this.commandMessage = c;
        this.builder = EventConsumers.newBuilder();
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
    public <E extends EventMessage> CommandRequestBuilder
    observe(Class<E> type, Consumer<E> consumer) {
        checkNotNull(consumer);
        builder.observe(type, consumer);
        return this;
    }

    /**
     * Adds the passed event consumer to the subscribers of the event of the passed type.
     *
     * @param type
     *          the type of the event message to be received by the consumer
     * @param consumer
     *          the consumer of the event message and its context
     * @param <E>
     *          the type of the event
     */
    public <E extends EventMessage> CommandRequestBuilder
    observe(Class<E> type, EventConsumer<E> consumer) {
        checkNotNull(consumer);
        builder.observe(type, consumer);
        return this;
    }

    /**
     * Subscribes the consumers to events to receive events resulting from the command as
     * the happen, then sends the command to the server.
     *
     * <p>The returned {@code Subscription} instance should be
     * {@linkplain Client#cancel(Subscription) canceled} after the requesting code receives all
     * the expected events, or after a reasonable timeout.
     *
     * @return the subscription to all the events
     * @implNote Subscriptions should be cancelled to free up client and server resources required
     * for their maintenance. It is not possible to cancel the returned subscription in an automatic
     * way because of the following. Subscriptions by nature are asynchronous and infinite requests.
     * Even that we know expected types of the events produced by the command, only the
     * client code “knows” how many of them it expects. Also, some events may not arrive because of
     * communication of business logic reasons. That's why the returned subscription should be
     * cancelled by the client code when it no longer needs it.
     */
    public Subscription post() {
        EventConsumers consumers = builder.build();
        Client client = client();
        Command command =
                client.requestOf(user())
                      .command()
                      .create(this.commandMessage);
        Subscription result = EventsAfterCommand.subscribe(client, command, consumers);
        client().postCommand(command);
        return result;
    }
}
