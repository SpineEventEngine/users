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

import com.google.common.truth.extensions.proto.ProtoFluentAssertion;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.spine.base.EntityState;
import io.spine.base.EventMessage;
import io.spine.core.TenantId;
import io.spine.net.InternetDomain;
import io.spine.server.BoundedContextBuilder;
import io.spine.testing.server.EventSubject;
import io.spine.testing.server.blackbox.BlackBoxContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An abstract base for tests in {@code Users} Bounded Context.
 */
public abstract class UsersContextTest {

    private BlackBoxContext context;

    @BeforeEach
    void createContext() {
        BoundedContextBuilder builder = contextBuilder();
        context = BlackBoxContext.from(builder);
        context.withTenant(tenantId());
    }

    protected TenantId tenantId() {
        InternetDomain domain = InternetDomain
                .newBuilder()
                .setValue("users.spine.io")
                .vBuild();
        return TenantId
                .newBuilder()
                .setDomain(domain)
                .vBuild();
    }

    protected BoundedContextBuilder contextBuilder() {
        return UsersContext.newBuilder();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    protected final BlackBoxContext context() {
        return checkNotNull(context);
    }

    /**
     * Asserts that the context generated only one event of the passed type.
     *
     * @param eventClass
     *         the type of the event to assert
     * @return the subject for further assertions
     */
    @CanIgnoreReturnValue
    protected final ProtoFluentAssertion assertEvent(Class<? extends EventMessage> eventClass) {
        EventSubject assertEvents =
                context().assertEvents()
                         .withType(eventClass);
        assertEvents.hasSize(1);
        return assertEvents.message(0)
                           .comparingExpectedFieldsOnly();
    }

    /**
     * Asserts that there is an entity with the state of the passed type and ID.
     */
    protected final <I, S extends EntityState> ProtoFluentAssertion
    assertEntityState(Class<S> stateClass, I id) {
        ProtoFluentAssertion stateAssertion =
                context().assertEntityWithState(stateClass, id)
                         .hasStateThat()
                         .comparingExpectedFieldsOnly();
        return stateAssertion;
    }
}
