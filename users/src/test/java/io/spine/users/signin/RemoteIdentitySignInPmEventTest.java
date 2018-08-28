/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin;

import com.google.protobuf.Message;
import io.spine.core.UserId;
import io.spine.server.entity.Repository;
import io.spine.testing.server.procman.PmCommandOnEventTest;
import io.spine.users.user.UserAggregateRepository;

/**
 * An implementation base for the {@link RemoteIdentitySignInPm} event reactors tests.
 *
 * @author Vladyslav Lubenskyi
 */
abstract class RemoteIdentitySignInPmEventTest<E extends Message>
        extends PmCommandOnEventTest<UserId, E, RemoteIdentitySignIn, RemoteIdentitySignInPm> {

    protected RemoteIdentitySignInPmEventTest(UserId processManagerId, E eventMessage) {
        super(processManagerId, eventMessage);
    }

    @Override
    protected Repository<UserId, RemoteIdentitySignInPm> createEntityRepository() {
        return new RemoteIdentitySignInProcManRepository(identityProvider(), userRepository());
    }

    abstract RemoteIdentityProvider identityProvider();

    abstract UserAggregateRepository userRepository();
}
