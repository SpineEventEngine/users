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

import io.spine.client.Client;
import io.spine.core.Command;
import io.spine.core.UserId;
import io.spine.logging.Logging;
import io.spine.users.PersonProfile;
import io.spine.users.login.command.LogUserIn;
import io.spine.users.login.command.LogUserOut;
import io.spine.users.login.event.UserLoggedIn;
import io.spine.users.user.Identity;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.protobuf.TextFormat.shortDebugString;
import static io.spine.util.Exceptions.newIllegalStateException;

/**
 * A session of the user's work in a client-side application.
 *
 * <p>The session starts when the user logs into the application using a 3rd-party auth.
 * service. The session is started using user credentials obtained upon successful authentication.
 */
public final class Session implements AutoCloseable, Logging {

    private final Client client;
    private @Nullable UserId user;
    private @Nullable PersonProfile userProfile;

    /**
     * Creates a client user session.
     *
     * @param client
     *         the instance of the API for speaking with the backend services of the application
     */
    public Session(Client client) {
        this.client = checkNotNull(client);
    }

    /**
     * Verifies if the user is logged into the application.
     */
    public boolean active() {
        return user != null;
    }

    /**
     * Obtains the profile of the currently logged in user, or the profile the user
     * who was last logged in.
     */
    public Optional<PersonProfile> userProfile() {
        return Optional.ofNullable(userProfile);
    }

    /**
     * Request the log in of the user with the passed identity.
     *
     * <p>The method quits after the login command is posted.
     * The user is logged in when the session becomes {@link #active()}.
     *
     * @see #logOut()
     */
    public void requestLogIn(Identity identity) {
        if (active()) {
            throw newIllegalStateException(
                    "The user with the identity `%s` is already logged in with the id `%s`.",
                    shortDebugString(identity),
                    user.getValue()
            );
        }

        Command command =
                client.onBehalfOf(client.guestUser())
                      .command()
                      .create(LogUserIn.newBuilder()
                                       .setIdentity(identity)
                                       .build());

        client.forCommand(command)
              .observe(UserLoggedIn.class, this::handleLogin)
              //TODO:2019-10-14:alexander.yevsyukov: Observe the rejection
              // `UserAlreadyLoggedIn` which should also carry on the profile of the user.
              // This is to handle the cases when a client app requests the login once again
              // after the `LogOut` command was not handled by the server (for example, because
              // the client failed to post it or a communication error occurred).
              .post();
    }

    private void handleLogin(UserLoggedIn event) {
        this.user  = event.getId();
        this.userProfile = event.getUser();
    }

    /**
     * Logs out the user with the passed ID.
     *
     * <p>After this method quits the session becomes {@link #active() inactive} and will not accept
     * communication calls until a user is logged in.
     *
     * @see #requestLogIn(Identity)
     */
    public void logOut() {
        UserId user = this.user;
        if (user == null) {
            throw newIllegalStateException("The user is already logged out.");
        }
        Command logOut =
                client.onBehalfOf(user)
                      .command()
                      .create(LogUserOut.newBuilder()
                                        .setId(user)
                                        .build());
        client.postCommand(logOut);
        // We do not wait for the `UserLoggedOut` event because it is of no importance for the
        // session. We just do not allow posting requests after we requested logout from the server.
        this.user = null;
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
            logOut();
        }
        client.close();
    }
}
