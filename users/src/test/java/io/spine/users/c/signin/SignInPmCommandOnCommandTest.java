/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.signin;

import com.google.protobuf.Message;
import io.spine.core.UserId;
import io.spine.server.entity.Repository;
import io.spine.testing.server.procman.PmCommandOnCommandTest;
import io.spine.users.c.IdentityProviderBridgeFactory;
import io.spine.users.c.user.UserPartRepository;

import static org.mockito.Mockito.mock;

/**
 * An implementation base for the {@link SignInPm} command handler tests.
 *
 * @param <C> the type of the command being tested
 * @author Vladyslav Lubenskyi
 */
abstract class SignInPmCommandOnCommandTest<C extends Message>
        extends PmCommandOnCommandTest<UserId, C, SignIn, SignInPm> {

    SignInPmCommandOnCommandTest(UserId processManagerId, C commandMessage) {
        super(processManagerId, commandMessage);
    }

    @Override
    protected Repository<UserId, SignInPm> createEntityRepository() {
        return new SignInPmRepository(mock(IdentityProviderBridgeFactory.class),
                                      mock(UserPartRepository.class));
    }
}
