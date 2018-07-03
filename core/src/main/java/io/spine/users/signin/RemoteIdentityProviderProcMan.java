/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin;

import com.google.protobuf.Empty;
import io.spine.users.signin.identity.CheckUserStatus;
import io.spine.users.signin.identity.FetchUserDetails;
import io.spine.users.signin.identity.UserDetailsFetched;
import io.spine.users.signin.identity.UserStatusChecked;
import io.spine.users.RemoteIdentityProviderId;
import io.spine.core.CommandContext;
import io.spine.server.procman.ProcessManager;
import io.spine.server.procman.ProcessManagerRepository;
import io.spine.server.tuple.EitherOfTwo;

/**
 * An abstract process manager to communicate with the remote identity provider.
 *
 * <p>This process has no state and no additional logic besides querying identity provider.
 *
 * <p>Unless {@linkplain ProcessManagerRepository#getCommandRouting() reconfigured}, this process
 * manager will naturally receive all commands with the {@link RemoteIdentityProviderId} as ID.
 *
 * <p>If you want to create a provider-specific implementation, that will accept only certain
 * {@linkplain RemoteIdentityProviderId IDs} (e.g. "google.com" or "Active Directory"), you should
 * check for this manually:
 *
 * <pre>
 * {@code
 *
 * class GoogleProviderProcMan extends extends RemoteIdentityProviderProcMan {
 *     private static final String GOOGLE = "google.com";
 *
 *     (at)Assign
 *     (at)Override
 *     EitherOfTwo<UserStatusChecked ,  Empty> checkStatus(CheckUserStatus command, ...) {
 *        String provider = command.getId().getValue();
 *        if (GOOGLE.equals(provider)) {
 *            return Empty.getDefaultInstance();
 *        } else {
 *            // ...
 *        }
 *     }
 * }
 * }
 * </pre>
 *
 * @author Vladyslav Lubenskyi
 */
abstract class RemoteIdentityProviderProcMan
        extends ProcessManager<RemoteIdentityProviderId,
                               RemoteIdentityProvider,
                               RemoteIdentityProviderVBuilder> {

    /**
     * @see ProcessManager#ProcessManager(Object)
     */
    RemoteIdentityProviderProcMan(RemoteIdentityProviderId id) {
        super(id);
    }

    /**
     * Checks user status from the identity provider.
     *
     * The implementations should be annotated as
     * {@link io.spine.server.command.Assign command handlers}
     *
     * @param command        a command to check the user status
     * @param commandContext a command context
     * @return {@code Empty} if the command is ignored or {@link UserStatusChecked} otherwise
     * @throws RemoteIdentityNotFound if when a remote identity provider didn't find a
     * requested identity.
     */
    @SuppressWarnings("unused") // A command handler is never used directly.
    abstract EitherOfTwo<UserStatusChecked, Empty> checkStatus(CheckUserStatus command,
                                                               CommandContext commandContext)
            throws RemoteIdentityNotFound;

    /**
     * Fetches user profile and various other user details from the identity provider.
     *
     * The implementations should be annotated as
     * {@link io.spine.server.command.Assign command handlers}
     *
     * @param command a command to fetch user details
     * @param context a command context
     * @return {@code Empty} if the command is ignored or {@link UserDetailsFetched} otherwise
     * @throws RemoteIdentityNotFound if when a remote identity provider didn't find a
     * requested identity.
     */
    @SuppressWarnings("unused") // A command handler is never used directly.
    abstract EitherOfTwo<UserDetailsFetched, Empty> fetchUserDetails(FetchUserDetails command,
                                                                     CommandContext context)
            throws RemoteIdentityNotFound;
}
