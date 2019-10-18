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
import io.spine.annotation.Experimental;
import io.spine.base.CommandMessage;
import io.spine.core.UserId;

import static com.google.common.base.Preconditions.checkNotNull;

@SuppressWarnings("ClassReferencesSubclass")
// we want to have DSL for calls encapsulated in this class.
@Experimental
public class RequestBuilder {

    private final UserId user;
    private final Client client;

    RequestBuilder(UserId user, Client client) {
        Util.checkNotDefault(user);
        this.user = user;
        this.client = checkNotNull(client);
    }

    /**
     * Creates a builder for customizing command request.
     */
    public CommandRequestBuilder command(CommandMessage c) {
        return new CommandRequestBuilder(this, c);
    }

    /**
     * Creates a builder for customizing subscription for the passed type.
     */
    public <M extends Message>
    SubscriptionRequestBuilder subscribeTo(Class<M> type) {
        return new SubscriptionRequestBuilder<>(this, type);
    }

    public <M extends Message>
    QueryRequestBuilder<M> select(Class<M> type) {
        return new QueryRequestBuilder<>(this, type);
    }

    /**
     * Obtains the ID of the user of the request.
     */
    protected final UserId user() {
        return user;
    }

    /**
     * Obtains the client instance that will perform the request.
     */
    protected final Client client() {
        return client;
    }
}
