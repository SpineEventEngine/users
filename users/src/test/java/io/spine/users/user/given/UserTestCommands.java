/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user.given;

import com.google.protobuf.Any;
import com.google.protobuf.Empty;
import io.spine.core.UserId;
import io.spine.users.User.Status;
import io.spine.users.UserAuthIdentity;
import io.spine.users.user.AddAuthIdentity;
import io.spine.users.user.AddAuthIdentityVBuilder;
import io.spine.users.user.AddUserAttribute;
import io.spine.users.user.AddUserAttributeVBuilder;
import io.spine.users.user.AssignRoleToUser;
import io.spine.users.user.AssignRoleToUserVBuilder;
import io.spine.users.user.ChangePrimaryAuthIdentity;
import io.spine.users.user.ChangePrimaryAuthIdentityVBuilder;
import io.spine.users.user.ChangeUserStatus;
import io.spine.users.user.ChangeUserStatusVBuilder;
import io.spine.users.user.CreateUser;
import io.spine.users.user.CreateUserVBuilder;
import io.spine.users.user.DeleteUser;
import io.spine.users.user.DeleteUserVBuilder;
import io.spine.users.user.MoveUser;
import io.spine.users.user.MoveUserVBuilder;
import io.spine.users.user.RemoveAuthIdentity;
import io.spine.users.user.RemoveAuthIdentityVBuilder;
import io.spine.users.user.RemoveUserAttribute;
import io.spine.users.user.RemoveUserAttributeVBuilder;
import io.spine.users.user.SignUserOut;
import io.spine.users.user.SignUserOutVBuilder;
import io.spine.users.user.StartGroupMembership;
import io.spine.users.user.StartGroupMembershipVBuilder;
import io.spine.users.user.StopGroupMembership;
import io.spine.users.user.StopGroupMembershipVBuilder;
import io.spine.users.user.UnassignRoleFromUser;
import io.spine.users.user.UnassignRoleFromUserVBuilder;
import io.spine.users.user.UpdateUserAttribute;
import io.spine.users.user.UpdateUserAttributeVBuilder;
import io.spine.users.user.UserAggregate;
import io.spine.users.user.UserAttribute;
import io.spine.users.user.UserAttributeVBuilder;

import static io.spine.protobuf.AnyPacker.pack;
import static io.spine.users.User.Status.NOT_READY;
import static io.spine.users.user.given.UserTestEnv.adminRoleId;
import static io.spine.users.user.given.UserTestEnv.attribute;
import static io.spine.users.user.given.UserTestEnv.displayName;
import static io.spine.users.user.given.UserTestEnv.editorRoleId;
import static io.spine.users.user.given.UserTestEnv.firstGroupId;
import static io.spine.users.user.given.UserTestEnv.githubIdentity;
import static io.spine.users.user.given.UserTestEnv.googleIdentity;
import static io.spine.users.user.given.UserTestEnv.newParentEntity;
import static io.spine.users.user.given.UserTestEnv.parentEntity;
import static io.spine.users.user.given.UserTestEnv.profile;

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
                                 .addAttribute(attribute())
                                 .setStatus(NOT_READY)
                                 .build();
    }

    public static MoveUser moveUser(UserId id) {
        return MoveUserVBuilder.newBuilder()
                               .setId(id)
                               .setNewParentEntity(newParentEntity())
                               .build();
    }

    public static StartGroupMembership startGroupMembership(UserId id) {
        return StartGroupMembershipVBuilder.newBuilder()
                                           .setId(id)
                                           .setGroupId(firstGroupId())
                                           .build();
    }

    public static StopGroupMembership stopGroupMembership(UserId id) {
        return StopGroupMembershipVBuilder.newBuilder()
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
                                       .setAttribute(attribute())
                                       .build();
    }

    public static RemoveUserAttribute removeUserAttribute(UserId id) {
        return RemoveUserAttributeVBuilder.newBuilder()
                                          .setId(id)
                                          .setAttributeName(attribute().getName())
                                          .build();
    }

    public static UpdateUserAttribute updateUserAttribute(UserId id) {
        Any newValue = pack(Empty.getDefaultInstance());
        UserAttribute newAttribute = UserAttributeVBuilder.newBuilder()
                .setName(attribute().getName())
                .setValue(newValue)
                .build();
        return UpdateUserAttributeVBuilder.newBuilder()
                                          .setId(id)
                                          .setAttribute(newAttribute)
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
