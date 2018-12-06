/*
 * Copyright 2018, TeamDev. All rights reserved.
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

import io.spine.core.UserId;
import io.spine.server.procman.ProcessManagerRepository;
import io.spine.users.server.IdentityProviderBridgeFactory;
import io.spine.users.server.user.UserPartRepository;
import io.spine.users.signin.SignIn;

/**
 * The repository for {@link SignInPm}.
 *
 * @author Vladyslav Lubenskyi
 */
public class SignInPmRepository extends ProcessManagerRepository<UserId, SignInPm, SignIn> {

    private final UserPartRepository userRepository;
    private final IdentityProviderBridgeFactory identityProviderFactory;

    public SignInPmRepository(IdentityProviderBridgeFactory identityProviderFactory,
                              UserPartRepository userRepository) {
        super();
        this.identityProviderFactory = identityProviderFactory;
        this.userRepository = userRepository;
    }

    @Override
    protected SignInPm findOrCreate(UserId id) {
        SignInPm processManager = super.findOrCreate(id);
        processManager.setUserRepository(userRepository);
        processManager.setIdentityProviderFactory(identityProviderFactory);
        return processManager;
    }
}
