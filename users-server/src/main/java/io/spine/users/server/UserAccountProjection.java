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

package io.spine.users.server;

import io.spine.core.Subscribe;
import io.spine.core.UserId;
import io.spine.server.projection.Projection;
import io.spine.users.AccountStatus;
import io.spine.users.UserAccount;
import io.spine.users.event.UserAccountCreated;
import io.spine.users.event.UserAccountRestored;
import io.spine.users.event.UserAccountSuspended;
import io.spine.users.event.UserAccountTerminated;

import static io.spine.users.AccountStatus.ACCOUNT_STATUS_UNKNOWN;
import static io.spine.users.AccountStatus.ACTIVE;
import static io.spine.users.AccountStatus.SUSPENDED;
import static io.spine.users.AccountStatus.TERMINATED;

/**
 * Gathers events on a user account for building its latest representation.
 */
final class UserAccountProjection extends Projection<UserId, UserAccount, UserAccount.Builder> {

    @Subscribe
    void on(UserAccountCreated e) {
        UserAccount.Builder builder = builder();
        builder.setUser(e.getUser());
        AccountStatus status = e.getStatus() == ACCOUNT_STATUS_UNKNOWN ? ACTIVE : e.getStatus();
        builder.setStatus(status);
    }

    @Subscribe
    void on(UserAccountTerminated e) {
        builder().setStatus(TERMINATED);
    }

    @Subscribe
    void on(UserAccountSuspended e) {
        builder().setStatus(SUSPENDED);
    }

    @Subscribe
    void on(UserAccountRestored e) {
        builder().setStatus(ACTIVE);
    }
}
