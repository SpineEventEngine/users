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

import java.util.Arrays;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Suppliers.memoize;

abstract class
FilteringRequestBuilder<M extends Message,
                        F extends Message,
                        R extends AbstractTargetBuilder<F, R>,
                        B extends FilteringRequestBuilder<M, F, R, B>> extends RequestBuilder {

    private final Class<M> messageType;
    private final TopicFactory factory;
    private final Supplier<R> builder;

    FilteringRequestBuilder(RequestBuilder parent, Class<M> type) {
        super(parent.user(), parent.client());
        this.messageType = type;
        this.factory = client().requestOf(user()).topic();
        this.builder = memoize(this::createBuilder);
    }

    abstract R createBuilder();

    abstract B self();

    final Class<M> messageType() {
        return messageType;
    }

    final TopicFactory factory() {
        return factory;
    }

    final R builder() {
        return builder.get();
    }

    private B withIds(Iterable<?> ids) {
        checkNotNull(ids);
        builder().byId(ids);
        return self();
    }

    public B byId(Iterable<?> ids) {
        return withIds(ids);
    }

    public B byId(Message... ids) {
        return withIds(Arrays.asList(ids));
    }

    public B byId(Long... ids) {
        return withIds(Arrays.asList(ids));
    }

    public B byId(Integer... ids) {
        return withIds(Arrays.asList(ids));
    }

    public B byId(String... ids) {
        return withIds(Arrays.asList(ids));
    }

    public B where(Filter... filter) {
        builder().where(filter);
        return self();
    }

    public B where(CompositeFilter... filter) {
        builder().where(filter);
        return self();
    }

    public B withMask(Iterable<String> fieldNames) {
        builder().withMask(fieldNames);
        return self();
    }

    public B withMask(String... fieldNames) {
        builder().withMask(fieldNames);
        return self();
    }
}
