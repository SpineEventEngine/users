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

import com.google.protobuf.Any;
import com.google.protobuf.Message;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.spine.client.grpc.CommandServiceGrpc;
import io.spine.client.grpc.CommandServiceGrpc.CommandServiceBlockingStub;
import io.spine.client.grpc.QueryServiceGrpc;
import io.spine.client.grpc.QueryServiceGrpc.QueryServiceBlockingStub;
import io.spine.client.grpc.SubscriptionServiceGrpc;
import io.spine.client.grpc.SubscriptionServiceGrpc.SubscriptionServiceBlockingStub;
import io.spine.client.grpc.SubscriptionServiceGrpc.SubscriptionServiceStub;
import io.spine.core.Command;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.spine.util.Exceptions.illegalStateWithCauseOf;
import static io.spine.util.Preconditions2.checkNotEmptyOrBlank;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;

/**
 * The gRPC-based gateway for backend services such as {@code CommandService} or
 * {@code QueryService}.
 */
public class Client implements AutoCloseable {

    /** The number of seconds to wait when {@linkplain #close() closing} the client. */
    private static final int TIMEOUT = 10;

    private final ManagedChannel channel;
    private final QueryServiceBlockingStub queryService;
    private final CommandServiceBlockingStub commandService;
    private final SubscriptionServiceStub subscriptionService;
    private final SubscriptionServiceBlockingStub blockingSubscriptionService;

    /**
     * Creates a builder for a client connected to the specified address.
     *
     * <p>The returned builder will create {@code ManagedChannel} with the default configuration.
     * For a channel with custom configuration please use {@link #usingChannel(ManagedChannel)}.
     *
     * @see #usingChannel(ManagedChannel)
     */
    public static Builder connectTo(String host, int port) {
        checkNotEmptyOrBlank(host);
        return new Builder(host, port);
    }

    /**
     * Creates a builder for a client which will use the passed channel for the communication
     * with the backend services.
     *
     * <p>Use this method when a channel with custom configuration is needed for your client
     * application.
     *
     * @see #connectTo(String, int)
     * @see ManagedChannel
     */
    public static Builder usingChannel(ManagedChannel channel) {
        checkNotNull(channel);
        return new Builder(channel);
    }

    /**
     * Creates a new client which uses the passed channel for communications
     * with the backend services.
     */
    private Client(ManagedChannel channel) {
        this.channel = checkNotNull(channel);
        this.commandService = CommandServiceGrpc.newBlockingStub(channel);
        this.queryService = QueryServiceGrpc.newBlockingStub(channel);
        this.subscriptionService = SubscriptionServiceGrpc.newStub(channel);
        this.blockingSubscriptionService = SubscriptionServiceGrpc.newBlockingStub(channel);
    }

    /**
     * Closes the client by shutting down the gRPC connection.
     */
    @Override
    public void close() {
        try {
            channel.shutdown()
                   .awaitTermination(TIMEOUT, SECONDS);
        } catch (InterruptedException e) {
            throw illegalStateWithCauseOf(e);
        }
    }

    /**
     * Subscribes the given {@link StreamObserver} to the given topic and activates
     * the subscription.
     *
     * @param topic
     *         the topic to subscribe to
     * @param observer
     *         the observer to subscribe
     * @param <M>
     *         the type of the result messages
     * @return the activated subscription
     * @see #cancel(Subscription)
     */
    public <M extends Message> Subscription subscribeTo(Topic topic, StreamObserver<M> observer) {
        Subscription subscription = blockingSubscriptionService.subscribe(topic);
        subscriptionService.activate(subscription, new SubscriptionObserver<>(observer));
        return subscription;
    }

    /**
     * Requests cancellation of the passed subscription.
     *
     * @see #subscribeTo(Topic, StreamObserver)
     */
    public void cancel(Subscription s) {
        blockingSubscriptionService.cancel(s);
    }

    /**
     * Posts the command to the {@code CommandService}.
     */
    public void postCommand(Command c) {
        commandService.post(c);
    }

    /**
     * Queries the read-side with the specified query.
     */
    public List<Any> query(Query query) {
        List<Any> result = queryService
                .read(query)
                .getMessageList()
                .stream()
                .map(EntityStateWithVersion::getState)
                .collect(toList());
        return result;
    }

    /**
     * The builder for the client.
     */
    public static final class Builder {

        /**
         * The channel to be used in the client.
         */
        private ManagedChannel channel;

        /**
         * The address of the host which will be used for creating an instance
         * of {@code ManagedChannel}.
         *
         * <p>This field is {@code null} if the builder is created using already made
         * {@code ManagedChannel}.
         */
        private @MonotonicNonNull String host;
        private int port;

        private Builder(ManagedChannel channel) {
            this.channel = checkNotNull(channel);
        }

        private Builder(String host, int port) {
            this.host = host;
            this.port = port;
        }

        private static ManagedChannel createChannel(String host, int port) {
            ManagedChannel result = ManagedChannelBuilder
                    .forAddress(host, port)
                    .build();
            return result;
        }

        /**
         * Creates a new instance of the client.
         */
        public Client build() {
            if (channel == null) {
                checkNotNull(host, "Either channel or host must be specified.");
                channel = createChannel(host, port);
            }
            return new Client(channel);
        }
    }
}
