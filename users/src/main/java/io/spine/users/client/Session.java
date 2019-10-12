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

import io.grpc.stub.StreamObserver;
import io.spine.base.CommandMessage;
import io.spine.base.Field;
import io.spine.client.ActorRequestFactory;
import io.spine.client.Client;
import io.spine.client.Subscription;
import io.spine.client.Topic;
import io.spine.core.Command;
import io.spine.core.TenantId;
import io.spine.core.UserId;
import io.spine.logging.Logging;
import io.spine.users.login.command.LogUserIn;
import io.spine.users.login.command.LogUserOut;
import io.spine.users.login.event.UserLoggedIn;
import io.spine.users.user.Identity;
import org.checkerframework.checker.nullness.qual.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.protobuf.TextFormat.shortDebugString;
import static io.spine.client.Filters.eq;
import static io.spine.util.Exceptions.newIllegalStateException;

/**
 * A session of the user's work in a client-side application.
 *
 * <p>The session starts when the user logs-in into the application using a 3rd party auth.
 * service. The session is started using user credentials obtained upon successful authentication.
 */
public final class Session implements AutoCloseable, Logging {

    private final @Nullable TenantId tenant;
    private final Client client;
    private @Nullable ActorRequestFactory requestFactory;

    /**
     * Creates a client user session in a multi-tenant application.
     *
     * @param tenant
     *         the ID of the tenant in a multi-tenant application
     * @param client
     *         the instance of the API for speaking with backend services
     */
    public Session(@Nullable TenantId tenant, Client client) {
        this.tenant = tenant;
        this.client = checkNotNull(client);
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
        subscribeToLoginEvent(identity);
        CommandMessage logIn = LogUserIn
                .newBuilder()
                .setIdentity(identity)
                .build();
        client.postCommand(systemCommand(logIn));
        //TODO:2019-10-02:alexander.yevsyukov: Wait till the arrival of the event.
    }

    private void subscribeToLoginEvent(Identity identity) {
        String fieldName =
                Field.nameOf(UserLoggedIn.IDENTITY_FIELD_NUMBER, UserLoggedIn.getDescriptor());
        Topic topic = client.systemRequests()
                            .topic()
                            .select(UserLoggedIn.class)
                            .where(eq(fieldName, identity))
                            .build();
        //TODO:2019-10-02:alexander.yevsyukov: Encapsulate subscription and waiting.
        LoginObserver observer = new LoginObserver();
        Subscription loginSubscription = client.subscribeTo(topic, observer);
        observer.setSubscription(loginSubscription);
    }

    /**
     * Logs out the user with the passed ID.
     *
     * <p>After this method the session becomes {@link #active() inactive} and will not accept
     * communication calls until
     */
    public void logOut(UserId user) {
        if (!active()) {
            throw newIllegalStateException("The user `%s` is already logged out.", user.getValue());
        }
        CommandMessage logOut = LogUserOut
                .newBuilder()
                .setId(user)
                .build();
        client.postSystemCommand(logOut);
        // We do not wait for the `UserLoggedOut` event because it is of no importance for the
        // session. We just do not allow posting requests after we requested logout from the server.
        requestFactory = null;
    }

    /**
     * Closes the user session.
     *
     * <p>If the user is logged in, posts the command to log the user out.
     * Then closes the client connection.
     */
    @Override
    public void close() {
        if (active()) {
            logOut(requestFactory.actor());
        }
        client.close();
    }

    private Command systemCommand(CommandMessage c) {
        Command result = client.systemRequests().command().create(c);
        return result;
    }

    /**
     * Creates {@code RequestsFactory} for the session when reciving {@link UserLoggedIn} event.
     */
    private final class LoginObserver implements StreamObserver<UserLoggedIn> {

        private Subscription subscription;

        /**
         * Injects the instance of the subscription to be cancelled when the event is received.
         */
        private void setSubscription(Subscription subscription) {
            this.subscription = checkNotNull(subscription);
        }

        @Override
        public void onNext(UserLoggedIn value) {
            UserId user = value.getId();
            requestFactory = createRequestFactory(user);
        }

        @Override
        public void onError(Throwable t) {
            _error().withCause(t)
                    .log("Error notifying on the user logging in.");
        }

        /**
         * Cancels the subscription to the event.
         */
        @Override
        public void onCompleted() {
            client.cancel(subscription);
        }

        private ActorRequestFactory createRequestFactory(UserId user) {
            ActorRequestFactory result = ActorRequestFactory
                    .newBuilder()
                    .setTenantId(tenant)
                    .setActor(user)
                    .build();
            return result;
        }
    }
}
