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

package io.spine.users.server.user.given;

import io.spine.core.UserId;
import io.spine.users.server.user.UserPart;
import io.spine.users.user.Identity;
import io.spine.users.user.User.Status;
import io.spine.users.user.command.AddSecondaryIdentity;
import io.spine.users.user.command.AddSecondaryIdentityVBuilder;
import io.spine.users.user.command.AssignRoleToUser;
import io.spine.users.user.command.AssignRoleToUserVBuilder;
import io.spine.users.user.command.ChangePrimaryIdentity;
import io.spine.users.user.command.ChangePrimaryIdentityVBuilder;
import io.spine.users.user.command.ChangeUserStatus;
import io.spine.users.user.command.ChangeUserStatusVBuilder;
import io.spine.users.user.command.CreateUser;
import io.spine.users.user.command.CreateUserVBuilder;
import io.spine.users.user.command.DeleteUser;
import io.spine.users.user.command.DeleteUserVBuilder;
import io.spine.users.user.command.JoinGroup;
import io.spine.users.user.command.JoinGroupVBuilder;
import io.spine.users.user.command.LeaveGroup;
import io.spine.users.user.command.LeaveGroupVBuilder;
import io.spine.users.user.command.MoveUser;
import io.spine.users.user.command.MoveUserVBuilder;
import io.spine.users.user.command.RemoveSecondaryIdentity;
import io.spine.users.user.command.RemoveSecondaryIdentityVBuilder;
import io.spine.users.user.command.RenameUser;
import io.spine.users.user.command.RenameUserVBuilder;
import io.spine.users.user.command.UnassignRoleFromUser;
import io.spine.users.user.command.UnassignRoleFromUserVBuilder;
import io.spine.users.user.command.UpdatePersonProfile;
import io.spine.users.user.command.UpdatePersonProfileVBuilder;

import static io.spine.users.server.user.given.UserTestEnv.adminRoleId;
import static io.spine.users.server.user.given.UserTestEnv.editorRoleId;
import static io.spine.users.server.user.given.UserTestEnv.firstGroupId;
import static io.spine.users.server.user.given.UserTestEnv.githubIdentity;
import static io.spine.users.server.user.given.UserTestEnv.googleIdentity;
import static io.spine.users.server.user.given.UserTestEnv.newProfile;
import static io.spine.users.server.user.given.UserTestEnv.newUserDisplayName;
import static io.spine.users.server.user.given.UserTestEnv.newUserOrgEntity;
import static io.spine.users.server.user.given.UserTestEnv.profile;
import static io.spine.users.server.user.given.UserTestEnv.userDisplayName;
import static io.spine.users.server.user.given.UserTestEnv.userOrgEntity;
import static io.spine.users.user.RoleInGroup.MEMBER;
import static io.spine.users.user.User.Status.NOT_READY;
import static io.spine.users.user.UserNature.PERSON;

/**
 * Test commands for {@link UserPart}.
 */
public class UserTestCommands {

    /**
     * Prevents direct instantiation.
     */
    private UserTestCommands() {
    }

    /**
     * Creates new {@link CreateUser} command.
     */
    public static CreateUser createUser(UserId id) {
        return CreateUserVBuilder.newBuilder()
                                 .setId(id)
                                 .setDisplayName(userDisplayName())
                                 .setOrgEntity(userOrgEntity())
                                 .setPrimaryIdentity(googleIdentity())
                                 .setProfile(profile())
                                 .setStatus(NOT_READY)
                                 .setNature(PERSON)
                                 .build();
    }

    public static MoveUser moveUser(UserId id) {
        return MoveUserVBuilder.newBuilder()
                               .setId(id)
                               .setNewOrgEntity(newUserOrgEntity())
                               .build();
    }

    public static JoinGroup startGroupMembership(UserId id) {
        return JoinGroupVBuilder.newBuilder()
                                .setId(id)
                                .setGroupId(firstGroupId())
                                .setRole(MEMBER)
                                .build();
    }

    public static LeaveGroup stopGroupMembership(UserId id) {
        return LeaveGroupVBuilder.newBuilder()
                                 .setId(id)
                                 .setGroupId(firstGroupId())
                                 .build();
    }

    public static DeleteUser deleteUser(UserId id) {
        return DeleteUserVBuilder.newBuilder()
                                 .setId(id)
                                 .build();
    }

    public static AssignRoleToUser assignRoleToUser(UserId id) {
        return AssignRoleToUserVBuilder.newBuilder()
                                       .setId(id)
                                       .setRoleId(editorRoleId())
                                       .build();
    }

    public static UnassignRoleFromUser unassignRoleFromUser(UserId id) {
        return UnassignRoleFromUserVBuilder.newBuilder()
                                           .setId(id)
                                           .setRoleId(adminRoleId())
                                           .build();
    }

    public static AddSecondaryIdentity addAuthIdentity(UserId id) {
        return AddSecondaryIdentityVBuilder.newBuilder()
                                           .setId(id)
                                           .setIdentity(googleIdentity())
                                           .build();
    }

    public static RemoveSecondaryIdentity removeAuthIdentity(UserId id) {
        Identity identity = googleIdentity();
        return RemoveSecondaryIdentityVBuilder.newBuilder()
                                              .setId(id)
                                              .setDirectoryId(identity.getDirectoryId())
                                              .setUserId(identity.getUserId())
                                              .build();
    }

    public static ChangeUserStatus changeUserStatus(UserId id) {
        return ChangeUserStatusVBuilder.newBuilder()
                                       .setId(id)
                                       .setStatus(Status.SUSPENDED)
                                       .build();
    }

    public static ChangePrimaryIdentity changePrimaryIdentity(UserId id) {
        return ChangePrimaryIdentityVBuilder.newBuilder()
                                            .setId(id)
                                            .setIdentity(githubIdentity())
                                            .build();
    }

    public static RenameUser renameUser(UserId id) {
        return RenameUserVBuilder.newBuilder()
                                 .setId(id)
                                 .setNewName(newUserDisplayName())
                                 .build();
    }

    public static UpdatePersonProfile updatePersonProfile(UserId id) {
        return UpdatePersonProfileVBuilder.newBuilder()
                                          .setId(id)
                                          .setUpdatedProfile(newProfile())
                                          .build();
    }
}
