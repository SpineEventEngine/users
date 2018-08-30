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
import io.spine.users.IdentityProviderId;
import io.spine.users.user.UserAggregateRepository;

/**
 * An implementation base for the {@link SignInPm} command handler tests.
 *
 * @author Vladyslav Lubenskyi
 */
abstract class SignInPmCommandTest<C extends Message>
        extends PmCommandOnCommandTest<UserId, C, SignIn, SignInPm> {

    protected SignInPmCommandTest(UserId processManagerId, C commandMessage) {
        super(processManagerId, commandMessage);
    }

    @Override
    protected Repository<UserId, SignInPm> createEntityRepository() {
        return new SignInPmRepository(identityProviderFactory(), userRepository());
    }

    protected IdentityProviderFactory identityProviderFactory() {
        return new IdentityProviderFactory() {

            @Override
            public IdentityProvider get(IdentityProviderId id) {
                return identityProvider();
            }
        };
    }

    abstract IdentityProvider identityProvider();

    abstract UserAggregateRepository userRepository();
}
