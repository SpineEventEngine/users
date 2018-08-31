/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.signin.given;

import com.google.protobuf.Any;
import com.google.protobuf.StringValue;
import io.spine.core.UserId;
import io.spine.core.UserIdVBuilder;
import io.spine.net.EmailAddress;
import io.spine.net.EmailAddressVBuilder;
import io.spine.people.PersonName;
import io.spine.people.PersonNameVBuilder;
import io.spine.testing.server.entity.given.Given;
import io.spine.users.IdentityProviderId;
import io.spine.users.IdentityProviderIdVBuilder;
import io.spine.users.OrganizationId;
import io.spine.users.OrganizationIdVBuilder;
import io.spine.users.ParentEntity;
import io.spine.users.ParentEntityVBuilder;
import io.spine.users.RoleId;
import io.spine.users.RoleIdVBuilder;
import io.spine.users.c.user.User;
import io.spine.users.UserAuthIdentity;
import io.spine.users.UserAuthIdentityVBuilder;
import io.spine.users.UserProfile;
import io.spine.users.UserProfileVBuilder;
import io.spine.users.c.IdentityProviderBridge;
import io.spine.users.c.IdentityProviderBridgeFactory;
import io.spine.users.c.signin.SignInPm;
import io.spine.users.c.user.UserAggregate;
import io.spine.users.c.user.UserAggregateRepository;
import io.spine.users.c.user.UserVBuilder;

import java.util.Optional;

import static io.spine.protobuf.AnyPacker.pack;
import static io.spine.users.c.user.User.Status.NOT_READY;
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
public class SignInTestEnv {

    /**
     * Prevents direct instantiation.
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
        when(mock.fetchUserProfile(any())).thenReturn(profile());
        return new TestIdentityProviderFactory(mock);
    }

    public static IdentityProviderBridgeFactory mockSuspendedIdentityProvider() {
        IdentityProviderBridge mock = mock(IdentityProviderBridge.class);
        when(mock.hasIdentity(any())).thenReturn(true);
        when(mock.signInAllowed(any())).thenReturn(false);
        when(mock.fetchUserProfile(any())).thenReturn(profile());
        return new TestIdentityProviderFactory(mock);
    }

    private static UserAggregate userAggregateState() {
        User state = UserVBuilder.newBuilder()
                                 .setId(userId())
                                 .setParentEntity(parentEntity())
                                 .setDisplayName(displayName())
                                 .setPrimaryAuthIdentity(identity())
                                 .setProfile(profile())
                                 .setStatus(NOT_READY)
                                 .addSecondaryAuthIdentity(identity())
                                 .putAttributes(attributeName(), attributeValue())
                                 .addRole(adminRoleId())
                                 .build();
        UserAggregate aggregate = Given.aggregateOfClass(UserAggregate.class)
                                       .withState(state)
                                       .withId(userId())
                                       .build();
        return aggregate;
    }

    static String attributeName() {
        return "auth_token";
    }

    static Any attributeValue() {
        return pack(StringValue.of("encrypted-auth-token-value"));
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

    private static ParentEntity parentEntity() {
        return ParentEntityVBuilder.newBuilder()
                                   .setOrganization(organizationId())
                                   .build();
    }

    public static UserAuthIdentity identity() {
        return UserAuthIdentityVBuilder.newBuilder()
                                       .setDisplayName("j.s@google.com")
                                       .setProviderId(googleProviderId())
                                       .setUid("123543")
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
            this.provider = provider;
        }

        @Override
        public Optional<IdentityProviderBridge> get(IdentityProviderId id) {
            return Optional.of(provider);
        }
    }
}
