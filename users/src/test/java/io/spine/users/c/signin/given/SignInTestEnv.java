/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.signin.given;

import io.spine.core.UserId;
import io.spine.core.UserIdVBuilder;
import io.spine.net.EmailAddress;
import io.spine.net.EmailAddressVBuilder;
import io.spine.people.PersonName;
import io.spine.people.PersonNameVBuilder;
import io.spine.testing.server.entity.given.Given;
import io.spine.users.Identity;
import io.spine.users.IdentityProviderId;
import io.spine.users.IdentityProviderIdVBuilder;
import io.spine.users.IdentityVBuilder;
import io.spine.users.OrganizationId;
import io.spine.users.OrganizationIdVBuilder;
import io.spine.users.OrganizationOrUnit;
import io.spine.users.OrganizationOrUnitVBuilder;
import io.spine.users.PersonProfile;
import io.spine.users.PersonProfileVBuilder;
import io.spine.users.RoleId;
import io.spine.users.RoleIdVBuilder;
import io.spine.users.c.IdentityProviderBridge;
import io.spine.users.c.IdentityProviderBridgeFactory;
import io.spine.users.c.signin.SignInPm;
import io.spine.users.c.user.User;
import io.spine.users.c.user.UserAggregate;
import io.spine.users.c.user.UserAggregateRepository;
import io.spine.users.c.user.UserVBuilder;

import java.util.Optional;

import static io.spine.users.c.user.User.Status.NOT_READY;
import static io.spine.users.c.user.UserKind.PERSON;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The environment for the {@link SignInPm} tests.
 *
 * @author Vladyslav Lubenskyi
 */
public final class SignInTestEnv {

    /**
     * Prevents instantiation.
     */
    private SignInTestEnv() {
    }

    public static UserId userId() {
        return UserIdVBuilder.newBuilder()
                             .setValue("john.smith@example.com")
                             .build();
    }

    public static UserAggregateRepository emptyUserRepo() {
        UserAggregateRepository mock = mock(UserAggregateRepository.class);
        Optional<UserAggregate> user = empty();
        when(mock.find(any())).thenReturn(user);
        return mock;
    }

    public static UserAggregateRepository nonEmptyUserRepo() {
        UserAggregateRepository mock = mock(UserAggregateRepository.class);
        Optional<UserAggregate> user = of(userAggregateState());
        when(mock.find(any())).thenReturn(user);
        return mock;
    }

    public static IdentityProviderBridgeFactory mockActiveIdentityProvider() {
        IdentityProviderBridge mock = mock(IdentityProviderBridge.class);
        when(mock.hasIdentity(any())).thenReturn(true);
        when(mock.signInAllowed(any())).thenReturn(true);
        when(mock.fetchPersonProfile(any())).thenReturn(profile());
        return new TestIdentityProviderFactory(mock);
    }

    public static IdentityProviderBridgeFactory mockSuspendedIdentityProvider() {
        IdentityProviderBridge mock = mock(IdentityProviderBridge.class);
        when(mock.hasIdentity(any())).thenReturn(true);
        when(mock.signInAllowed(any())).thenReturn(false);
        when(mock.fetchPersonProfile(any())).thenReturn(profile());
        return new TestIdentityProviderFactory(mock);
    }

    private static UserAggregate userAggregateState() {
        User state = UserVBuilder.newBuilder()
                                 .setId(userId())
                                 .setOrgEntity(orgEntity())
                                 .setDisplayName(displayName())
                                 .setPrimaryIdentity(identity())
                                 .setProfile(profile())
                                 .setStatus(NOT_READY)
                                 .addSecondaryIdentity(identity())
                                 .addRole(adminRoleId())
                                 .setKind(PERSON)
                                 .build();
        UserAggregate aggregate = Given.aggregateOfClass(UserAggregate.class)
                                       .withState(state)
                                       .withId(userId())
                                       .build();
        return aggregate;
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

    private static OrganizationOrUnit orgEntity() {
        return OrganizationOrUnitVBuilder.newBuilder()
                                   .setOrganization(organizationId())
                                   .build();
    }

    public static Identity identity() {
        return IdentityVBuilder.newBuilder()
                                       .setDisplayName("j.s@google.com")
                                       .setProviderId(googleProviderId())
                                       .setUserId("123543")
                                       .build();
    }

    private static RoleId adminRoleId() {
        return RoleIdVBuilder.newBuilder()
                             .setValue("admin_role")
                             .build();
    }

    static IdentityProviderId googleProviderId() {
        return IdentityProviderIdVBuilder.newBuilder()
                                               .setValue("gmail.com")
                                               .build();
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
            return of(provider);
        }
    }
}
