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

import io.spine.net.EmailAddress;
import io.spine.net.EmailAddressVBuilder;
import io.spine.net.InternetDomain;
import io.spine.net.InternetDomainVBuilder;
import io.spine.users.GoogleGroupId;
import io.spine.users.GroupId;
import io.spine.users.GroupIdVBuilder;

import static io.spine.base.Identifier.newUuid;

/**
 * Test environment for testing
 *
 * @author Vladyslav Lubenskyi
 */
public class GoogleIdMappingTestEnv {

    /**
     * Prevents instantiation.
     */
    private GoogleIdMappingTestEnv() {
    }

    static GroupId groupId() {
        return GroupIdVBuilder.newBuilder()
                              .setValue(newUuid())
                              .build();
    }

    static String googleGroupDisplayName() {
        return "Developers Group";
    }

    static InternetDomain domain() {
        return InternetDomainVBuilder.newBuilder()
                                     .setValue("spine.io")
                                     .build();
    }

    static GoogleGroupId googleId() {
        return GoogleGroupId
                .newBuilder()
                .setValue(newUuid())
                .build();
    }

    static EmailAddress email() {
        return EmailAddressVBuilder.newBuilder()
                                   .setValue("developers@company.com")
                                   .build();
    }
}
