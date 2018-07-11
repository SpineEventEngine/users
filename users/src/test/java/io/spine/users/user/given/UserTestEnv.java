/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user.given;

import io.spine.core.UserId;
import io.spine.core.UserIdVBuilder;
import io.spine.net.EmailAddress;
import io.spine.net.EmailAddressVBuilder;
import io.spine.people.PersonName;
import io.spine.people.PersonNameVBuilder;
import io.spine.users.*;
import io.spine.users.signin.FirebaseTokens;
import io.spine.users.signin.FirebaseTokensVBuilder;
import io.spine.users.user.UserAggregate;
import io.spine.users.user.UserAttribute;
import io.spine.users.user.UserAttributeVBuilder;

import static io.spine.protobuf.AnyPacker.pack;

/**
 * The environment for the {@link UserAggregate} tests.
 *
 * @author Vladyslav Lubenskyi
 */
public class UserTestEnv {

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

    static GroupId firstGroupId() {
        return GroupIdVBuilder.newBuilder()
                .setValue("fisrt_group@example.com")
                .build();
    }

    static String displayName() {
        return "John Smith";
    }

    static UserProfile profile() {
        return UserProfileVBuilder.newBuilder()
                .setName(name())
                .setEmail(email())
                .build();
    }

    static ParentEntity parentEntity() {
        return ParentEntityVBuilder.newBuilder()
                .setOrganization(organizationId())
                .build();
    }

    static ParentEntity newParentEntity() {
        return ParentEntityVBuilder.newBuilder()
                .setOrgunit(orgUnitId())
                .build();
    }

    static UserAuthIdentity googleIdentity() {
        return UserAuthIdentityVBuilder.newBuilder()
                .setDisplayName("j.s@google.com")
                .setProviderId(googleProviderId())
                .setUid("123543")
                .build();
    }

    static UserAuthIdentity githubIdentity() {
        return UserAuthIdentityVBuilder.newBuilder()
                .setDisplayName("j.s@github.com")
                .setProviderId(githubPoviderId())
                .setUid("123543")
                .build();
    }

    static RoleId adminRoleId() {
        return RoleIdVBuilder.newBuilder()
                .setValue("admin_role")
                .build();
    }

    static RoleId editorRoleId() {
        return RoleIdVBuilder.newBuilder()
                .setValue("editor_role")
                .build();
    }

    static UserAttribute attribute() {
        FirebaseTokens tokenSet = FirebaseTokensVBuilder.newBuilder()
                .setAccessToken("access-token")
                .setRefreshToken("refresh-token")
                .build();
        return UserAttributeVBuilder.newBuilder()
                .setName("firebase_token")
                .setValue(pack(tokenSet))
                .build();
    }

    private static RemoteIdentityProviderId googleProviderId() {
        return RemoteIdentityProviderIdVBuilder.newBuilder()
                .setValue("gmail.com")
                .build();
    }

    private static RemoteIdentityProviderId githubPoviderId() {
        return RemoteIdentityProviderIdVBuilder.newBuilder()
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

    private static PersonName name() {
        return PersonNameVBuilder.newBuilder()
                .setGivenName("John")
                .setFamilyName("Smith")
                .build();
    }
}
