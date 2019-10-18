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

import java.util.List;

public final class QueryRequestBuilder<M extends Message>
    extends FilteringRequestBuilder<M,
                                    Query,
                                    QueryBuilder,
                                    QueryRequestBuilder<M>> {

    QueryRequestBuilder(RequestBuilder parent, Class<M> type) {
        super(parent, type);
    }

    public QueryRequestBuilder<M> orderBy(String column, OrderBy.Direction direction) {
        builder().orderBy(column, direction);
        return this;
    }

    public QueryRequestBuilder<M> limit(int count) {
        builder().limit(count);
        return this;
    }

    public List<M> query() {
        Query query = builder().build();
        List<M> result = client().query(query);
        return result;
    }

    @Override
    QueryBuilder createBuilder() {
        return factory().query().select(messageType());
    }

    @Override
    QueryRequestBuilder<M> self() {
        return this;
    }
}
