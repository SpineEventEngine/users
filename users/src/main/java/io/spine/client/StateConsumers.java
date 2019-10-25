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
import io.spine.core.EmptyContext;

final class StateConsumers<S extends Message> extends Consumers<S, EmptyContext> {

    static <S extends Message> Builder<S> newBuilder() {
        return new Builder<S>();
    }

    private StateConsumers(Builder<S> builder) {
        super(builder);
    }

    @Override
    StreamObserver<S> toObserver() {
        return new StateObserver();
    }

    final class StateObserver extends DeliveringObserver<S> {

        @Override
        S toMessage(S outer) {
            return outer;
        }

        @Override
        EmptyContext toContext(S outer) {
            return EmptyContext.getDefaultInstance();
        }
    }

    static final class Builder<S extends Message>
            extends Consumers.Builder<S, EmptyContext, Builder<S>> {

        @Override
        Builder<S> self() {
            return this;
        }

        @Override
        StateConsumers<S> build() {
            return new StateConsumers<>(this);
        }
    }
}
