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

import io.spine.server.aggregate.AggregatePart;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.GroupId;
import io.spine.users.db.Group;
import io.spine.users.db.command.ChangeGroupDescription;
import io.spine.users.db.command.ChangeGroupEmail;
import io.spine.users.db.command.CreateGroup;
import io.spine.users.db.command.DeleteGroup;
import io.spine.users.db.command.RenameGroup;
import io.spine.users.db.rejection.GroupAlreadyExists;
import io.spine.users.db.rejection.UnavailableForDeletedGroup;
import io.spine.users.event.GroupCreated;
import io.spine.users.event.GroupDeleted;
import io.spine.users.event.GroupDescriptionChanged;
import io.spine.users.event.GroupEmailChanged;
import io.spine.users.event.GroupRenamed;

/**
 * An aggregate part of a {@link GroupRoot} that handles basic lifecycle events of a group.
 *
 * <p>The roles assigned to a group are implicitly inherited by all members of the group,
 * including sub-groups.
 */
final class GroupAccountPart extends AggregatePart<GroupId, Group, Group.Builder, GroupRoot> {

    GroupAccountPart(GroupRoot root) {
        super(root);
    }

    @Assign
    GroupCreated handle(CreateGroup c)
            throws GroupAlreadyExists,
                   UnavailableForDeletedGroup {
        GroupId group = c.getGroup();
        if (isDeleted()) {
            throw UnavailableForDeletedGroup
                    .newBuilder()
                    .setGroup(group)
                    .build();
        }
        boolean alreadyExists =
                !state().getDisplayName()
                        .isEmpty();
        if (alreadyExists) {
            throw GroupAlreadyExists
                    .newBuilder()
                    .setGroup(group)
                    .build();
        }
        return GroupCreated
                .newBuilder()
                .setGroup(group)
                .setDisplayName(c.getDisplayName())
                .setEmail(c.getEmail())
                .setDescription(c.getDescription())
                .vBuild();
    }

    @Assign
    GroupDeleted handle(DeleteGroup c) {
        return GroupDeleted
                .newBuilder()
                .setGroup(c.getGroup())
                .vBuild();
    }

    @Assign
    GroupRenamed handle(RenameGroup c) {
        return GroupRenamed
                .newBuilder()
                .setGroup(c.getGroup())
                .setName(c.getName())
                .vBuild();
    }

    @Assign
    GroupEmailChanged handle(ChangeGroupEmail c) {
        return GroupEmailChanged
                .newBuilder()
                .setGroup(c.getGroup())
                .setEmail(c.getEmail())
                .vBuild();
    }

    @Assign
    GroupDescriptionChanged handle(ChangeGroupDescription c) {
        return GroupDescriptionChanged
                .newBuilder()
                .setGroup(c.getGroup())
                .setDescription(c.getDescription())
                .vBuild();
    }

    @Apply
    private void on(GroupCreated e) {
        Group.Builder builder = builder();
        builder.setId(e.getGroup())
               .setDisplayName(e.getDisplayName())
               .setDescription(e.getDescription())
               .setEmail(e.getEmail());
    }

    @Apply
    @SuppressWarnings({ "unused", "RedundantSuppression" /* to keep both IDEA and PMD happy */ })
    private void on(GroupDeleted e) {
        setDeleted(true);
    }

    @Apply
    private void on(GroupRenamed e) {
        builder().setDisplayName(e.getName().getNewValue());
    }

    @Apply
    private void on(GroupEmailChanged e) {
        builder().setEmail(e.getEmail().newValue());
    }

    @Apply
    private void on(GroupDescriptionChanged e) {
        builder().setDescription(e.getDescription().getNewValue());
    }
}
