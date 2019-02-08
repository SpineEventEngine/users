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

package io.spine.users.server.group.google.given;

import io.spine.users.GroupId;
import io.spine.users.google.group.event.GoogleGroupAliasesChanged;
import io.spine.users.google.group.event.GoogleGroupAliasesChangedVBuilder;
import io.spine.users.google.group.event.GoogleGroupCreated;
import io.spine.users.google.group.event.GoogleGroupCreatedVBuilder;
import io.spine.users.google.group.event.GoogleGroupDeleted;
import io.spine.users.google.group.event.GoogleGroupDeletedVBuilder;
import io.spine.users.google.group.event.GoogleGroupDescriptionChanged;
import io.spine.users.google.group.event.GoogleGroupDescriptionChangedVBuilder;
import io.spine.users.google.group.event.GoogleGroupEmailChanged;
import io.spine.users.google.group.event.GoogleGroupEmailChangedVBuilder;
import io.spine.users.google.group.event.GoogleGroupJoinedParentGroup;
import io.spine.users.google.group.event.GoogleGroupJoinedParentGroupVBuilder;
import io.spine.users.google.group.event.GoogleGroupLeftParentGroup;
import io.spine.users.google.group.event.GoogleGroupLeftParentGroupVBuilder;
import io.spine.users.google.group.event.GoogleGroupRenamed;
import io.spine.users.google.group.event.GoogleGroupRenamedVBuilder;
import io.spine.users.server.group.google.GoogleGroupLifecyclePm;

/**
 * Test events for {@link GoogleGroupLifecyclePm}.
 *
 * @author Vladyslav Lubenskyi
 */
public class GoogleGroupTestEvents {

    /**
     * Prevents instantiation.
     */
    private GoogleGroupTestEvents() {
    }

    public static GoogleGroupCreated internalGoogleGroupCreated(GroupId id) {
        return GoogleGroupCreatedVBuilder.newBuilder()
                                         .setId(id)
                                         .setGoogleId(GoogleGroupTestEnv.googleId())
                                         .setDisplayName(GoogleGroupTestEnv.displayName())
                                         .setEmail(GoogleGroupTestEnv.email())
                                         .setDomain(GoogleGroupTestEnv.internalDomain())
                                         .addAlias(GoogleGroupTestEnv.alias())
                                         .setDescription(GoogleGroupTestEnv.description())
                                         .build();
    }

    public static GoogleGroupCreated externalGoogleGroupCreated(GroupId id) {
        return GoogleGroupCreatedVBuilder.newBuilder()
                                         .setId(id)
                                         .setGoogleId(GoogleGroupTestEnv.googleId())
                                         .setDisplayName(GoogleGroupTestEnv.displayName())
                                         .setEmail(GoogleGroupTestEnv.email())
                                         .setDomain(GoogleGroupTestEnv.internalDomain())
                                         .addAlias(GoogleGroupTestEnv.alias())
                                         .setDescription(GoogleGroupTestEnv.description())
                                         .build();
    }

    public static GoogleGroupJoinedParentGroup googleGroupJoinedParentGroup(GroupId groupId) {
        return GoogleGroupJoinedParentGroupVBuilder.newBuilder()
                                                   .setId(groupId)
                                                   .setRole(GoogleGroupTestEnv.role())
                                                   .setNewParentId(GoogleGroupTestEnv.parentGroup())
                                                   .build();
    }

    public static GoogleGroupLeftParentGroup googleGroupLeftParentGroup(GroupId groupId) {
        return GoogleGroupLeftParentGroupVBuilder.newBuilder()
                                                 .setId(groupId)
                                                 .setParentGroupId(GoogleGroupTestEnv.parentGroup())
                                                 .build();
    }

    public static GoogleGroupRenamed googleGroupRenamed(GroupId groupId) {
        return GoogleGroupRenamedVBuilder.newBuilder()
                                         .setId(groupId)
                                         .setDisplayName(GoogleGroupTestEnv.groupName())
                                         .build();
    }

    public static GoogleGroupDeleted googleGroupDeleted(GroupId groupId) {
        return GoogleGroupDeletedVBuilder.newBuilder()
                                         .setId(groupId)
                                         .build();
    }

    public static GoogleGroupEmailChanged googleGroupEmailChanged(GroupId groupId) {
        return GoogleGroupEmailChangedVBuilder.newBuilder()
                                              .setId(groupId)
                                              .setNewEmail(GoogleGroupTestEnv.newEmail())
                                              .build();
    }

    public static GoogleGroupDescriptionChanged googleGroupDescriptionChanged(GroupId groupId) {
        return GoogleGroupDescriptionChangedVBuilder.newBuilder()
                                                    .setId(groupId)
                                                    .setNewDescription(
                                                            GoogleGroupTestEnv.newDescription())
                                                    .build();
    }

    public static GoogleGroupAliasesChanged googleGroupAliasesChanged(GroupId groupId) {
        return GoogleGroupAliasesChangedVBuilder
                .newBuilder()
                .setId(groupId)
                .addNewAlias(GoogleGroupTestEnv.newEmail())
                .build();
    }
}
