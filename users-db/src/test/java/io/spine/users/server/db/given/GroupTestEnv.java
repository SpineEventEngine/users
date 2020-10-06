/*
 * Copyright 2020, TeamDev. All rights reserved.
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

package io.spine.users.server.db.given;

import io.spine.net.EmailAddress;

import static io.spine.testing.TestValues.random;

/**
 * The environment for the {@code GroupPart} tests.
 */
public final class GroupTestEnv {

    /** Prevents instantiation. */
    private GroupTestEnv() {
    }

    public static String groupName() {
        return "Group-" + random(100);
    }

    public static EmailAddress groupEmail() {
        return EmailAddress
                .newBuilder()
                .setValue("developers-list-" + random(50) + "@gmail.com")
                .vBuild();
    }

    public static String groupDescription() {
        return "A relatively nice group";
    }

    static String anotherGroupDescription() {
        return "A very nice group";
    }

    static String anotherGroupName() {
        return "Developers-renamed";
    }

    static EmailAddress newGroupEmail() {
        return EmailAddress
                .newBuilder()
                .setValue("developers-renamed-list" + random(42) + "@gmail.com")
                .vBuild();
    }
}
