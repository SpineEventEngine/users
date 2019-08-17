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

import io.spine.core.UserId;
import io.spine.server.entity.Repository;
import io.spine.server.procman.ProcessManagerRepository;
import io.spine.users.server.DirectoryFactory;
import io.spine.users.server.user.UserPart;
import io.spine.users.signin.SignIn;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The repository for {@link SignInPm}.
 *
 * @author Vladyslav Lubenskyi
 */
public class SignInPmRepository extends ProcessManagerRepository<UserId, SignInPm, SignIn> {

    private final Repository<UserId, UserPart> userRepository;
    private final DirectoryFactory directoryFactory;

    public SignInPmRepository(DirectoryFactory directoryFactory,
                              Repository<UserId, UserPart> userRepository) {
        super();
        this.directoryFactory = checkNotNull(directoryFactory);
        this.userRepository = checkNotNull(userRepository);
    }

    @Override
    protected SignInPm findOrCreate(UserId id) {
        SignInPm processManager = super.findOrCreate(id);
        processManager.setUserRepository(userRepository);
        processManager.setDirectoryFactory(directoryFactory);
        return processManager;
    }
}
