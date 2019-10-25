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

import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.protobuf.Message;
import io.grpc.stub.StreamObserver;
import io.spine.base.MessageContext;
import io.spine.logging.Logging;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

abstract class Consumers<M extends Message, C extends MessageContext> implements Logging {

    private final ImmutableSet<MessageConsumer<M, C>> consumers;
    private final ErrorHandler streamingErrorHandler;
    private final ConsumerErrorHandler<M> consumingErrorHandler;

    Consumers(Builder<M, C, ?> builder) {
        this.consumers = builder.consumers.build();
        this.streamingErrorHandler =
                Optional.ofNullable(builder.streamingErrorHandler)
                        .orElseGet(() -> ErrorHandler.logError(
                                this.logger(),
                                "Error receiving a message of the type `%s`."
                        ));
        this.consumingErrorHandler =
                Optional.ofNullable(builder.consumingErrorHandler)
                        .orElseGet(() -> ConsumerErrorHandler.logError(
                                this.logger(),
                                "The consumer `%s` could not handle the message of the type `%s`."
                        ));
    }

    abstract StreamObserver<M> toObserver();

    /**
     * Delivers messages to all the consumers.
     *
     * <p>If a streaming error occurs, passes it to {@link Consumers#streamingErrorHandler}.
     */
    abstract class DeliveringObserver<O extends Message> implements StreamObserver<O> {

        abstract M toMessage(O outer);

        abstract C toContext(O outer);
        @Override
        public void onNext(O value) {
            M msg = toMessage(value);
            C ctx = toContext(value);
            deliver(msg, ctx);
        }

        /**
         * Delivers the message to consumers.
         *
         * <p>If a consumer method throws, the {@code Throwable} is passed
         * to {@link #consumingErrorHandler}, and the message is passed to remaining consumers.
         */
        private void deliver(M message, C context) {
            consumers.forEach(consumer -> {
                try {
                    consumer.accept(message, context);
                } catch (Throwable t) {
                    consumingErrorHandler.accept(consumer, t);
                }
            });
        }

        @Override
        public void onError(Throwable t) {
            streamingErrorHandler.accept(t);
        }

        @Override
        @SuppressWarnings("NoopMethodInAbstractClass")
        public void onCompleted() {
            // Do nothing.
        }

    }

    /**
     * The builder for the collection of consumers of messages of the specified type.
     *
     * @param <M>
     *         the type of the messages delivered to consumers
     */
    abstract static class Builder<M extends Message, C extends MessageContext,
                        B extends Builder> {

        private final ImmutableSet.Builder<MessageConsumer<M, C>> consumers =
                ImmutableSet.builder();
        private @Nullable ErrorHandler streamingErrorHandler;
        private @Nullable ConsumerErrorHandler<M> consumingErrorHandler;

        abstract B self();
        abstract Consumers<M, C> build();

        @CanIgnoreReturnValue
        B add(MessageConsumer<M, C> consumer) {
            consumers.add(checkNotNull(consumer));
            return self();
        }

        /**
         * Assigns a handler for the error reported to
         * {@link io.grpc.stub.StreamObserver#onError(Throwable)} of
         * the {@link io.grpc.stub.StreamObserver} responsible for delivering messages
         * to the consumers.
         *
         * <p>Once this handler is called, no more messages will be delivered to consumers.
         *
         * @see #onConsumingError(ConsumerErrorHandler)
         */
        @CanIgnoreReturnValue
        B onStreamingError(ErrorHandler handler) {
            streamingErrorHandler = checkNotNull(handler);
            return self();
        }

        /**
         * Assigns a handler for an error that may occur in the code of one of the consumers.
         *
         * <p>After this handler called, remaining consumers will get the message as usually.
         *
         * @see #onStreamingError(ErrorHandler)
         */
        @CanIgnoreReturnValue
        B onConsumingError(ConsumerErrorHandler<M> handler) {
            consumingErrorHandler = checkNotNull(handler);
            return self();
        }
    }
}
