/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user.given;

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
import io.spine.users.user.Identity;
import io.spine.users.user.IdentityVBuilder;
import io.spine.users.user.UserPart;

import static io.spine.base.Identifier.newUuid;
import static io.spine.users.role.RoleIds.roleId;

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
