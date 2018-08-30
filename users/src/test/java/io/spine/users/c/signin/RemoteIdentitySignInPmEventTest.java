/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.signin;

import com.google.protobuf.Message;
import io.spine.core.UserId;
import io.spine.server.entity.Repository;
import io.spine.testing.server.procman.PmCommandOnEventTest;
import io.spine.users.c.user.UserAggregateRepository;

import static org.mockito.Mockito.mock;

/**
 * An implementation base for the {@link SignInPm} event reactors tests.
 *
 * @author Vladyslav Lubenskyi
 */
abstract class SignInPmEventTest<E extends Message>
        extends PmCommandOnEventTest<UserId, E, SignIn, SignInPm> {

    SignInPmEventTest(UserId processManagerId, E eventMessage) {
        super(processManagerId, eventMessage);
    }

    @Override
    protected Repository<UserId, SignInPm> createEntityRepository() {
        return new SignInPmRepository(mock(IdentityProviderFactory.class),
                                      mock(UserAggregateRepository.class));
    }
}
