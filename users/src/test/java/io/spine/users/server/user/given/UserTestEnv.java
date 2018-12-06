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

package io.spine.users.server.user.given;

import io.spine.core.UserId;
import io.spine.net.EmailAddress;
import io.spine.net.EmailAddressVBuilder;
import io.spine.people.PersonName;
import io.spine.people.PersonNameVBuilder;
import io.spine.testing.core.given.GivenUserId;
import io.spine.users.GroupId;
import io.spine.users.GroupIdVBuilder;
import io.spine.users.IdentityProviderId;
import io.spine.users.IdentityProviderIdVBuilder;
import io.spine.users.OrgUnitId;
import io.spine.users.OrgUnitIdVBuilder;
import io.spine.users.OrganizationId;
import io.spine.users.OrganizationIdVBuilder;
import io.spine.users.OrganizationOrUnit;
import io.spine.users.OrganizationOrUnitVBuilder;
import io.spine.users.PersonProfile;
import io.spine.users.PersonProfileVBuilder;
import io.spine.users.RoleId;
import io.spine.users.server.user.UserPart;
import io.spine.users.user.Identity;
import io.spine.users.user.IdentityVBuilder;

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
        return GroupIdVBuilder.newBuilder()
                              .setValue("fisrt_group@example.com")
                              .build();
    }

    public static String userDisplayName() {
        return "John Smith";
    }

    public static String newUserDisplayName() {
        return "John Doe";
    }

    public static PersonProfile profile() {
        return PersonProfileVBuilder.newBuilder()
                                    .setName(personName())
                                    .setEmail(email())
                                    .build();
    }

    public static PersonProfile newProfile() {
        return PersonProfileVBuilder.newBuilder()
                                    .setName(newPersonName())
                                    .setEmail(newEmail())
                                    .build();
    }

    public static OrganizationOrUnit userOrgEntity() {
        return OrganizationOrUnitVBuilder.newBuilder()
                                         .setOrganization(organizationId())
                                         .build();
    }

    static OrganizationOrUnit newUserOrgEntity() {
        return OrganizationOrUnitVBuilder.newBuilder()
                                         .setOrgUnit(orgUnitId())
                                         .build();
    }

    public static Identity googleIdentity() {
        return IdentityVBuilder.newBuilder()
                               .setDisplayName("j.s@google.com")
                               .setProviderId(googleProviderId())
                               .setUserId(USER_UUID)
                               .build();
    }

    public static RoleId adminRoleId() {
        return roleId(userOrgEntity(), "admin_role");
    }

    static Identity githubIdentity() {
        return IdentityVBuilder.newBuilder()
                               .setDisplayName("j.s@github.com")
                               .setProviderId(githubPoviderId())
                               .setUserId(USER_UUID)
                               .build();
    }

    static RoleId editorRoleId() {
        return roleId(userOrgEntity(), "editor_role");
    }

    private static IdentityProviderId googleProviderId() {
        return IdentityProviderIdVBuilder.newBuilder()
                                         .setValue("gmail.com")
                                         .build();
    }

    private static IdentityProviderId githubPoviderId() {
        return IdentityProviderIdVBuilder.newBuilder()
                                         .setValue("github.com")
                                         .build();
    }

    private static OrganizationId organizationId() {
        return OrganizationIdVBuilder.newBuilder()
                                     .setValue("org_id")
                                     .build();
    }

    private static OrgUnitId orgUnitId() {
        return OrgUnitIdVBuilder.newBuilder()
                                .setValue("orgunit_id")
                                .build();
    }

    private static EmailAddress email() {
        return EmailAddressVBuilder.newBuilder()
                                   .setValue("john@smith.com")
                                   .build();
    }

    private static PersonName personName() {
        return PersonNameVBuilder.newBuilder()
                                 .setGivenName("Jane")
                                 .setFamilyName("Jones")
                                 .build();
    }

    private static EmailAddress newEmail() {
        return EmailAddressVBuilder.newBuilder()
                                   .setValue("john+alias@smith.com")
                                   .build();
    }

    private static PersonName newPersonName() {
        return PersonNameVBuilder.newBuilder()
                                 .setGivenName("John")
                                 .setMiddleName("The Person")
                                 .setFamilyName("Smith")
                                 .build();
    }
}
