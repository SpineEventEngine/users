/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user.given;

import io.spine.core.UserId;
import io.spine.users.Identity;
import io.spine.users.c.user.AddSecondaryIdentity;
import io.spine.users.c.user.AddSecondaryIdentityVBuilder;
import io.spine.users.c.user.AssignRoleToUser;
import io.spine.users.c.user.AssignRoleToUserVBuilder;
import io.spine.users.c.user.ChangePrimaryIdentity;
import io.spine.users.c.user.ChangePrimaryIdentityVBuilder;
import io.spine.users.c.user.ChangeUserStatus;
import io.spine.users.c.user.ChangeUserStatusVBuilder;
import io.spine.users.c.user.CreateUser;
import io.spine.users.c.user.CreateUserVBuilder;
import io.spine.users.c.user.DeleteUser;
import io.spine.users.c.user.DeleteUserVBuilder;
import io.spine.users.c.user.JoinGroup;
import io.spine.users.c.user.JoinGroupVBuilder;
import io.spine.users.c.user.LeaveGroup;
import io.spine.users.c.user.LeaveGroupVBuilder;
import io.spine.users.c.user.MoveUser;
import io.spine.users.c.user.MoveUserVBuilder;
import io.spine.users.c.user.RemoveSecondaryIdentity;
import io.spine.users.c.user.RemoveSecondaryIdentityVBuilder;
import io.spine.users.c.user.RenameUser;
import io.spine.users.c.user.RenameUserVBuilder;
import io.spine.users.c.user.UnassignRoleFromUser;
import io.spine.users.c.user.UnassignRoleFromUserVBuilder;
import io.spine.users.c.user.UpdatePersonProfile;
import io.spine.users.c.user.UpdatePersonProfileVBuilder;
import io.spine.users.c.user.User.Status;
import io.spine.users.c.user.UserPart;

import static io.spine.users.c.user.RoleInGroup.MEMBER;
import static io.spine.users.c.user.User.Status.NOT_READY;
import static io.spine.users.c.user.UserNature.PERSON;
import static io.spine.users.c.user.given.UserTestEnv.adminRoleId;
import static io.spine.users.c.user.given.UserTestEnv.editorRoleId;
import static io.spine.users.c.user.given.UserTestEnv.firstGroupId;
import static io.spine.users.c.user.given.UserTestEnv.githubIdentity;
import static io.spine.users.c.user.given.UserTestEnv.googleIdentity;
import static io.spine.users.c.user.given.UserTestEnv.newProfile;
import static io.spine.users.c.user.given.UserTestEnv.newUserDisplayName;
import static io.spine.users.c.user.given.UserTestEnv.newUserOrgEntity;
import static io.spine.users.c.user.given.UserTestEnv.profile;
import static io.spine.users.c.user.given.UserTestEnv.userDisplayName;
import static io.spine.users.c.user.given.UserTestEnv.userOrgEntity;

/**
 * Test commands for {@link UserPart}.
 *
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings("OverlyCoupledClass") // It is OK for a test command factory.
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
                                 .addRole(adminRoleId())
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
