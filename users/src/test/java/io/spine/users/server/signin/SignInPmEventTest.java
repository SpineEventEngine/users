/*
 * Copyright 2019, TeamDev. All rights reserved.
 *
 * Redistribution and use in source and/or binary forms, with or without
 * modification, must retain the above copyright notice and the following
 * disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.spine.users.server.signin;

import io.spine.base.EventMessage;
import io.spine.core.UserId;
import io.spine.server.entity.Repository;
import io.spine.testing.server.procman.PmCommandOnEventTest;
import io.spine.users.server.IdentityProviderBridgeFactory;
import io.spine.users.server.user.UserPartRepository;
import io.spine.users.signin.SignIn;

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
