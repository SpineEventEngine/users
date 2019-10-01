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

package io.spine.users.client;

import com.google.protobuf.Any;
import com.google.protobuf.Message;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.spine.client.EntityStateWithVersion;
import io.spine.client.Query;
import io.spine.client.Subscription;
import io.spine.client.Topic;
import io.spine.client.grpc.CommandServiceGrpc;
import io.spine.client.grpc.CommandServiceGrpc.CommandServiceBlockingStub;
import io.spine.client.grpc.QueryServiceGrpc;
import io.spine.client.grpc.QueryServiceGrpc.QueryServiceBlockingStub;
import io.spine.client.grpc.SubscriptionServiceGrpc;
import io.spine.client.grpc.SubscriptionServiceGrpc.SubscriptionServiceBlockingStub;
import io.spine.client.grpc.SubscriptionServiceGrpc.SubscriptionServiceStub;
import io.spine.core.Command;

import java.util.List;

import static io.spine.util.Exceptions.illegalStateWithCauseOf;
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

    public Client(String host, int port) {
        this.channel = initChannel(host, port);
        this.commandService = CommandServiceGrpc.newBlockingStub(channel);
        this.queryService = QueryServiceGrpc.newBlockingStub(channel);
        this.subscriptionService = SubscriptionServiceGrpc.newStub(channel);
        this.blockingSubscriptionService = SubscriptionServiceGrpc.newBlockingStub(channel);
    }

    private static ManagedChannel initChannel(String host, int port) {
        ManagedChannel result = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        return result;
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
     * @see #unSubscribe(Subscription)
     */
    public <M extends Message> Subscription subscribeTo(Topic topic, StreamObserver<M> observer) {
        Subscription subscription = blockingSubscriptionService.subscribe(topic);
        subscriptionService.activate(subscription, new SubscriptionUpdateObserver<>(observer));
        return subscription;
    }

    /**
     * Requests cancellation of the passed subscription.
     *
     * @see #subscribeTo(Topic, StreamObserver)
     */
    public void unSubscribe(Subscription subscription) {
        blockingSubscriptionService.cancel(subscription);
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
}
