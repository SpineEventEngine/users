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

package io.spine.users.google.q.given;

import io.spine.users.GroupId;
import io.spine.users.google.GoogleGroupId;
import io.spine.users.google.q.GoogleGroupView;
import io.spine.users.google.q.GoogleGroupViewProjection;

import static io.spine.testing.server.entity.given.Given.projectionOfClass;
import static io.spine.users.google.given.GoogleGroupTestEnv.alias;

public class GoogleGroupViewProjectionTestEnv {

    /** Prevents instantiation of this utility class. */
    private GoogleGroupViewProjectionTestEnv() {
    }

    public static GoogleGroupViewProjection projectionWithValidState(GroupId id) {
        GoogleGroupId googleId = GoogleGroupId.newBuilder()
                                              .setValue(id.getValue())
                                              .build();
        GoogleGroupView stateWithId = GoogleGroupView.newBuilder()
                                                     .setId(id)
                                                     .setGoogleId(googleId)
                                                     .addAliases(alias())
                                                     .build();
        return projectionOfClass(GoogleGroupViewProjection.class)
                .withId(id)
                .withState(stateWithId)
                .build();
    }
}
