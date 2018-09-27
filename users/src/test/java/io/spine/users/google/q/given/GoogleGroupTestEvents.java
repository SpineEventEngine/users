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

import io.spine.users.google.c.group.GoogleGroupCreated;
import io.spine.users.google.c.group.GoogleGroupCreatedVBuilder;

import static io.spine.users.google.q.given.GoogleGroupTestEnv.email;
import static io.spine.users.google.q.given.GoogleGroupTestEnv.googleGroupDisplayName;
import static io.spine.users.google.q.given.GoogleGroupTestEnv.googleId;
import static io.spine.users.google.q.given.GoogleGroupTestEnv.groupId;
import static io.spine.users.google.q.given.GoogleGroupTestEnv.organization;

/**
 * Test events for testing Google Group views.
 */
public class GoogleGroupTestEvents {

    /**
     * Prevents instantiation.
     */
    private GoogleGroupTestEvents() {
    }

    public static GoogleGroupCreated googleGroupCreated() {
        return GoogleGroupCreatedVBuilder.newBuilder()
                                         .setId(groupId())
                                         .setDisplayName(googleGroupDisplayName())
                                         .setOrganization(organization())
                                         .setGoogleId(googleId())
                                         .setEmail(email())
                                         .build();
    }
}
