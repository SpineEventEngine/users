/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin;

import io.spine.users.signin.identity.UserDetailsFetched;
import io.spine.users.signin.identity.UserStatusChecked;
import io.spine.users.user.UserAggregateRepository;
import io.spine.core.UserId;
import io.spine.server.procman.ProcessManagerRepository;
import io.spine.server.route.EventRouting;

import static java.util.Collections.singleton;

/**
 * The repository for {@link RemoteIdentitySignInProcMan}.
 *
 * @author Vladyslav Lubenskyi
 */
public class RemoteIdentitySignInProcManRepository
        extends ProcessManagerRepository<UserId, RemoteIdentitySignInProcMan, RemoteIdentitySignIn> {

    private UserAggregateRepository userRepository;

    public void setUserRepository(UserAggregateRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected RemoteIdentitySignInProcMan findOrCreate(UserId id) {
        RemoteIdentitySignInProcMan processManager = super.findOrCreate(id);
        processManager.setUserRepository(userRepository);
        return processManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRegistered() {
        super.onRegistered();
        EventRouting<UserId> routing = getEventRouting();

        routing.route(UserDetailsFetched.class, (event, context) -> singleton(event.getUserId()));
        routing.route(UserStatusChecked.class, (event, context) -> singleton(event.getUserId()));
    }
}
