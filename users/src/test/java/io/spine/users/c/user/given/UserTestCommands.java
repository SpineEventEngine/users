/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.c.user.given;

import com.google.protobuf.Any;
import com.google.protobuf.Empty;
import io.spine.core.UserId;
import io.spine.users.UserAuthIdentity;
import io.spine.users.c.user.AddSecondaryAuthIdentity;
import io.spine.users.c.user.AddSecondaryAuthIdentityVBuilder;
import io.spine.users.c.user.AddUserAttribute;
import io.spine.users.c.user.AddUserAttributeVBuilder;
import io.spine.users.c.user.AssignRoleToUser;
import io.spine.users.c.user.AssignRoleToUserVBuilder;
import io.spine.users.c.user.ChangePrimaryAuthIdentity;
import io.spine.users.c.user.ChangePrimaryAuthIdentityVBuilder;
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
import io.spine.users.c.user.RemoveSecondaryAuthIdentity;
import io.spine.users.c.user.RemoveSecondaryAuthIdentityVBuilder;
import io.spine.users.c.user.RemoveUserAttribute;
import io.spine.users.c.user.RemoveUserAttributeVBuilder;
import io.spine.users.c.user.RenameUser;
import io.spine.users.c.user.RenameUserVBuilder;
import io.spine.users.c.user.UnassignRoleFromUser;
import io.spine.users.c.user.UnassignRoleFromUserVBuilder;
import io.spine.users.c.user.UpdateUserAttribute;
import io.spine.users.c.user.UpdateUserAttributeVBuilder;
import io.spine.users.c.user.UpdateUserProfile;
import io.spine.users.c.user.UpdateUserProfileVBuilder;
import io.spine.users.c.user.User.Status;
import io.spine.users.c.user.UserAggregate;

import static io.spine.protobuf.AnyPacker.pack;
import static io.spine.users.c.user.User.Status.NOT_READY;
import static io.spine.users.c.user.given.UserTestEnv.adminRoleId;
import static io.spine.users.c.user.given.UserTestEnv.attributeName;
import static io.spine.users.c.user.given.UserTestEnv.attributeValue;
import static io.spine.users.c.user.given.UserTestEnv.editorRoleId;
import static io.spine.users.c.user.given.UserTestEnv.firstGroupId;
import static io.spine.users.c.user.given.UserTestEnv.githubIdentity;
import static io.spine.users.c.user.given.UserTestEnv.googleIdentity;
import static io.spine.users.c.user.given.UserTestEnv.newUserOrgEntity;
import static io.spine.users.c.user.given.UserTestEnv.newProfile;
import static io.spine.users.c.user.given.UserTestEnv.newUserDisplayName;
import static io.spine.users.c.user.given.UserTestEnv.userOrgEntity;
import static io.spine.users.c.user.given.UserTestEnv.profile;
import static io.spine.users.c.user.given.UserTestEnv.userDisplayName;

/**
 * Test commands for {@link UserAggregate}.
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
                                 .putAttributes(attributeName(), attributeValue())
                                 .setStatus(NOT_READY)
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

    public static AddSecondaryAuthIdentity addAuthIdentity(UserId id) {
        return AddSecondaryAuthIdentityVBuilder.newBuilder()
                                               .setId(id)
                                               .setIdentity(googleIdentity())
                                               .build();
    }

    public static RemoveSecondaryAuthIdentity removeAuthIdentity(UserId id) {
        UserAuthIdentity identity = googleIdentity();
        return RemoveSecondaryAuthIdentityVBuilder.newBuilder()
                                                  .setId(id)
                                                  .setProviderId(identity.getProviderId())
                                                  .setUserId(identity.getUserId())
                                                  .build();
    }

    public static AddUserAttribute addUserAttribute(UserId id) {
        return AddUserAttributeVBuilder.newBuilder()
                                       .setId(id)
                                       .setName(attributeName())
                                       .setValue(attributeValue())
                                       .build();
    }

    public static RemoveUserAttribute removeUserAttribute(UserId id) {
        return RemoveUserAttributeVBuilder.newBuilder()
                                          .setId(id)
                                          .setName(attributeName())
                                          .build();
    }

    public static UpdateUserAttribute updateUserAttribute(UserId id) {
        Any newValue = pack(Empty.getDefaultInstance());
        return UpdateUserAttributeVBuilder.newBuilder()
                                          .setId(id)
                                          .setName(attributeName())
                                          .setNewValue(newValue)
                                          .build();
    }

    public static ChangeUserStatus changeUserStatus(UserId id) {
        return ChangeUserStatusVBuilder.newBuilder()
                                       .setId(id)
                                       .setStatus(Status.SUSPENDED)
                                       .build();
    }

    public static ChangePrimaryAuthIdentity changePrimaryIdentity(UserId id) {
        return ChangePrimaryAuthIdentityVBuilder.newBuilder()
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

    public static UpdateUserProfile updateUserProfile(UserId id) {
        return UpdateUserProfileVBuilder.newBuilder()
                                        .setId(id)
                                        .setUpdatedProfile(newProfile())
                                        .build();
    }
}
