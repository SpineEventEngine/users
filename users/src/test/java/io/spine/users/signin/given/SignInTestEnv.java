/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin.given;

import io.spine.core.UserId;
import io.spine.net.EmailAddress;
import io.spine.net.EmailAddressVBuilder;
import io.spine.people.PersonName;
import io.spine.people.PersonNameVBuilder;
import io.spine.testing.core.given.GivenUserId;
import io.spine.users.IdentityProviderBridge;
import io.spine.users.IdentityProviderBridgeFactory;
import io.spine.users.IdentityProviderId;
import io.spine.users.IdentityProviderIdVBuilder;
import io.spine.users.OrganizationId;
import io.spine.users.OrganizationIdVBuilder;
import io.spine.users.OrganizationOrUnit;
import io.spine.users.OrganizationOrUnitVBuilder;
import io.spine.users.PersonProfile;
import io.spine.users.PersonProfileVBuilder;
import io.spine.users.RoleId;
import io.spine.users.signin.SignInFailureReason;
import io.spine.users.signin.SignInPm;
import io.spine.users.user.Identity;
import io.spine.users.user.IdentityVBuilder;
import io.spine.users.user.User;
import io.spine.users.user.UserPart;
import io.spine.users.user.UserPartRepository;
import io.spine.users.user.UserVBuilder;

import java.util.Optional;

import static io.spine.testing.server.TestBoundedContext.create;
import static io.spine.testing.server.entity.given.Given.aggregatePartOfClass;
import static io.spine.users.role.RoleIds.roleId;
import static io.spine.users.signin.SignInFailureReason.SIGN_IN_NOT_AUTHORIZED;
import static io.spine.users.user.User.Status.NOT_READY;
import static io.spine.users.user.UserNature.PERSON;
import static io.spine.users.user.UserRoot.getForTest;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The environment for the {@link SignInPm} tests.
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

    public static UserPartRepository emptyUserRepo() {
        UserPartRepository mock = mock(UserPartRepository.class);
        Optional<UserPart> user = empty();
        when(mock.find(any())).thenReturn(user);
        return mock;
    }

    public static UserPartRepository nonEmptyUserRepo() {
        UserPartRepository mock = mock(UserPartRepository.class);
        Optional<UserPart> user = of(normalUserPart());
        when(mock.find(any())).thenReturn(user);
        return mock;
    }

    public static UserPartRepository noIdentityUserRepo() {
        UserPartRepository mock = mock(UserPartRepository.class);
        Optional<UserPart> user = of(noIdentityUserPart());
        when(mock.find(any())).thenReturn(user);
        return mock;
    }

    public static IdentityProviderBridgeFactory mockActiveIdentityProvider() {
        IdentityProviderBridge mock = mock(IdentityProviderBridge.class);
        when(mock.hasIdentity(any())).thenReturn(true);
        when(mock.isSignInAllowed(any())).thenReturn(true);
        when(mock.fetchProfile(any())).thenReturn(profile());
        return new TestIdentityProviderFactory(mock);
    }

    public static IdentityProviderBridgeFactory mockSuspendedIdentityProvider() {
        IdentityProviderBridge mock = mock(IdentityProviderBridge.class);
        when(mock.hasIdentity(any())).thenReturn(true);
        when(mock.isSignInAllowed(any())).thenReturn(false);
        when(mock.fetchProfile(any())).thenReturn(profile());
        return new TestIdentityProviderFactory(mock);
    }

    public static IdentityProviderBridgeFactory mockEmptyIdentityProvider() {
        IdentityProviderBridge mock = mock(IdentityProviderBridge.class);
        when(mock.hasIdentity(any())).thenReturn(false);
        when(mock.isSignInAllowed(any())).thenReturn(false);
        when(mock.fetchProfile(any())).thenReturn(PersonProfile.getDefaultInstance());
        return new TestIdentityProviderFactory(mock);
    }

    public static IdentityProviderBridgeFactory mockEmptyProviderFactory() {
        return new TestIdentityProviderFactory(null);
    }

    public static Identity identity() {
        return IdentityVBuilder.newBuilder()
                               .setDisplayName("j.s@google.com")
                               .setProviderId(googleProviderId())
                               .setUserId("123543")
                               .build();
    }

    public static Identity secondaryIdentity() {
        return IdentityVBuilder.newBuilder()
                               .setDisplayName("s.j@google.com")
                               .setProviderId(googleProviderId())
                               .setUserId("6987")
                               .build();
    }

    public static SignInFailureReason failureReason() {
        return SIGN_IN_NOT_AUTHORIZED;
    }

    static String displayName() {
        return "John Smith";
    }

    static PersonProfile profile() {
        return PersonProfileVBuilder.newBuilder()
                                    .setName(name())
                                    .setEmail(email())
                                    .build();
    }

    static IdentityProviderId googleProviderId() {
        return IdentityProviderIdVBuilder.newBuilder()
                                         .setValue("gmail.com")
                                         .build();
    }

    private static UserPart normalUserPart() {
        User state = userPartState().setPrimaryIdentity(identity())
                                    .build();
        UserPart aggregate = aggregatePartOfClass(UserPart.class)
                .withRoot(getForTest(create(), userId()))
                .withState(state)
                .withId(userId())
                .build();
        return aggregate;
    }

    private static UserPart noIdentityUserPart() {
        UserPart aggregate = aggregatePartOfClass(UserPart.class)
                .withRoot(getForTest(create(), userId()))
                .withState(userPartState().build())
                .withId(userId())
                .build();
        return aggregate;
    }

    private static UserVBuilder userPartState() {
        return UserVBuilder.newBuilder()
                           .setId(userId())
                           .setOrgEntity(orgEntity())
                           .setDisplayName(displayName())
                           .setProfile(profile())
                           .setStatus(NOT_READY)
                           .addSecondaryIdentity(secondaryIdentity())
                           .addRole(adminRoleId())
                           .setNature(PERSON);
    }

    private static OrganizationOrUnit orgEntity() {
        return OrganizationOrUnitVBuilder.newBuilder()
                                         .setOrganization(organizationId())
                                         .build();
    }

    private static RoleId adminRoleId() {
        return roleId(orgEntity(), "admin_role");
    }

    private static OrganizationId organizationId() {
        return OrganizationIdVBuilder.newBuilder()
                                     .setValue("org_id")
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

    /**
     * A factory that always returns a single identity provider.
     */
    static class TestIdentityProviderFactory extends IdentityProviderBridgeFactory {

        private final IdentityProviderBridge provider;

        private TestIdentityProviderFactory(IdentityProviderBridge provider) {
            super();
            this.provider = provider;
        }

        @Override
        public Optional<IdentityProviderBridge> get(IdentityProviderId id) {
            return ofNullable(provider);
        }
    }
}
