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

import io.spine.server.BoundedContext;
import io.spine.server.BoundedContextBuilder;
import io.spine.server.DefaultRepository;
import io.spine.users.server.group.GroupMembershipPart;
import io.spine.users.server.group.GroupPart;
import io.spine.users.server.group.GroupRolesPropagationRepository;
import io.spine.users.server.group.google.GoogleGroupLifecyclePm;
import io.spine.users.server.group.google.GoogleIdMappingRepository;
import io.spine.users.server.organization.OrganizationAggregate;
import io.spine.users.server.orgunit.OrgUnitAggregate;
import io.spine.users.server.role.RoleAggregate;
import io.spine.users.server.user.UserMembershipPart;
import io.spine.users.server.user.UserPartRepository;
import io.spine.users.server.user.UserRolesRepository;

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
     * Creates a builder of {@code Users} Bounded Context along with the registered repositories.
     *
     * <p>{@link io.spine.users.server.signin.SignInPmRepository SignInPmRepository} is not
     * configured, as it requires {@link DirectoryFactory} instance configured. So it should
     * be supplied by a caller of this method.
     *
     * @return a new instance of {@code BoundedContextBuilder} for this BoundedContext
     */
    @SuppressWarnings("OverlyCoupledMethod")    // OK, as references all the repositories.
    public static BoundedContextBuilder builder() {
        UserPartRepository userPartRepo = new UserPartRepository();
        BoundedContextBuilder builder = BoundedContext
                .multitenant(NAME)
                .add(userPartRepo)
                .add(DefaultRepository.of(
                        UserMembershipPart.class))
                .add(new UserRolesRepository())
                .add(DefaultRepository.of(RoleAggregate.class))
                .add(DefaultRepository.of(OrgUnitAggregate.class))
                .add(DefaultRepository.of(OrganizationAggregate.class))
                .add(DefaultRepository.of(GroupPart.class))
                .add(DefaultRepository.of(GroupMembershipPart.class))
                .add(new GroupRolesPropagationRepository())
                .add(DefaultRepository.of(GoogleGroupLifecyclePm.class))
                .add(new GoogleIdMappingRepository());
        return builder;
    }
}
