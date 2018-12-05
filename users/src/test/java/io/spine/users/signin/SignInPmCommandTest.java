/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.signin;

import io.spine.base.CommandMessage;
import io.spine.core.UserId;
import io.spine.server.entity.Repository;
import io.spine.testing.server.procman.PmCommandTest;
import io.spine.users.c.IdentityProviderBridgeFactory;
import io.spine.users.c.user.UserPartRepository;

import static org.mockito.Mockito.mock;

/**
 * An implementation base for the {@link SignInPm} command handler tests.
 *
 * @param <C> the type of the command being tested
 * @author Vladyslav Lubenskyi
 */
abstract class SignInPmCommandTest<C extends CommandMessage>
        extends PmCommandTest<UserId, C, SignIn, SignInPm> {

    SignInPmCommandTest(UserId processManagerId, C commandMessage) {
        super(processManagerId, commandMessage);
    }

    @Override
    protected Repository<UserId, SignInPm> createRepository() {
        return new SignInPmRepository(mock(IdentityProviderBridgeFactory.class),
                                      mock(UserPartRepository.class));
    }
}
