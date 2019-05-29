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

package io.spine.users.server.group.google;

import io.spine.testing.server.entity.given.Given;
import io.spine.users.GroupId;
import io.spine.users.group.google.GoogleGroupLifecycle;

/**
 * Test {@link GoogleGroupLifecyclePm Google Group process managers}.
 */
public class GoogleGroupTestPms {

    /**
     * Prevents instantiation.
     */
    private GoogleGroupTestPms() {
    }

    /**
     * Creates empty process manager in the default state.
     *
     * @param id
     *         and ID of the process manager
     */
    static GoogleGroupLifecyclePm emptyPm(GroupId id) {
        return Given.processManagerOfClass(GoogleGroupLifecyclePm.class)
                    .withState(lifecycle(id))
                    .build();
    }

    private static GoogleGroupLifecycle lifecycle(GroupId id) {
        return GoogleGroupLifecycle
                .newBuilder()
                .setId(id)
                .vBuild();
    }
}
