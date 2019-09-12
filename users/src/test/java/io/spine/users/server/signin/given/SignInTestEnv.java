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

package io.spine.users.server.signin.given;

import io.spine.core.UserId;
import io.spine.net.EmailAddress;
import io.spine.people.PersonName;
import io.spine.testing.core.given.GivenUserId;
import io.spine.users.DirectoryId;
import io.spine.users.OrganizationId;
import io.spine.users.OrganizationOrUnit;
import io.spine.users.PersonProfile;
import io.spine.users.RoleId;
import io.spine.users.server.signin.SignInProcess;
import io.spine.users.user.Identity;

import static io.spine.users.server.role.RoleIds.roleId;

/**
 * The environment for the {@link SignInProcess} tests.
 */
public final class SignInTestEnv {

    /**
     * Prevents instantiation.
     */
    private SignInTestEnv() {
    }

    public static UserId userId() {
        return GivenUserId.of("john.smith@example.com");
    }

    public static Identity identity() {
        return identity("123543", googleDirectoryId(), "j.s@google.com");
    }

    public static Identity identity(String id, DirectoryId directoryId, String name) {
        return Identity
                .newBuilder()
                .setUserId(id)
                .setDisplayName(name)
                .setDirectoryId(directoryId)
                .build();
    }

    static String displayName() {
        return "John Smith";
    }

    static PersonProfile profile() {
        return PersonProfile
                .newBuilder()
                .setName(name())
                .setEmail(email())
                .build();
    }

    private static DirectoryId googleDirectoryId() {
        return DirectoryId
                .newBuilder()
                .setValue("gmail.com")
                .build();
    }

    private static OrganizationOrUnit orgEntity() {
        return OrganizationOrUnit
                .newBuilder()
                .setOrganization(organizationId())
                .build();
    }

    private static RoleId adminRoleId() {
        return roleId(orgEntity(), "admin_role");
    }

    private static OrganizationId organizationId() {
        return OrganizationId
                .newBuilder()
                .setValue("org_id")
                .build();
    }

    private static EmailAddress email() {
        return EmailAddress
                .newBuilder()
                .setValue("john@smith.com")
                .vBuild();
    }

    private static PersonName name() {
        return PersonName
                .newBuilder()
                .setGivenName("John")
                .setFamilyName("Smith")
                .vBuild();
    }
}
