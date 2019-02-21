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

import io.spine.users.PersonProfile;
import io.spine.users.user.Identity;

/**
 * A facade above a source of information about users of a system.
 *
 * <p>The {@code Users} bounded context is a framework solution and thus relies upon the third-party
 * authentication supplied by the particular application.
 *
 * <p>For {@code Users}, a directory serves as a data source and the single source of truth about
 * {@linkplain Identity user authentication identities}.
 *
 * <p>This interface may be used both for application's own and remote user data sources.
 * For example:
 *
 * <ul
 *     <li>Spring Security;
 *     <li>Google Directory API;
 *     <li>Active Directory.
 * </ul>
 */
public interface Directory {

    /**
     * Checks whether the directory is aware of the given identity.
     *
     * @param identity
     *         an authentication identity to check
     * @return {@code true} if the given identity is known to this directory,
     *         {@code false} otherwise
     */
    boolean hasIdentity(Identity identity);

    /**
     * Checks whether the given identity is allowed to perform sign-in.
     *
     * <p>For example, the specific implementation may check if an associated user account is still
     * active or has a permission to sign-in.
     *
     * @param identity
     *         an authentication identity to check
     * @return {@code true} if the given identity is allowed to sign-in, {@code false} otherwise
     */
    boolean isSignInAllowed(Identity identity);

    /**
     * Fetches the profile of the user associated with the given identity.
     *
     * @param identity
     *         an identity of the user
     * @return the user profile
     */
    PersonProfile fetchProfile(Identity identity);
}
