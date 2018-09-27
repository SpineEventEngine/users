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

package io.spine.users;

import io.spine.server.BoundedContext;
import io.spine.users.c.IdentityProviderBridgeFactory;
import io.spine.users.c.group.GroupMembershipPartRepository;
import io.spine.users.c.group.GroupPartRepository;
import io.spine.users.c.organization.OrganizationAggregateRepository;
import io.spine.users.c.orgunit.OrgUnitAggregateRepository;
import io.spine.users.c.role.RoleAggregateRepository;
import io.spine.users.c.signin.SignInPmRepository;
import io.spine.users.c.user.UserMembershipPartRepository;
import io.spine.users.c.user.UserPartRepository;
import io.spine.users.google.q.GoogleIdMappingRepository;
import io.spine.users.q.group.GroupViewProjectionRepository;

/**
 * An abstract class to assemble 'Users' bounded context.
 *
 * @author Vladyslav Lubenskyi
 */
public abstract class AbstractUsersBoundedContext {

    protected AbstractUsersBoundedContext() {
    }

    public final BoundedContext create() {
        BoundedContext context = initBoundedContext();
        UserPartRepository userRepository = new UserPartRepository();
        context.register(userRepository);
        context.register(new UserMembershipPartRepository());
        context.register(new GroupPartRepository());
        context.register(new GroupMembershipPartRepository());
        context.register(new OrganizationAggregateRepository());
        context.register(new OrgUnitAggregateRepository());
        context.register(new RoleAggregateRepository());
        context.register(new SignInPmRepository(identityProviderFactory(), userRepository));
        context.register(new GoogleIdMappingRepository());
        context.register(new GroupViewProjectionRepository());
        return context;
    }

    protected abstract BoundedContext initBoundedContext();

    protected abstract IdentityProviderBridgeFactory identityProviderFactory();

}
