/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user.given;

import com.google.protobuf.Any;
import com.google.protobuf.StringValue;
import io.spine.core.UserId;
import io.spine.core.UserIdVBuilder;
import io.spine.net.EmailAddress;
import io.spine.net.EmailAddressVBuilder;
import io.spine.people.PersonName;
import io.spine.people.PersonNameVBuilder;
import io.spine.users.GroupId;
import io.spine.users.GroupIdVBuilder;
import io.spine.users.IdentityProviderId;
import io.spine.users.IdentityProviderIdVBuilder;
import io.spine.users.OrgUnitId;
import io.spine.users.OrgUnitIdVBuilder;
import io.spine.users.OrganizationId;
import io.spine.users.OrganizationIdVBuilder;
import io.spine.users.OrganizationalEntity;
import io.spine.users.OrganizationalEntityVBuilder;
import io.spine.users.RoleId;
import io.spine.users.RoleIdVBuilder;
import io.spine.users.UserAuthIdentity;
import io.spine.users.UserAuthIdentityVBuilder;
import io.spine.users.UserProfile;
import io.spine.users.UserProfileVBuilder;
import io.spine.users.c.user.UserAggregate;

import static io.spine.base.Identifier.newUuid;
import static io.spine.protobuf.AnyPacker.pack;

/**
 * The environment for the {@link UserAggregate} tests.
 *
 * @author Vladyslav Lubenskyi
 */
public class UserTestEnv {
    private static final String USER_UUID = newUuid();

    /**
     * Prevents direct instantiation.
     */
    private UserTestEnv() {
    }

    public static UserId userId() {
        return UserIdVBuilder.newBuilder()
                             .setValue("john.smith@example.com")
                             .build();
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

    public static UserProfile profile() {
        return UserProfileVBuilder.newBuilder()
                                  .setName(personName())
                                  .setEmail(email())
                                  .build();
    }

    public static UserProfile newProfile() {
        return UserProfileVBuilder.newBuilder()
                                  .setName(newPersonName())
                                  .setEmail(newEmail())
                                  .build();
    }

    public static OrganizationalEntity userOrgEntity() {
        return OrganizationalEntityVBuilder.newBuilder()
                                   .setOrganization(organizationId())
                                   .build();
    }

    static OrganizationalEntity newUserOrgEntity() {
        return OrganizationalEntityVBuilder.newBuilder()
                                   .setOrgUnit(orgUnitId())
                                   .build();
    }

    public static UserAuthIdentity googleIdentity() {
        return UserAuthIdentityVBuilder.newBuilder()
                                       .setDisplayName("j.s@google.com")
                                       .setProviderId(googleProviderId())
                                       .setUserId(USER_UUID)
                                       .build();
    }

    public static RoleId adminRoleId() {
        return RoleIdVBuilder.newBuilder()
                             .setValue("admin_role")
                             .build();
    }

    public static String attributeName() {
        return "auth_token";
    }

    public static Any attributeValue() {
        return pack(StringValue.of("encrypted-auth-token-value"));
    }

    static UserAuthIdentity githubIdentity() {
        return UserAuthIdentityVBuilder.newBuilder()
                                       .setDisplayName("j.s@github.com")
                                       .setProviderId(githubPoviderId())
                                       .setUserId(USER_UUID)
                                       .build();
    }

    static RoleId editorRoleId() {
        return RoleIdVBuilder.newBuilder()
                             .setValue("editor_role")
                             .build();
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
