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

import com.google.protobuf.Message;
import io.grpc.stub.StreamObserver;
import io.spine.client.EntityStateUpdate;
import io.spine.client.SubscriptionUpdate;

import static io.spine.protobuf.AnyPacker.unpack;

/**
 * A {@link StreamObserver} of {@link SubscriptionUpdate} messages translating the message
 * payload to the given delegate {@code StreamObserver}.
 *
 * <p>The errors and completion acknowledgements are translated directly to the delegate.
 *
 * <p>The {@linkplain SubscriptionUpdate#getEntityUpdates() messages} are unpacked
 * and sent to the delegate observer one by one.
 *
 * @param <M>
 *         the type of the delegate observer messages
 */
final class SubscriptionUpdateObserver<M extends Message>
        implements StreamObserver<SubscriptionUpdate> {

    private final StreamObserver<M> delegate;

    SubscriptionUpdateObserver(StreamObserver<M> targetObserver) {
        this.delegate = targetObserver;
    }

    @SuppressWarnings("unchecked") // Logically correct.
    @Override
    public void onNext(SubscriptionUpdate value) {
        value.getEntityUpdates()
             .getUpdateList()
             .stream()
             .map(EntityStateUpdate::getState)
             .map(any -> (M) unpack(any))
             .forEach(delegate::onNext);
    }

    @Override
    public void onError(Throwable t) {
        delegate.onError(t);
    }

    @Override
    public void onCompleted() {
        delegate.onCompleted();
    }
}
