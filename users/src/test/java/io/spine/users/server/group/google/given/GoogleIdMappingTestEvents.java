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

package io.spine.users.server.group.google.given;

import io.spine.users.google.group.event.GoogleGroupCreated;
import io.spine.users.google.group.event.GoogleGroupCreatedVBuilder;

import static io.spine.users.server.group.google.given.GoogleIdMappingTestEnv.domain;
import static io.spine.users.server.group.google.given.GoogleIdMappingTestEnv.email;
import static io.spine.users.server.group.google.given.GoogleIdMappingTestEnv.googleGroupDisplayName;
import static io.spine.users.server.group.google.given.GoogleIdMappingTestEnv.googleId;
import static io.spine.users.server.group.google.given.GoogleIdMappingTestEnv.groupId;

/**
 * Test events for testing Google Group views.
 */
public class GoogleIdMappingTestEvents {

    /**
     * Prevents instantiation.
     */
    private GoogleIdMappingTestEvents() {
    }

    public static GoogleGroupCreated googleGroupCreated() {
        return GoogleGroupCreatedVBuilder.newBuilder()
                                         .setId(groupId())
                                         .setDisplayName(googleGroupDisplayName())
                                         .setDomain(domain())
                                         .setGoogleId(googleId())
                                         .setEmail(email())
                                         .build();
    }
}
