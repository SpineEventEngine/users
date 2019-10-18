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

import com.google.protobuf.Message;
import io.grpc.stub.StreamObserver;

/**
 * Allows to subscribe to updates of messages using filtering conditions.
 */
public class SubscriptionRequestBuilder<M extends Message>
        extends FilteringRequestBuilder<M,
                                        Topic,
                                        TopicBuilder,
                                        SubscriptionRequestBuilder<M>> {

    SubscriptionRequestBuilder(RequestBuilder parent, Class<M> type) {
        super(parent, type);
    }

    private Subscription subscribe(Topic topic, StreamObserver<M> observer) {
        Subscription subscription = client().subscribeTo(topic, observer);
        return subscription;
    }

    public Subscription observe(StreamObserver<M> observer) {
        Topic topic = builder().build();
        return subscribe(topic, observer);
    }

    public Subscription all(StreamObserver<M> observer) {
        Topic topic = factory().allOf(messageType());
        return subscribe(topic, observer);
    }

    @Override
    TopicBuilder createBuilder() {
        return factory().select(messageType());
    }

    @Override
    SubscriptionRequestBuilder<M> self() {
        return this;
    }
}
