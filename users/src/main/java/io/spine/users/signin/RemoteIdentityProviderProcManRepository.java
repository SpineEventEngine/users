/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin;

import io.spine.users.RemoteIdentityProviderId;
import io.spine.server.procman.ProcessManagerRepository;

/**
 * The abstract repository for {@link RemoteIdentityProviderProcMan}.
 *
 * @author Vladyslav Lubenskyi
 */
abstract public class RemoteIdentityProviderProcManRepository<P extends RemoteIdentityProviderProcMan>
        extends ProcessManagerRepository<RemoteIdentityProviderId, P, RemoteIdentityProvider> {
}
