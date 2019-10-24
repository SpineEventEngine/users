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
import com.google.gson.reflect.TypeToken;
import com.google.protobuf.Message;
import io.grpc.stub.StreamObserver;
import io.spine.logging.Logging;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

final class Consumers<M extends Message> implements Logging {

    private final ImmutableSet<Consumer<M>> consumers;
    private final ErrorHandler streamingErrorHandler;
    private final ErrorHandler consumingErrorHandler;

    static <M extends Message> Builder<M> newBuilder() {
        return new Builder<>();
    }

    private Consumers(Builder<M> builder) {
        this.consumers = builder.consumers.build();
        Class<? super M> type = new TypeToken<M>(){}.getRawType();
        this.streamingErrorHandler = nullToDefault(
                builder.streamingErrorHandler,
                "Error receiving a message of the type `%s`.",
                type
        );
        this.consumingErrorHandler = nullToDefault(
                builder.consumingErrorHandler,
                "Error consuming the message of the type `%s`.",
                type
        );
    }

    /**
     * If the passed handler is non-null returns it; otherwise creates a handler which
     * logs a reported error.
     *
     * @param handler
     *         the handler to check
     * @param errorMessageFormat
     *         the error message containing a single placeholder ("%s") for the name of the type
     *         of delivered messages
     * @return the passed handler, if it's not {@code null} or t
     */
    private ErrorHandler nullToDefault(@Nullable ErrorHandler handler,
                                       String errorMessageFormat,
                                       Object... args) {
        if (handler != null) {
            return handler;
        }
        return throwable -> _error().withCause(throwable)
                                    .log(errorMessageFormat, args);
    }

    void deliver(M message) {
        consumers.forEach(consumer -> {
            try {
                consumer.accept(message);
            } catch (Throwable t) {
                consumingErrorHandler.accept(t);
            }
        });
    }

    StreamObserver<M> toObserver() {
        return new DeliveringObserver();
    }

    /**
     * Delivers messages to all the consumers.
     *
     * <p>If a streaming error occurs, passes it to {@link Consumers#streamingErrorHandler}.
     */
    private final class DeliveringObserver implements StreamObserver<M> {

        @Override
        public void onNext(M value) {
            deliver(value);
        }

        @Override
        public void onError(Throwable t) {
            streamingErrorHandler.accept(t);
        }

        @Override
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
    static final class Builder<M extends Message> {

        private final ImmutableSet.Builder<Consumer<M>> consumers = ImmutableSet.builder();
        private @Nullable ErrorHandler streamingErrorHandler;
        private @Nullable ErrorHandler consumingErrorHandler;

        @CanIgnoreReturnValue
        Builder<M> add(Consumer<M> consumer) {
            consumers.add(checkNotNull(consumer));
            return this;
        }

        /**
         * Assigns a handler for the error reported to
         * {@link io.grpc.stub.StreamObserver#onError(Throwable)} of
         * the {@link io.grpc.stub.StreamObserver} responsible for delivering messages
         * to the consumers.
         *
         * <p>Once this handler is called, no more messages will be delivered to consumers.
         *
         * @see #onConsumingError(ErrorHandler)
         */
        @CanIgnoreReturnValue
        Builder<M> onStreamingError(ErrorHandler handler) {
            streamingErrorHandler = checkNotNull(handler);
            return this;
        }

        /**
         * Assigns a handler for an error that may occur in the code of one of the consumers.
         *
         * <p>After this handler called, remaining consumers will get the message as usually.
         *
         * @see #onStreamingError(ErrorHandler)
         */
        @CanIgnoreReturnValue
        Builder<M> onConsumingError(ErrorHandler handler) {
            consumingErrorHandler = checkNotNull(handler);
            return this;
        }

        Consumers<M> build() {
            return new Consumers<>(this);
        }
    }
}
