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

package io.spine.users.server;

import io.spine.core.UserId;
import io.spine.server.BoundedContext;
import io.spine.server.BoundedContextBuilder;
import io.spine.server.DefaultRepository;
import io.spine.server.entity.Repository;
import io.spine.users.server.group.GroupMembershipPart;
import io.spine.users.server.group.GroupPart;
import io.spine.users.server.group.GroupRolesPropagationRepository;
import io.spine.users.server.group.google.GoogleGroupLifecyclePm;
import io.spine.users.server.group.google.GoogleIdMappingRepository;
import io.spine.users.server.organization.OrganizationAggregate;
import io.spine.users.server.orgunit.OrgUnitAggregate;
import io.spine.users.server.role.RoleAggregate;
import io.spine.users.server.signin.SignInRepository;
import io.spine.users.server.user.UserMembershipPart;
import io.spine.users.server.user.UserPart;
import io.spine.users.server.user.UserRolesRepository;

import javax.annotation.Nullable;

/**
 * A factory of {@code Users} bounded context.
 */
public final class UsersContext {

    /**
     * The name of the context.
     */
    public static final String NAME = "Users";

    /**
     * Prevents instantiation of this factory.
     */
    private UsersContext() {
    }

    /**
     * Creates a builder of the {@code Users} Context along with the registered repositories.
     *
     * <p>This method optionally accepts {@link DirectoryFactory} instance. In case it is supplied,
     * the {@link SignInRepository SignInPmRepository} is registered
     * with the supplied factory instance as a constructor argument.
     *
     * <p>If the passed {@code DirectoryFactory} instance is {@code null},
     * the {@code SignInPmRepository} is not registered in the resulting builder instance.
     *
     * @return a new instance of {@code BoundedContextBuilder} for this Bounded Context
     */
    @SuppressWarnings("OverlyCoupledMethod")    // OK, as references all the repositories.
    public static BoundedContextBuilder newBuilder(@Nullable DirectoryFactory directoryFactory) {
        Repository<UserId, UserPart> userPartRepo = DefaultRepository.of(UserPart.class);
        BoundedContextBuilder builder = BoundedContext
                .multitenant(NAME)
                .add(userPartRepo)
                .add(UserMembershipPart.class)
                .add(new UserRolesRepository())
                .add(RoleAggregate.class)
                .add(OrgUnitAggregate.class)
                .add(OrganizationAggregate.class)
                .add(GroupPart.class)
                .add(GroupMembershipPart.class)
                .add(new GroupRolesPropagationRepository())
                .add(GoogleGroupLifecyclePm.class)
                .add(new GoogleIdMappingRepository());
        if (directoryFactory != null) {
            builder.add(new SignInRepository(directoryFactory, userPartRepo));
        }
        return builder;
    }

    /**
     * Creates a builder of {@code Users} Bounded Context along with the registered repositories.
     *
     * <p>Registers all the entity repositories except for
     * {@link SignInRepository SignInPmRepository}.
     *
     * @return a new instance of {@code BoundedContextBuilder} for this BoundedContext
     * @see #newBuilder(DirectoryFactory)
     */
    public static BoundedContextBuilder newBuilder() {
        return newBuilder(null);
    }
}
