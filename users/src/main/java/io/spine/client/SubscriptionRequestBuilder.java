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

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Allows to subscribe to updates of messages using filtering conditions.
 */
public class SubscriptionRequestBuilder<M extends Message> extends RequestBuilder {

    private final Class<M> messageType;
    private final TopicFactory factory;
    /** The builder for the case of selecting not all instances. */
    private final TopicBuilder topic;

    SubscriptionRequestBuilder(RequestBuilder parent, Class<M> type) {
        super(parent.user(), parent.client());
        this.messageType = type;
        this.factory = client().requestOf(user()).topic();
        this.topic = factory.select(type);
    }

    public Subscription all(StreamObserver<M> observer) {
        Topic topic = factory.allOf(messageType);
        return subscribe(topic, observer);
    }

    private Subscription subscribe(Topic topic, StreamObserver<M> observer) {
        Subscription subscription = client().subscribeTo(topic, observer);
        return subscription;
    }

    private SubscriptionRequestBuilder<M> withIds(Iterable<?> ids) {
        checkNotNull(ids);
        topic.byId(ids);
        return this;
    }

    public SubscriptionRequestBuilder<M> byId(Iterable<?> ids) {
        return withIds(ids);
    }

    public SubscriptionRequestBuilder<M> byId(Message... ids) {
        return withIds(Arrays.asList(ids));
    }

    public SubscriptionRequestBuilder<M> byId(Long... ids) {
        return withIds(Arrays.asList(ids));
    }

    public SubscriptionRequestBuilder<M> byId(Integer... ids) {
        return withIds(Arrays.asList(ids));
    }

    public SubscriptionRequestBuilder<M> byId(String... ids) {
        return withIds(Arrays.asList(ids));
    }

    public SubscriptionRequestBuilder<M> where(Filter... filter) {
        topic.where(filter);
        return this;
    }

    public SubscriptionRequestBuilder<M> where(CompositeFilter... filter) {
        topic.where(filter);
        return this;
    }

    public SubscriptionRequestBuilder<M> withMask(Iterable<String> fieldNames) {
        topic.withMask(fieldNames);
        return this;
    }

    public SubscriptionRequestBuilder<M> withMask(String... fieldNames) {
        topic.withMask(fieldNames);
        return this;
    }

    public Subscription observe(StreamObserver<M> observer) {
        Topic topic = this.topic.build();
        return subscribe(topic, observer);
    }
}
