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
import io.spine.users.db.command.DeleteUserAccount;
import io.spine.users.event.UserAccountCreated;
import io.spine.users.event.UserAccountTerminated;
import io.spine.users.rejection.UnavalableForPreviouslyDeletedAccount;
import io.spine.users.rejection.UserAccountAlreadyDeleted;
import io.spine.users.rejection.UserAccountAlreadyExists;

/**
 * An aggregate for user of the application, either a person or machine.
 *
 * <p>A user is a leaf in the hierarchical structure of the organization. It can have either
 * a single {@code Organization} or single {@code OrgUnit} as a parent organizational entity.
 */
final class UserAccountPart
        extends AggregatePart<UserId, UserAccount, UserAccount.Builder, UserRoot> {

    UserAccountPart(UserRoot root) {
        super(root);
    }

    @Assign
    UserAccountCreated handle(CreateUserAccount command)
            throws UserAccountAlreadyExists,
                   UnavalableForPreviouslyDeletedAccount {
        if (isDeleted()) {
            throw UnavalableForPreviouslyDeletedAccount
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
    UserAccountTerminated handle(DeleteUserAccount command)
            throws UserAccountAlreadyDeleted {
        if (isDeleted()) {
            throw UserAccountAlreadyDeleted
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
