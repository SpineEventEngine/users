/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user.given;

import io.spine.core.UserId;
import io.spine.users.user.Identity;
import io.spine.users.user.User.Status;
import io.spine.users.user.UserPart;
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

import static io.spine.users.user.RoleInGroup.MEMBER;
import static io.spine.users.user.User.Status.NOT_READY;
import static io.spine.users.user.UserNature.PERSON;
import static io.spine.users.user.given.UserTestEnv.adminRoleId;
import static io.spine.users.user.given.UserTestEnv.editorRoleId;
import static io.spine.users.user.given.UserTestEnv.firstGroupId;
import static io.spine.users.user.given.UserTestEnv.githubIdentity;
import static io.spine.users.user.given.UserTestEnv.googleIdentity;
import static io.spine.users.user.given.UserTestEnv.newProfile;
import static io.spine.users.user.given.UserTestEnv.newUserDisplayName;
import static io.spine.users.user.given.UserTestEnv.newUserOrgEntity;
import static io.spine.users.user.given.UserTestEnv.profile;
import static io.spine.users.user.given.UserTestEnv.userDisplayName;
import static io.spine.users.user.given.UserTestEnv.userOrgEntity;

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
                                              .setProviderId(identity.getProviderId())
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
