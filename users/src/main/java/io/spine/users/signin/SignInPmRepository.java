/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin;

import io.spine.core.UserId;
import io.spine.server.procman.ProcessManagerRepository;
import io.spine.users.IdentityProviderBridgeFactory;
import io.spine.users.user.UserPartRepository;

/**
 * The repository for {@link SignInPm}.
 *
 * @author Vladyslav Lubenskyi
 */
public class SignInPmRepository extends ProcessManagerRepository<UserId, SignInPm, SignIn> {

    private final UserPartRepository userRepository;
    private final IdentityProviderBridgeFactory identityProviderFactory;

    public SignInPmRepository(IdentityProviderBridgeFactory identityProviderFactory,
                              UserPartRepository userRepository) {
        super();
        this.identityProviderFactory = identityProviderFactory;
        this.userRepository = userRepository;
    }

    @Override
    protected SignInPm findOrCreate(UserId id) {
        SignInPm processManager = super.findOrCreate(id);
        processManager.setUserRepository(userRepository);
        processManager.setIdentityProviderFactory(identityProviderFactory);
        return processManager;
    }
}
