/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin;

import com.google.protobuf.Message;
import io.spine.core.UserId;
import io.spine.server.entity.Repository;
import io.spine.testing.server.procman.ProcessManagerCommandTest;

import static io.spine.users.signin.given.SignInTestEnv.userId;

/**
 * An implementation base for the {@link RemoteIdentitySignInProcMan} command handler tests.
 *
 * @author Vladyslav Lubenskyi
 */
abstract class RemoteIdentitySignInPmCommandTest<C extends Message>
        extends ProcessManagerCommandTest<UserId, C, RemoteIdentitySignIn, RemoteIdentitySignInProcMan> {

    @Override
    protected final UserId newId() {
        return userId();
    }

    @Override
    protected Repository<UserId, RemoteIdentitySignInProcMan> createEntityRepository() {
        return new RemoteIdentitySignInProcManRepository();
    }
}
