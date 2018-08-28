/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin;

import com.google.protobuf.Message;
import io.spine.core.UserId;
import io.spine.server.entity.Repository;
import io.spine.testing.server.procman.PmCommandOnCommandTest;
import io.spine.users.user.UserAggregateRepository;

/**
 * An implementation base for the {@link RemoteIdentitySignInPm} command handler tests.
 *
 * @author Vladyslav Lubenskyi
 */
abstract class RemoteIdentitySignInPmCommandTest<C extends Message>
        extends PmCommandOnCommandTest<UserId, C, RemoteIdentitySignIn, RemoteIdentitySignInPm> {

    protected RemoteIdentitySignInPmCommandTest(UserId processManagerId, C commandMessage) {
        super(processManagerId, commandMessage);
    }

    @Override
    protected Repository<UserId, RemoteIdentitySignInPm> createEntityRepository() {
        return new RemoteIdentitySignInProcManRepository(identityProvider(), userRepository());
    }

    abstract RemoteIdentityProvider identityProvider();

    abstract UserAggregateRepository userRepository();
}
