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

package io.spine.users.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.spine.client.ActorRequestFactory;
import io.spine.core.TenantId;
import io.spine.users.user.Identity;
import org.checkerframework.checker.nullness.qual.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.flogger.LazyArgs.lazy;
import static com.google.protobuf.TextFormat.shortDebugString;
import static io.spine.util.Exceptions.newIllegalStateException;

/**
 * A session of the user's work in a client-side application.
 *
 * <p>The session starts when the user logs-in into the application using a 3rd party authentication
 * service. The session is started using user credentials obtained upon successful authentication.
 */
public final class Session {

    private final @Nullable TenantId tenant;
    private final Client client;

    private @Nullable ActorRequestFactory requestFactory;

    /**
     * Creates a client user session in a multi-tenant application.
     *
     * @param tenant
     *         the ID of the tenant in a multi-tenant application
     * @param host
     *         the target address to which the client application connects
     * @param port
     *         the port to which the client application connects
     */
    public Session(@Nullable TenantId tenant, String host, int port) {
        this.tenant = tenant;
        this.client = new Client(host, port);
    }

    /**
     * Creates a client user session in a single-tenant application.
     *
     * @param host
     *         the target address to which the client application connects
     * @param port
     *         the port to which the client application connects
     */
    public Session(String host, int port) {
        this(null, host, port);
    }

    /**
     * Verifies if the user is logged into the application.
     */
    public boolean active() {
        return requestFactory != null;
    }

    /**
     * Logs in the user with the passed identity.
     */
    public void logIn(Identity identity) {
        if (active()) {
            throw newIllegalStateException(
                    "The user with identity %s is already logged in.",
                    shortDebugString(identity)
            );
        }

        //TODO:2019-10-01:alexander.yevsyukov: Send the command to log in the user.
    }
}
