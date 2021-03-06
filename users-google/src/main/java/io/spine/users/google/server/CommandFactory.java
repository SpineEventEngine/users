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

package io.spine.users.google.server;

import io.spine.users.db.command.AddGroupToGroup;
import io.spine.users.db.command.ChangeGroupDescription;
import io.spine.users.db.command.ChangeGroupEmail;
import io.spine.users.db.command.CreateGroup;
import io.spine.users.db.command.DeleteGroup;
import io.spine.users.db.command.RemoveGroupFromGroup;
import io.spine.users.db.command.RenameGroup;
import io.spine.users.google.event.GoogleGroupCreated;
import io.spine.users.google.event.GoogleGroupDeleted;
import io.spine.users.google.event.GoogleGroupDescriptionChanged;
import io.spine.users.google.event.GoogleGroupEmailChanged;
import io.spine.users.google.event.GoogleGroupJoinedParentGroup;
import io.spine.users.google.event.GoogleGroupLeftParentGroup;
import io.spine.users.google.event.GoogleGroupRenamed;

/**
 * Translates Google Group events into system-neutral commands.
 */
final class CommandFactory {

    private static final CommandFactory INSTANCE = new CommandFactory();

    /** Prevents direct instantiation. */
    private CommandFactory() {
    }

    static CommandFactory instance() {
        return INSTANCE;
    }

    /**
     * Creates {@link CreateGroup} command for a group from a tenant's domain.
     */
    CreateGroup createInternalGroup(GoogleGroupCreated event) {
        CreateGroup result = CreateGroup
                .newBuilder()
                .setGroup(event.getId())
                .setEmail(event.getEmail())
                .setDisplayName(event.getDisplayName())
                .vBuild();
        return result;
    }

    /**
     * Creates {@link AddGroupToGroup} command based on information from
     * {@link GoogleGroupJoinedParentGroup} event.
     */
    AddGroupToGroup joinParentGroup(GoogleGroupJoinedParentGroup event) {
        return AddGroupToGroup
                .newBuilder()
                .setGroup(event.getGroup())
                .setParentGroup(event.getNewParentId())
                .vBuild();
    }

    /**
     * Creates {@link RenameGroup} command based on information from {@link GoogleGroupRenamed}
     * event.
     */
    RenameGroup renameGroup(GoogleGroupRenamed event) {
        return RenameGroup
                .newBuilder()
                .setGroup(event.getGroup())
                .setName(event.getDisplayName())
                .vBuild();
    }

    /**
     * Creates {@link RemoveGroupFromGroup} command based on information from
     * {@link GoogleGroupLeftParentGroup} event.
     */
    RemoveGroupFromGroup leaveParentGroup(GoogleGroupLeftParentGroup event) {
        return RemoveGroupFromGroup
                .newBuilder()
                .setGroup(event.getGroup())
                .setParentGroup(event.getParentGroup())
                .vBuild();
    }

    /**
     * Creates {@link DeleteGroup} command based on information from {@link GoogleGroupDeleted}
     * event.
     */
    DeleteGroup deleteGroup(GoogleGroupDeleted event) {
        return DeleteGroup
                .newBuilder()
                .setGroup(event.getGroup())
                .vBuild();
    }

    /**
     * Creates {@link ChangeGroupEmail} command based on information from
     * {@link GoogleGroupEmailChanged} event.
     */
    ChangeGroupEmail changeEmail(GoogleGroupEmailChanged event) {
        return ChangeGroupEmail
                .newBuilder()
                .setGroup(event.getId())
                .setEmail(event.getEmail())
                .vBuild();
    }

    ChangeGroupDescription changeDescription(GoogleGroupDescriptionChanged event) {
        return ChangeGroupDescription
                .newBuilder()
                .setGroup(event.getGroup())
                .setDescription(event.getDescription())
                .vBuild();
    }
}
