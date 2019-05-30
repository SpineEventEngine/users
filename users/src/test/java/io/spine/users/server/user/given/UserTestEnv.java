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

package io.spine.users.server.user.given;

import io.spine.core.UserId;
import io.spine.net.EmailAddress;
import io.spine.people.PersonName;
import io.spine.testing.core.given.GivenUserId;
import io.spine.users.DirectoryId;
import io.spine.users.GroupId;
import io.spine.users.OrgUnitId;
import io.spine.users.OrganizationId;
import io.spine.users.OrganizationOrUnit;
import io.spine.users.PersonProfile;
import io.spine.users.RoleId;
import io.spine.users.server.user.UserPart;
import io.spine.users.user.Identity;

import static io.spine.base.Identifier.newUuid;
import static io.spine.users.server.role.RoleIds.roleId;

/**
 * The environment for the {@link UserPart} tests.
 */
public class UserTestEnv {

    private static final String USER_UUID = newUuid();

    /**
     * Prevents direct instantiation.
     */
    private UserTestEnv() {
    }

    public static UserId userId() {
        return GivenUserId.of("john.smith@example.com");
    }

    public static GroupId firstGroupId() {
        return GroupId
                .newBuilder()
                .setValue("fisrt_group@example.com")
                .vBuild();
    }

    public static String userDisplayName() {
        return "John Smith";
    }

    public static String newUserDisplayName() {
        return "John Doe";
    }

    public static PersonProfile profile() {
        return PersonProfile
                .newBuilder()
                .setName(personName())
                .setEmail(email())
                .vBuild();
    }

    public static PersonProfile newProfile() {
        return PersonProfile
                .newBuilder()
                .setName(newPersonName())
                .setEmail(newEmail())
                .vBuild();
    }

    public static OrganizationOrUnit userOrgEntity() {
        return OrganizationOrUnit
                .newBuilder()
                .setOrganization(organizationId())
                .vBuild();
    }

    static OrganizationOrUnit newUserOrgEntity() {
        return OrganizationOrUnit
                .newBuilder()
                .setOrgUnit(orgUnitId())
                .vBuild();
    }

    public static Identity googleIdentity() {
        return Identity
                .newBuilder()
                .setDisplayName("j.s@google.com")
                .setDirectoryId(googleDirectoryId())
                .setUserId(USER_UUID)
                .vBuild();
    }

    public static RoleId adminRoleId() {
        return roleId(userOrgEntity(), "admin_role");
    }

    static Identity githubIdentity() {
        return Identity
                .newBuilder()
                .setDisplayName("j.s@github.com")
                .setDirectoryId(githubDirectoryId())
                .setUserId(USER_UUID)
                .vBuild();
    }

    static RoleId editorRoleId() {
        return roleId(userOrgEntity(), "editor_role");
    }

    private static DirectoryId googleDirectoryId() {
        return DirectoryId
                .newBuilder()
                .setValue("gmail.com")
                .vBuild();
    }

    private static DirectoryId githubDirectoryId() {
        return DirectoryId
                .newBuilder()
                .setValue("github.com")
                .vBuild();
    }

    private static OrganizationId organizationId() {
        return OrganizationId
                .newBuilder()
                .setValue("org_id")
                .vBuild();
    }

    private static OrgUnitId orgUnitId() {
        return OrgUnitId
                .newBuilder()
                .setValue("orgunit_id")
                .vBuild();
    }

    private static EmailAddress email() {
        return EmailAddress
                .newBuilder()
                .setValue("john@smith.com")
                .vBuild();
    }

    private static PersonName personName() {
        return PersonName
                .newBuilder()
                .setGivenName("Jane")
                .setFamilyName("Jones")
                .vBuild();
    }

    private static EmailAddress newEmail() {
        return EmailAddress
                .newBuilder()
                .setValue("john+alias@smith.com")
                .vBuild();
    }

    private static PersonName newPersonName() {
        return PersonName
                .newBuilder()
                .setGivenName("John")
                .setMiddleName("The Person")
                .setFamilyName("Smith")
                .vBuild();
    }
}
