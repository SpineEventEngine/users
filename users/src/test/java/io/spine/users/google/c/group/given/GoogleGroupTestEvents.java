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

package io.spine.users.google.c.group.given;

import io.spine.users.GroupId;
import io.spine.users.google.c.group.GoogleGroupCreated;
import io.spine.users.google.c.group.GoogleGroupCreatedVBuilder;
import io.spine.users.google.c.group.GoogleGroupDeleted;
import io.spine.users.google.c.group.GoogleGroupDeletedVBuilder;
import io.spine.users.google.c.group.GoogleGroupEmailChanged;
import io.spine.users.google.c.group.GoogleGroupEmailChangedVBuilder;
import io.spine.users.google.c.group.GoogleGroupJoinedParentGroup;
import io.spine.users.google.c.group.GoogleGroupJoinedParentGroupVBuilder;
import io.spine.users.google.c.group.GoogleGroupLeftParentGroup;
import io.spine.users.google.c.group.GoogleGroupLeftParentGroupVBuilder;
import io.spine.users.google.c.group.GoogleGroupRenamed;
import io.spine.users.google.c.group.GoogleGroupRenamedVBuilder;

import static io.spine.users.google.c.group.given.GoogleGroupTestEnv.alias;
import static io.spine.users.google.c.group.given.GoogleGroupTestEnv.description;
import static io.spine.users.google.c.group.given.GoogleGroupTestEnv.displayName;
import static io.spine.users.google.c.group.given.GoogleGroupTestEnv.email;
import static io.spine.users.google.c.group.given.GoogleGroupTestEnv.googleId;
import static io.spine.users.google.c.group.given.GoogleGroupTestEnv.groupName;
import static io.spine.users.google.c.group.given.GoogleGroupTestEnv.internalDomain;
import static io.spine.users.google.c.group.given.GoogleGroupTestEnv.newEmail;
import static io.spine.users.google.c.group.given.GoogleGroupTestEnv.parentGroup;
import static io.spine.users.google.c.group.given.GoogleGroupTestEnv.role;

/**
 * Test events for {@link io.spine.users.google.c.group.GoogleGroupPm}.
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
                                         .addAlialses(alias())
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
                                         .addAlialses(alias())
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
}