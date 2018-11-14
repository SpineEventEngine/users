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

import io.spine.testing.server.entity.given.Given;
import io.spine.users.GroupId;

import static io.spine.users.q.group.given.GroupViewTestEnv.*;

/**
 * Test {@link io.spine.users.q.group.GroupView} projections.
 */
class GroupViewTestProjections {

    /**
     * Prevents direct instantiation.
     */
    private GroupViewTestProjections() {
    }

    static GroupViewProjection emptyProjection(GroupId id) {
        return new GroupViewProjection(id);
    }

    static GroupViewProjection groupWithoutMemberProjection(GroupId id) {
        GroupView state = state(id).build();
        return newProjection(state);
    }

    static GroupViewProjection groupWithMemberProjection(GroupId id) {
        GroupView state = state(id)
                .addChildGroup(childGroup())
                .build();
        return newProjection(state);
    }

    private static GroupViewVBuilder state(GroupId id) {
        return GroupViewVBuilder.newBuilder()
                                .setId(id)
                                .setEmail(email())
                                .setOrgEntity(orgEntity())
                                .setDisplayName(groupDisplayName());
    }

    private static GroupViewProjection newProjection(GroupView state) {
        return Given.projectionOfClass(GroupViewProjection.class)
                .withId(state.getId())
                .withState(state)
                .build();
    }
}
