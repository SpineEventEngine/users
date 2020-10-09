/*
 * Copyright 2020, TeamDev. All rights reserved.
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

package io.spine.users.server.db;

import io.spine.core.UserId;
import io.spine.server.aggregate.AggregatePart;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.User;
import io.spine.users.db.UserAccount;
import io.spine.users.db.command.CreateUserAccount;
import io.spine.users.db.command.TerminateUserAccount;
import io.spine.users.db.rejection.UnavailableForTerminatedAccount;
import io.spine.users.db.rejection.UserAccountAlreadyExists;
import io.spine.users.db.rejection.UserAccountAlreadyTerminated;
import io.spine.users.event.UserAccountCreated;
import io.spine.users.event.UserAccountTerminated;

/**
 * Manages a user account stored locally in the database of an application.
 */
final class UserAccountPart
        extends AggregatePart<UserId, UserAccount, UserAccount.Builder, UserRoot> {

    UserAccountPart(UserRoot root) {
        super(root);
    }

    @Assign
    UserAccountCreated handle(CreateUserAccount command)
            throws UserAccountAlreadyExists,
                   UnavailableForTerminatedAccount {
        if (isDeleted()) {
            throw UnavailableForTerminatedAccount
                    .newBuilder()
                    .setAccount(id())
                    .build();
        }
        boolean userAlreadyExists =
                !User.getDefaultInstance()
                     .equals(state().getUser());
        if (userAlreadyExists) {
            throw UserAccountAlreadyExists
                    .newBuilder()
                    .setAccount(id())
                    .build();
        }
        return UserAccountCreated
                .newBuilder()
                .setAccount(command.getAccount())
                .setUser(command.getUser())
                .vBuild();
    }

    @Assign
    UserAccountTerminated handle(TerminateUserAccount command)
            throws UserAccountAlreadyTerminated {
        if (isDeleted()) {
            throw UserAccountAlreadyTerminated
                    .newBuilder()
                    .setAccount(id())
                    .build();
        }
        return UserAccountTerminated
                .newBuilder()
                .setAccount(command.getAccount())
                .vBuild();
    }

    @Apply
    private void on(UserAccountCreated event) {
        builder().setUser(event.getUser());
    }

    @Apply
    private void on(@SuppressWarnings("unused") UserAccountTerminated event) {
        setDeleted(true);
    }
}
