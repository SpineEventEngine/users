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

import io.grpc.stub.StreamObserver;
import io.spine.base.EventMessage;
import io.spine.base.Field;
import io.spine.core.Command;
import io.spine.core.Event;
import io.spine.core.EventContext;
import io.spine.core.UserId;
import io.spine.logging.Logging;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.protobuf.TextFormat.shortDebugString;
import static io.spine.client.Filters.eq;
import static io.spine.validate.Validate.isDefault;
import static java.lang.String.format;

/**
 * Subscribes to events of the given type which originate from the given command.
 *
 * <p>If the command produces more than one event, all they will be delivered to the consumer
 * of the subscription.
 *
 * <p>Once the events are delivered the subscription is cancelled. This done so because the
 * subscription was made only for the passed command (which has a unique ID),
 * and no more updates would be sent after ever.
 *
 * @param <E>
 *         the type of the event
 */
final class EventAfterCommandSubscription<E extends EventMessage> implements Logging {

    private final Client client;
    private final UserId user;
    private final Command command;
    private final Class<E> eventType;
    private final Subscription subscription;
    private final EventConsumer<E> consumer;

    EventAfterCommandSubscription(Client client,
                                  UserId user,
                                  Command command,
                                  Class<E> eventType,
                                  EventConsumer<E> consumer) {
        this.client = checkNotNull(client);
        checkArgument(!isDefault(user));
        this.user = user;
        this.command = command;
        this.eventType = checkNotNull(eventType);
        this.consumer = checkNotNull(consumer);
        this.subscription = subscribeToEventsOf(command);
    }

    /**
     * Obtains the path to the "context.past_message" field of {@code Event}.
     *
     * <p>This method is safer than using a string constant because it relies on field numbers,
     * rather than names (that might be changed).
     */
    private static String pastMessageField() {
        Field context = Field.withNumberIn(Event.CONTEXT_FIELD_NUMBER, Event.getDescriptor());
        Field pastMessage = Field.withNumberIn(EventContext.PAST_MESSAGE_FIELD_NUMBER,
                                               EventContext.getDescriptor());
        return format("%s.%s", context.toString(), pastMessage.toString());
    }

    private Subscription subscribeToEventsOf(Command command) {
        Topic topic = allEventsOf(command);
        return client.subscribeTo(topic, new EventObserver());
    }

    /**
     * Creates a subscription topic for all events for which the passed command is the origin.
     */
    private Topic allEventsOf(Command c) {
        String fieldName = pastMessageField();
        Topic topic =
                client.onBehalfOf(user)
                      .topic()
                      .select(Event.class)
                      .where(eq(fieldName, c.asMessageOrigin()))
                      .build();
        return topic;
    }

    /**
     * Passes the event to listener once the subscription is updated.
     */
    private final class EventObserver implements StreamObserver<Event> {

        @Override
        public void onNext(Event e) {
            EventMessage eventMessage = e.enclosedMessage();
            if (eventType.isAssignableFrom(eventMessage.getClass())) {
                @SuppressWarnings("unchecked") // protected by the subscription type
                        E cast = (E) eventMessage;
                consumer.accept(cast, e.context());
            }
        }

        @Override
        public void onError(Throwable t) {
            _error().withCause(t)
                    .log("Unable to get events for the command `%s`.", shortDebugString(command));
        }

        @Override
        public void onCompleted() {
            client.cancel(subscription);
        }
    }
}
