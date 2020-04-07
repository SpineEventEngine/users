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

import io.spine.server.aggregate.AggregatePart;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.GroupId;
import io.spine.users.group.Group;
import io.spine.users.group.command.ChangeGroupDescription;
import io.spine.users.group.command.ChangeGroupEmail;
import io.spine.users.group.command.CreateGroup;
import io.spine.users.group.command.DeleteGroup;
import io.spine.users.group.command.RenameGroup;
import io.spine.users.group.event.GroupCreated;
import io.spine.users.group.event.GroupDeleted;
import io.spine.users.group.event.GroupDescriptionChanged;
import io.spine.users.group.event.GroupEmailChanged;
import io.spine.users.group.event.GroupRenamed;
import io.spine.users.group.rejection.GroupAlreadyExists;
import io.spine.users.organization.Organization;
import io.spine.users.orgunit.OrgUnit;

/**
 * An aggregate part of a {@link GroupRoot} that handles basic lifecycle events of a group.
 *
 * <p>A {@code Group} is the way to group {@linkplain UserAccountPart users} by their common functions
 * and functional roles inside of an {@linkplain Organization organization} or
 * {@linkplain OrgUnit organizational unit}.
 *
 * <p>The roles, assigned to a group are implicitly inherited by all members of the group,
 * including sub-groups.
 */
final class GroupAccount extends AggregatePart<GroupId, Group, Group.Builder, GroupRoot> {

    GroupAccount(GroupRoot root) {
        super(root);
    }

    @Assign
    GroupCreated handle(CreateGroup command) throws GroupAlreadyExists {
        boolean alreadyExists =
                !state().getDisplayName()
                        .isEmpty();
        GroupId group = command.getGroup();
        if (alreadyExists) {
            throw GroupAlreadyExists
                    .newBuilder()
                    .setGroup(group)
                    .build();
        }
        return GroupCreated
                .newBuilder()
                .setGroup(group)
                .setDisplayName(command.getDisplayName())
                .setEmail(command.getEmail())
                .setDescription(command.getDescription())
                .build();
    }

    @Assign
    GroupDeleted handle(DeleteGroup command) {
        return GroupDeleted
                .newBuilder()
                .setGroup(command.getGroup())
                .build();
    }

   @Assign
    GroupRenamed handle(RenameGroup command) {
        return GroupRenamed
                .newBuilder()
                .setGroup(command.getGroup())
                .setNewName(command.getNewName())
                .setOldName(state().getDisplayName())
                .build();
    }

    @Assign
    GroupEmailChanged handle(ChangeGroupEmail command) {
        return GroupEmailChanged
                .newBuilder()
                .setGroup(command.getGroup())
                .setNewEmail(command.getNewEmail())
                .setOldEmail(state().getEmail())
                .build();
    }

    @Assign
    GroupDescriptionChanged handle(ChangeGroupDescription command) {
        return GroupDescriptionChanged
                .newBuilder()
                .setGroup(command.getGroup())
                .setNewDescription(command.getDescription())
                .setOldDescription(state().getDescription())
                .build();
    }

    @Apply
    private void on(GroupCreated event) {
        Group.Builder builder = builder();
        builder.setId(event.getGroup())
               .setDisplayName(event.getDisplayName())
               .setDescription(event.getDescription())
               .setEmail(event.getEmail());
    }

    @Apply
    private void on(@SuppressWarnings("unused") GroupDeleted event) {
        setDeleted(true);
    }

    @Apply
    private void on(GroupRenamed event) {
        builder().setDisplayName(event.getNewName());
    }

    @Apply
    private void on(GroupEmailChanged event) {
        builder().setEmail(event.getNewEmail());
    }

    @Apply
    private void on(GroupDescriptionChanged event) {
        builder().setDescription(event.getNewDescription());
    }
}
