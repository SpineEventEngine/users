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

import io.spine.core.CommandContext;
import io.spine.core.UserId;
import io.spine.server.aggregate.AggregatePart;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.user.User;
import io.spine.users.user.command.CreateUser;
import io.spine.users.user.command.DeleteUser;
import io.spine.users.user.command.RenameUser;
import io.spine.users.user.command.UpdatePersonProfile;
import io.spine.users.user.event.PersonProfileUpdated;
import io.spine.users.user.event.UserCreated;
import io.spine.users.user.event.UserDeleted;
import io.spine.users.user.event.UserRenamed;

/**
 * An aggregate for user of the application, either a person or machine.
 *
 * <p>A user is a leaf in the hierarchical structure of the organization. It can have either
 * a single {@code Organization} or single {@code OrgUnit} as a parent organizational entity.
 */
final class UserPart extends AggregatePart<UserId, User, User.Builder, UserRoot> {

    UserPart(UserRoot root) {
        super(root);
    }

    @Assign
    UserCreated handle(CreateUser command, CommandContext context) {
        UserCreated.Builder eventBuilder = UserCreated
                .newBuilder()
                .setId(command.getId())
                .setDisplayName(command.getDisplayName())
                .setProfile(command.getProfile())
                .setNature(command.getNature());
        return eventBuilder.vBuild();
    }

    @Assign
    UserDeleted handle(DeleteUser command, CommandContext context) {
        UserDeleted event = UserDeleted
                .newBuilder()
                .setId(command.getId())
                .vBuild();
        return event;
    }

    @Assign
    UserRenamed handle(RenameUser command, CommandContext context) {
        UserRenamed event = UserRenamed
                .newBuilder()
                .setId(command.getId())
                .setNewName(command.getNewName())
                .setOldName(state().getDisplayName())
                .vBuild();
        return event;
    }

    @Assign
    PersonProfileUpdated handle(UpdatePersonProfile command, CommandContext context) {
        PersonProfileUpdated event = PersonProfileUpdated
                .newBuilder()
                .setId(command.getId())
                .setUpdatedProfile(command.getUpdatedProfile())
                .vBuild();
        return event;
    }

    @Apply
    private void on(UserCreated event) {
        User.Builder builder = builder();
        builder.setId(event.getId())
               .setDisplayName(event.getDisplayName())
               .setProfile(event.getProfile())
               .setNature(event.getNature());
    }

    @Apply
    private void on(@SuppressWarnings("unused") // Event data is not required.
                    UserDeleted event) {
        setDeleted(true);
    }
    @Apply
    private void on(UserRenamed event) {
        builder().setDisplayName(event.getNewName());
    }

    @Apply
    private void on(PersonProfileUpdated event) {
        builder().setProfile(event.getUpdatedProfile());
    }
}
