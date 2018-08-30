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
import io.spine.users.c.user.AddAuthIdentity;
import io.spine.users.c.user.AddAuthIdentityVBuilder;
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
import io.spine.users.c.user.RemoveAuthIdentity;
import io.spine.users.c.user.RemoveAuthIdentityVBuilder;
import io.spine.users.c.user.RemoveUserAttribute;
import io.spine.users.c.user.RemoveUserAttributeVBuilder;
import io.spine.users.c.user.SignUserOut;
import io.spine.users.c.user.SignUserOutVBuilder;
import io.spine.users.c.user.UnassignRoleFromUser;
import io.spine.users.c.user.UnassignRoleFromUserVBuilder;
import io.spine.users.c.user.UpdateUserAttribute;
import io.spine.users.c.user.UpdateUserAttributeVBuilder;
import io.spine.users.c.user.User.Status;
import io.spine.users.c.user.UserAggregate;

import static io.spine.protobuf.AnyPacker.pack;
import static io.spine.users.c.user.User.Status.NOT_READY;
import static io.spine.users.c.user.given.UserTestEnv.adminRoleId;
import static io.spine.users.c.user.given.UserTestEnv.attributeName;
import static io.spine.users.c.user.given.UserTestEnv.attributeValue;
import static io.spine.users.c.user.given.UserTestEnv.displayName;
import static io.spine.users.c.user.given.UserTestEnv.editorRoleId;
import static io.spine.users.c.user.given.UserTestEnv.firstGroupId;
import static io.spine.users.c.user.given.UserTestEnv.githubIdentity;
import static io.spine.users.c.user.given.UserTestEnv.googleIdentity;
import static io.spine.users.c.user.given.UserTestEnv.newParentEntity;
import static io.spine.users.c.user.given.UserTestEnv.parentEntity;
import static io.spine.users.c.user.given.UserTestEnv.profile;

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
                                 .setDisplayName(displayName())
                                 .setParentEntity(parentEntity())
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
                               .setNewParentEntity(newParentEntity())
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

    public static AddAuthIdentity addAuthIdentity(UserId id) {
        return AddAuthIdentityVBuilder.newBuilder()
                                      .setId(id)
                                      .setIdentity(googleIdentity())
                                      .build();
    }

    public static RemoveAuthIdentity removeAuthIdentity(UserId id) {
        UserAuthIdentity identity = googleIdentity();
        return RemoveAuthIdentityVBuilder.newBuilder()
                                         .setId(id)
                                         .setProviderId(identity.getProviderId())
                                         .setUid(identity.getUid())
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

    public static SignUserOut signUserOut(UserId id) {
        return SignUserOutVBuilder.newBuilder()
                                  .setId(id)
                                  .build();
    }

    public static ChangePrimaryAuthIdentity changePrimaryIdentity(UserId id) {
        return ChangePrimaryAuthIdentityVBuilder.newBuilder()
                                                .setId(id)
                                                .setIdentity(githubIdentity())
                                                .build();
    }
}
