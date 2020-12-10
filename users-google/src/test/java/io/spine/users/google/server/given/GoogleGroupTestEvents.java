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

package io.spine.users.google.server.given;

import io.spine.change.Changes;
import io.spine.net.EmailAddress;
import io.spine.net.NetChange;
import io.spine.users.GroupId;
import io.spine.users.google.event.GoogleGroupAliasesChanged;
import io.spine.users.google.event.GoogleGroupCreated;
import io.spine.users.google.event.GoogleGroupDeleted;
import io.spine.users.google.event.GoogleGroupDescriptionChanged;
import io.spine.users.google.event.GoogleGroupEmailChanged;
import io.spine.users.google.event.GoogleGroupJoinedParentGroup;
import io.spine.users.google.event.GoogleGroupLeftParentGroup;
import io.spine.users.google.event.GoogleGroupRenamed;
import io.spine.users.google.server.GoogleGroupLifecyclePm;

import static io.spine.users.google.server.given.GoogleGroupTestEnv.alias;
import static io.spine.users.google.server.given.GoogleGroupTestEnv.description;
import static io.spine.users.google.server.given.GoogleGroupTestEnv.displayName;
import static io.spine.users.google.server.given.GoogleGroupTestEnv.email;
import static io.spine.users.google.server.given.GoogleGroupTestEnv.externalDomain;
import static io.spine.users.google.server.given.GoogleGroupTestEnv.googleId;
import static io.spine.users.google.server.given.GoogleGroupTestEnv.groupName;
import static io.spine.users.google.server.given.GoogleGroupTestEnv.internalDomain;
import static io.spine.users.google.server.given.GoogleGroupTestEnv.newDescription;
import static io.spine.users.google.server.given.GoogleGroupTestEnv.newEmail;
import static io.spine.users.google.server.given.GoogleGroupTestEnv.parentGroup;
import static io.spine.users.google.server.given.GoogleGroupTestEnv.role;

/**
 * Test events for {@link GoogleGroupLifecyclePm}.
 */
public class GoogleGroupTestEvents {

    /**
     * Prevents instantiation.
     */
    private GoogleGroupTestEvents() {
    }

    public static GoogleGroupCreated internalGoogleGroupCreated(GroupId id) {
        return GoogleGroupCreated
                .newBuilder()
                .setId(id)
                .setGoogleId(googleId())
                .setDisplayName(displayName())
                .setEmail(email())
                .setDomain(internalDomain())
                .addAlias(alias())
                .setDescription(description())
                .vBuild();
    }

    public static GoogleGroupCreated externalGoogleGroupCreated(GroupId id) {
        return GoogleGroupCreated
                .newBuilder()
                .setId(id)
                .setGoogleId(googleId())
                .setDisplayName(displayName())
                .setEmail(email())
                .setDomain(externalDomain())
                .addAlias(alias())
                .setDescription(description())
                .vBuild();
    }

    public static GoogleGroupJoinedParentGroup googleGroupJoinedParentGroup(GroupId group) {
        return GoogleGroupJoinedParentGroup
                .newBuilder()
                .setGroup(group)
                .setRole(role())
                .setNewParentId(parentGroup())
                .vBuild();
    }

    public static GoogleGroupLeftParentGroup googleGroupLeftParentGroup(GroupId group) {
        return GoogleGroupLeftParentGroup
                .newBuilder()
                .setGroup(group)
                .setParentGroup(parentGroup())
                .vBuild();
    }

    public static GoogleGroupRenamed googleGroupRenamed(GroupId group) {
        return GoogleGroupRenamed
                .newBuilder()
                .setGroup(group)
                .setDisplayName(Changes.of("", groupName()))
                .build();
    }

    public static GoogleGroupDeleted googleGroupDeleted(GroupId group) {
        return GoogleGroupDeleted
                .newBuilder()
                .setGroup(group)
                .build();
    }

    public static GoogleGroupEmailChanged googleGroupEmailChanged(GroupId group) {
        return GoogleGroupEmailChanged
                .newBuilder()
                .setId(group)
                .setEmail(NetChange.of(EmailAddress.getDefaultInstance(), newEmail()))
                .build();
    }

    public static GoogleGroupDescriptionChanged googleGroupDescriptionChanged(GroupId group) {
        return GoogleGroupDescriptionChanged
                .newBuilder()
                .setGroup(group)
                .setDescription(Changes.of("", newDescription()))
                .build();
    }

    public static GoogleGroupAliasesChanged googleGroupAliasesChanged(GroupId groupId) {
        return GoogleGroupAliasesChanged
                .newBuilder()
                .setId(groupId)
                .addNewAlias(newEmail())
                .build();
    }
}
