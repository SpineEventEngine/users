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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.grpc.stub.StreamObserver;
import io.spine.base.EventMessage;
import io.spine.core.Event;
import io.spine.core.EventContext;
import io.spine.logging.Logging;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.protobuf.TextFormat.shortDebugString;

/**
 * An association of event types to their consumers which also delivers events.
 *
 * <p>A consumer of an event can accept {@linkplain EventConsumer event message and its context}
 * or {@linkplain java.util.function.Consumer only event message}.
 */
public final class EventConsumers implements Logging {

    private final
    ImmutableMultimap<Class<? extends EventMessage>, EventConsumer<? extends EventMessage>> map;

    public static Builder newBuilder() {
        return new Builder();
    }

    private EventConsumers(Builder builder) {
        this.map = ImmutableMultimap.copyOf(builder.map);
    }

    /**
     * Verifies if there is at least one consumer for the passed event type.
     */
    public <E extends EventMessage> boolean contains(Class<E> eventType) {
        boolean result = map.containsKey(eventType);
        return result;
    }

    /**
     * Performs the passed actions for all the consumers.
     */
    public void
    forEach(BiConsumer<? super Class<? extends EventMessage>,
            ? super EventConsumer<? extends EventMessage>> action) {
        checkNotNull(action);
        map.forEach(action);
    }

    /**
     * Creates an observer that would deliver events to all the consumers.
     *
     * <p>Errors that may occur when delivering events will be logged. In order to handle
     * the errors, please use the method which accepts {@code Consumer<Throwable>}.
     *
     * @see #toObserver(Consumer)
     */
    public StreamObserver<Event> toObserver() {
        return new EventObserver(null);
    }

    /**
     * Creates an observer that would deliver events to all the consumers.
     *
     * @param errorHandler
     *         the handler for possible errors reported by the server.
     *         If null the error will be simply logged.
     */
    public StreamObserver<Event> toObserver(@Nullable Consumer<Throwable> errorHandler) {
        return new EventObserver(errorHandler);
    }

    /**
     * Delivers the event to all the subscribed consumers.
     *
     * <p>If one of the consumers would cause an error when handling the event, the error will
     * be logged, and the event will be passed to remaining consumers.
     */
    public void deliver(Event event) {
        EventMessage message = event.enclosedMessage();
        EventContext context = event.getContext();
        Class<? extends EventMessage> type = message.getClass();
        ImmutableCollection<EventConsumer<? extends EventMessage>> consumers = map.get(type);
        consumers.forEach(c -> {
            try {
                @SuppressWarnings("unchecked") // Safe as we match the type when adding consumers.
                EventConsumer<EventMessage> consumer = (EventConsumer<EventMessage>) c;
                consumer.accept(message, context);
            } catch (Throwable throwable) {
                logError(c, event, throwable);
            }
        });
    }

    /**
     * Logs the fact that the passed event consumer cased the error.
     *
     * <p>If the passed consumer is an instance of {@code DelegatingEventConsumer}
     * the real consumer will be reported in the log.
     */
    private void logError(EventConsumer consumer, Event event, Throwable throwable) {
        String eventDiags = shortDebugString(event);
        Object consumerToReport = consumer instanceof DelegatingEventConsumer
            ? ((DelegatingEventConsumer) consumer).delegate()
            : consumer;
        _error().withCause(throwable)
                .log("The consumer `%s` could not handle the event `%s`.",
                     consumerToReport, eventDiags);
    }

    /**
     * The builder for {@link EventConsumers}.
     */
    public static final class Builder {

        private final
        Multimap<Class<? extends EventMessage>, EventConsumer<? extends EventMessage>>
                map = HashMultimap.create();

        /**
         * Adds the consumer of the events message.
         */
        @CanIgnoreReturnValue
        public <E extends EventMessage>
        Builder observe(Class<E> eventType, Consumer<E> consumer) {
            checkNotNull(eventType);
            checkNotNull(consumer);
            map.put(eventType, EventConsumer.fromConsumer(consumer));
            return this;
        }

        /**
         * Adds the consumer of the event message and its context.
         */
        @CanIgnoreReturnValue
        public <E extends EventMessage>
        Builder observe(Class<E> eventType, EventConsumer<E> consumer) {
            checkNotNull(eventType);
            checkNotNull(consumer);
            map.put(eventType, consumer);
            return this;
        }

        /**
         * Creates the new instance.
         */
        public EventConsumers build() {
            return new EventConsumers(this);
        }
    }

    /**
     * Passes the event to listener once the subscription is updated, then cancels the subscription.
     */
    private final class EventObserver implements StreamObserver<Event> {

        private final Consumer<Throwable> errorHandler;

        private EventObserver(@Nullable Consumer<Throwable> handler) {
            this.errorHandler = nullToDefault(handler);
        }

        private Consumer<Throwable> nullToDefault(@Nullable Consumer<Throwable> handler) {
            if (handler != null) {
                return handler;
            }
            return throwable -> _error().withCause(throwable).log("Error receiving event.");
        }

        @Override
        public void onNext(Event e) {
            deliver(e);
        }

        @Override
        public void onError(Throwable t) {
            errorHandler.accept(t);
        }

        @Override
        public void onCompleted() {
            // Do nothing.
        }
    }
}
