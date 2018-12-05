/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin;

import io.spine.base.EventMessage;
import io.spine.core.UserId;
import io.spine.server.entity.Repository;
import io.spine.testing.server.procman.PmCommandOnEventTest;
import io.spine.users.IdentityProviderBridgeFactory;
import io.spine.users.user.UserPartRepository;

import static org.mockito.Mockito.mock;

/**
 * An implementation base for the {@link SignInPm} event reactors tests.
 *
 * @param <E> the type of the event being tested
 * @author Vladyslav Lubenskyi
 */
abstract class SignInPmEventTest<E extends EventMessage>
        extends PmCommandOnEventTest<UserId, E, SignIn, SignInPm> {

    SignInPmEventTest(UserId processManagerId, E eventMessage) {
        super(processManagerId, eventMessage);
    }

    @Override
    protected Repository<UserId, SignInPm> createRepository() {
        return new SignInPmRepository(mock(IdentityProviderBridgeFactory.class),
                                      mock(UserPartRepository.class));
    }
}
