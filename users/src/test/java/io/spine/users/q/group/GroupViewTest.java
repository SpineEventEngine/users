/*
 * Copyright 2018, TeamDev. All rights reserved.
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

package io.spine.users.q.group;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import io.spine.base.EventMessage;
import io.spine.core.Enrichment;
import io.spine.protobuf.AnyPacker;
import io.spine.server.entity.Repository;
import io.spine.testing.server.projection.ProjectionTest;
import io.spine.users.GroupId;

import static io.spine.users.q.group.given.GroupViewTestEnv.groupId;

/**
 * The implementation base for testing event subscriptions in {@link GroupView}.
 *
 * @param <M>
 *         the type of event being tested
 */
abstract class GroupViewTest<M extends EventMessage>
        extends ProjectionTest<GroupId, M, GroupView, GroupViewProjection> {

    GroupViewTest(M eventMessage) {
        super(groupId(), eventMessage);
    }

    @Override
    protected Repository<GroupId, GroupViewProjection> createRepository() {
        return new GroupViewProjectionRepository();
    }

    static Enrichment enrichWith(Enrichment enrichment, Message enrichmentMessage) {
        Descriptors.Descriptor descriptor = enrichmentMessage.getDescriptorForType();
        Enrichment.Container updatedContainer = enrichment
                .getContainer()
                .toBuilder()
                .putItems(descriptor.getFullName(),
                          AnyPacker.pack(enrichmentMessage))
                .build();
        return enrichment.toBuilder()
                         .setContainer(updatedContainer)
                         .build();
    }
}
