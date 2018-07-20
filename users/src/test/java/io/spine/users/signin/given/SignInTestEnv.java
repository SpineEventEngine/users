/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin.given;

import io.spine.core.UserId;
import io.spine.core.UserIdVBuilder;
import io.spine.net.EmailAddress;
import io.spine.net.EmailAddressVBuilder;
import io.spine.people.PersonName;
import io.spine.people.PersonNameVBuilder;
import io.spine.testing.server.entity.given.Given;
import io.spine.time.OffsetDateTime;
import io.spine.users.*;
import io.spine.users.signin.RemoteIdentitySignInProcMan;
import io.spine.users.user.UserAggregate;
import io.spine.users.user.UserAggregateRepository;
import io.spine.users.user.UserAttribute;
import io.spine.users.user.UserAttributeVBuilder;

import java.util.Optional;

import static io.spine.protobuf.AnyPacker.pack;
import static io.spine.time.OffsetDateTimes.now;
import static io.spine.time.ZoneOffsets.utc;
import static io.spine.users.User.Status.NOT_READY;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The environment for the {@link RemoteIdentitySignInProcMan} tests.
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

    private static UserAggregate userAggregateState() {
        User state = UserVBuilder.newBuilder()
                                 .setId(userId())
                                 .setParentEntity(parentEntity())
                                 .setDisplayName(displayName())
                                 .setPrimaryAuthIdentity(identity())
                                 .setProfile(profile())
                                 .setStatus(NOT_READY)
                                 .addAuthIdentity(identity())
                                 .addAttribute(attribute())
                                 .addRole(adminRoleId())
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

    static UserAuthIdentity identity() {
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

    static UserAttribute attribute() {
        OffsetDateTime time = now(utc());
        return UserAttributeVBuilder.newBuilder()
                                    .setName("when_registered")
                                    .setValue(pack(time))
                                    .build();
    }

    static RemoteIdentityProviderId googleProviderId() {
        return RemoteIdentityProviderIdVBuilder.newBuilder()
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
}
