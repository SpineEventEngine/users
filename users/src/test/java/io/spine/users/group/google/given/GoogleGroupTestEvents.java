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

package io.spine.users.group.google.given;

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
import io.spine.users.group.google.GoogleGroupLifecyclePm;

import static io.spine.users.group.google.given.GoogleGroupTestEnv.alias;
import static io.spine.users.group.google.given.GoogleGroupTestEnv.description;
import static io.spine.users.group.google.given.GoogleGroupTestEnv.displayName;
import static io.spine.users.group.google.given.GoogleGroupTestEnv.email;
import static io.spine.users.group.google.given.GoogleGroupTestEnv.googleId;
import static io.spine.users.group.google.given.GoogleGroupTestEnv.groupName;
import static io.spine.users.group.google.given.GoogleGroupTestEnv.internalDomain;
import static io.spine.users.group.google.given.GoogleGroupTestEnv.newDescription;
import static io.spine.users.group.google.given.GoogleGroupTestEnv.newEmail;
import static io.spine.users.group.google.given.GoogleGroupTestEnv.parentGroup;
import static io.spine.users.group.google.given.GoogleGroupTestEnv.role;

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
                                         .setGoogleId(googleId())
                                         .setDisplayName(displayName())
                                         .setEmail(email())
                                         .setDomain(internalDomain())
                                         .addAlias(alias())
                                         .setDescription(description())
                                         .build();
    }

    public static GoogleGroupCreated externalGoogleGroupCreated(GroupId id) {
        return GoogleGroupCreatedVBuilder.newBuilder()
                                         .setId(id)
                                         .setGoogleId(googleId())
                                         .setDisplayName(displayName())
                                         .setEmail(email())
                                         .setDomain(internalDomain())
                                         .addAlias(alias())
                                         .setDescription(description())
                                         .build();
    }

    public static GoogleGroupJoinedParentGroup googleGroupJoinedParentGroup(GroupId groupId) {
        return GoogleGroupJoinedParentGroupVBuilder.newBuilder()
                                                   .setId(groupId)
                                                   .setRole(role())
                                                   .setNewParentId(parentGroup())
                                                   .build();
    }

    public static GoogleGroupLeftParentGroup googleGroupLeftParentGroup(GroupId groupId) {
        return GoogleGroupLeftParentGroupVBuilder.newBuilder()
                                                 .setId(groupId)
                                                 .setParentGroupId(parentGroup())
                                                 .build();
    }

    public static GoogleGroupRenamed googleGroupRenamed(GroupId groupId) {
        return GoogleGroupRenamedVBuilder.newBuilder()
                                         .setId(groupId)
                                         .setDisplayName(groupName())
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
                                              .setNewEmail(newEmail())
                                              .build();
    }

    public static GoogleGroupDescriptionChanged googleGroupDescriptionChanged(GroupId groupId) {
        return GoogleGroupDescriptionChangedVBuilder.newBuilder()
                                                    .setId(groupId)
                                                    .setNewDescription(newDescription())
                                                    .build();
    }

    public static GoogleGroupAliasesChanged googleGroupAliasesChanged(GroupId groupId) {
        return GoogleGroupAliasesChangedVBuilder
                .newBuilder()
                .setId(groupId)
                .addNewAlias(newEmail())
                .build();
    }
}
