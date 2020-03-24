/*
 * Copyright 2020, TeamDev. All rights reserved.
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

package io.spine.roles.server;

import io.spine.core.TenantId;
import io.spine.net.InternetDomain;
import io.spine.server.BoundedContext;
import io.spine.server.integration.IntegrationBroker;
import io.spine.testing.server.blackbox.BlackBoxContext;
import io.spine.users.server.UsersContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The base for Roles Context tests.
 *
 * <p>Since the Roles Context works in cooperation with the Users Context, two contexts are
 * {@link #createContexts() created} before each test.
 */
public abstract class RolesContextTest {

    private BlackBoxContext rolesContext;
    private BlackBoxContext usersContext;

    @BeforeEach
    protected void createContexts() {
        final TenantId tenant = tenantId();
        usersContext = BlackBoxContext
                .from(UsersContext.newBuilder())
                .withTenant(tenant);
        rolesContext = BlackBoxContext
                .from(RolesContext.newBuilder())
                .withTenant(tenant);
    }

    private static TenantId tenantId() {
        InternetDomain domain = InternetDomain
                .newBuilder()
                .setValue("roles.spine.io")
                .vBuild();
        return TenantId
                .newBuilder()
                .setDomain(domain)
                .vBuild();
    }

    @AfterEach
    protected void closeContexts() {
        rolesContext.close();
        usersContext.close();
    }

    protected final BlackBoxContext rolesContext() {
        return checkNotNull(rolesContext);
    }

    protected final BlackBoxContext usersContext() {
        return checkNotNull(usersContext);
    }
}
