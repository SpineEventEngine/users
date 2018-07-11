/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.user.given;

import com.google.protobuf.Any;
import com.google.protobuf.Empty;
import io.spine.users.User.Status;
import io.spine.users.UserAuthIdentity;
import io.spine.users.user.*;

import static io.spine.protobuf.AnyPacker.pack;
import static io.spine.users.User.Status.NOT_READY;
import static io.spine.users.user.given.UserTestEnv.*;

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
    public static CreateUser createUser() {
        return CreateUserVBuilder.newBuilder()
                .setId(userId())
                .setDisplayName(displayName())
                .setParentEntity(parentEntity())
                .setPrimaryIdentity(googleIdentity())
                .setProfile(profile())
                .addRole(adminRoleId())
                .addAttribute(attribute())
                .setStatus(NOT_READY)
                .build();
    }

    public static MoveUser moveUser() {
        return MoveUserVBuilder.newBuilder()
                .setId(userId())
                .setNewParentEntity(newParentEntity())
                .build();
    }

    public static StartGroupMembership startGroupMembership() {
        return StartGroupMembershipVBuilder.newBuilder()
                .setId(userId())
                .setGroupId(firstGroupId())
                .build();
    }

    public static StopGroupMembership stopGroupMembership() {
        return StopGroupMembershipVBuilder.newBuilder()
                .setId(userId())
                .setGroupId(firstGroupId())
                .build();
    }

    public static DeleteUser deleteUser() {
        return DeleteUserVBuilder.newBuilder()
                .setId(userId())
                .build();
    }

    public static AssignRoleToUser assignRoleToUser() {
        return AssignRoleToUserVBuilder.newBuilder()
                .setId(userId())
                .setRoleId(editorRoleId())
                .build();
    }

    public static UnassignRoleFromUser unassignRoleFromUser() {
        return UnassignRoleFromUserVBuilder.newBuilder()
                .setId(userId())
                .setRoleId(adminRoleId())
                .build();
    }

    public static AddAuthIdentity addAuthIdentity() {
        return AddAuthIdentityVBuilder.newBuilder()
                .setId(userId())
                .setIdentity(googleIdentity())
                .build();
    }

    public static RemoveAuthIdentity removeAuthIdentity() {
        UserAuthIdentity identity = googleIdentity();
        return RemoveAuthIdentityVBuilder.newBuilder()
                .setId(userId())
                .setProviderId(identity.getProviderId())
                .setUid(identity.getUid())
                .build();
    }

    public static AddUserAttribute addUserAttribute() {
        return AddUserAttributeVBuilder.newBuilder()
                .setId(userId())
                .setAttribute(attribute())
                .build();
    }

    public static RemoveUserAttribute removeUserAttribute() {
        return RemoveUserAttributeVBuilder.newBuilder()
                .setId(userId())
                .setAttributeName(attribute().getName())
                .build();
    }

    public static UpdateUserAttribute updateUserAttribute() {
        Any newValue = pack(Empty.getDefaultInstance());
        UserAttribute newAttribute = UserAttributeVBuilder.newBuilder()
                .setName(attribute().getName())
                .setValue(newValue)
                .build();
        return UpdateUserAttributeVBuilder.newBuilder()
                .setId(userId())
                .setAttribute(newAttribute)
                .build();
    }

    public static ChangeUserStatus changeUserStatus() {
        return ChangeUserStatusVBuilder.newBuilder()
                .setId(userId())
                .setStatus(Status.SUSPENDED)
                .build();
    }

    public static SignUserOut signUserOut() {
        return SignUserOutVBuilder.newBuilder()
                .setId(userId())
                .build();
    }

    public static ChangePrimaryAuthIdentity changePrimaryIdentity() {
        return ChangePrimaryAuthIdentityVBuilder.newBuilder()
                .setId(userId())
                .setIdentity(githubIdentity())
                .build();
    }
}
