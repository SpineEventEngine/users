/*
 * Copyright 2020, TeamDev. All rights reserved.
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

package io.spine.users.server;

import io.spine.core.CommandContext;
import io.spine.core.UserId;
import io.spine.server.aggregate.AggregatePart;
import io.spine.server.aggregate.Apply;
import io.spine.server.command.Assign;
import io.spine.users.user.Identity;
import io.spine.users.user.User;
import io.spine.users.user.command.AddSecondaryIdentity;
import io.spine.users.user.command.ChangePrimaryIdentity;
import io.spine.users.user.command.ChangeUserStatus;
import io.spine.users.user.command.CreateUser;
import io.spine.users.user.command.DeleteUser;
import io.spine.users.user.command.MoveUser;
import io.spine.users.user.command.RemoveSecondaryIdentity;
import io.spine.users.user.command.RenameUser;
import io.spine.users.user.command.UpdatePersonProfile;
import io.spine.users.user.event.PersonProfileUpdated;
import io.spine.users.user.event.PrimaryIdentityChanged;
import io.spine.users.user.event.SecondaryIdentityAdded;
import io.spine.users.user.event.SecondaryIdentityRemoved;
import io.spine.users.user.event.UserCreated;
import io.spine.users.user.event.UserDeleted;
import io.spine.users.user.event.UserMoved;
import io.spine.users.user.event.UserRenamed;
import io.spine.users.user.event.UserStatusChanged;
import io.spine.users.user.rejection.CannotMoveExternalUser;
import io.spine.users.user.rejection.IdentityDoesNotExist;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static io.spine.users.user.User.OriginCase.EXTERNAL_DOMAIN;
import static io.spine.util.Exceptions.newIllegalArgumentException;

/**
 * An aggregate for user of the application, either a person or machine.
 *
 * <p>A user is a leaf in the hierarchical structure of the organization. It can have either
 * a single {@code Organization} or single {@code OrgUnit} as a parent organizational entity.
 */
final class UserPart extends AggregatePart<UserId, User, User.Builder, UserRoot> {

    UserPart(UserRoot root) {
        super(root);
    }

    @Assign
    UserCreated handle(CreateUser command, CommandContext context) {
        UserCreated.Builder eventBuilder = UserCreated
                .newBuilder()
                .setId(command.getId())
                .setDisplayName(command.getDisplayName())
                .setPrimaryIdentity(command.getPrimaryIdentity())
                .setStatus(command.getStatus())
                .setProfile(command.getProfile())
                .setNature(command.getNature());
        switch (command.getOriginCase()) {
            case ORG_ENTITY:
                eventBuilder.setOrgEntity(command.getOrgEntity());
                break;
            case EXTERNAL_DOMAIN:
                eventBuilder.setExternalDomain(command.getExternalDomain());
                break;
            case ORIGIN_NOT_SET: // Fallthrough intended.
            default:
                throw newIllegalArgumentException("No `origin` found in `CreateUser` command.");
        }
        return eventBuilder.vBuild();
    }

    @Assign
    UserMoved handle(MoveUser command, CommandContext context) throws CannotMoveExternalUser {
        if (state().getOriginCase() == EXTERNAL_DOMAIN) {
            throw CannotMoveExternalUser
                    .newBuilder()
                    .setId(command.getId())
                    .setExternalDomain(state().getExternalDomain())
                    .build();
        }
        UserMoved event = UserMoved
                .newBuilder()
                .setId(command.getId())
                .setNewOrgEntity(command.getNewOrgEntity())
                .setOldOrgEntity(state().getOrgEntity())
                .vBuild();
        return event;
    }

    @Assign
    UserDeleted handle(DeleteUser command, CommandContext context) {
        UserDeleted event = UserDeleted
                .newBuilder()
                .setId(command.getId())
                .vBuild();
        return event;
    }

    @Assign
    UserStatusChanged handle(ChangeUserStatus command, CommandContext context) {
        UserStatusChanged event = UserStatusChanged
                .newBuilder()
                .setId(command.getId())
                .setNewStatus(command.getStatus())
                .setOldStatus(state().getStatus())
                .vBuild();
        return event;
    }

    @Assign
    SecondaryIdentityAdded handle(AddSecondaryIdentity command, CommandContext context) {
        SecondaryIdentityAdded event = SecondaryIdentityAdded
                .newBuilder()
                .setId(command.getId())
                .setIdentity(command.getIdentity())
                .vBuild();
        return event;
    }

    @Assign
    SecondaryIdentityRemoved handle(RemoveSecondaryIdentity command,
                                    CommandContext context) throws IdentityDoesNotExist {
        Optional<Identity> identityToRemove = findAuthIdentity(command);
        if (identityToRemove.isPresent()) {
            SecondaryIdentityRemoved event = SecondaryIdentityRemoved
                    .newBuilder()
                    .setId(command.getId())
                    .setIdentity(identityToRemove.get())
                    .vBuild();
            return event;
        }
        throw identityDoesNotExist(command);
    }

    @Assign
    PrimaryIdentityChanged handle(ChangePrimaryIdentity command, CommandContext context) {
        PrimaryIdentityChanged event = PrimaryIdentityChanged
                .newBuilder()
                .setId(command.getId())
                .setIdentity(command.getIdentity())
                .vBuild();
        return event;
    }

    @Assign
    UserRenamed handle(RenameUser command, CommandContext context) {
        UserRenamed event = UserRenamed
                .newBuilder()
                .setId(command.getId())
                .setNewName(command.getNewName())
                .setOldName(state().getDisplayName())
                .vBuild();
        return event;
    }

    @Assign
    PersonProfileUpdated handle(UpdatePersonProfile command, CommandContext context) {
        PersonProfileUpdated event = PersonProfileUpdated
                .newBuilder()
                .setId(command.getId())
                .setUpdatedProfile(command.getUpdatedProfile())
                .vBuild();
        return event;
    }

    @Apply
    private void on(UserCreated event) {
        User.Builder builder = builder();
        builder.setId(event.getId())
               .setDisplayName(event.getDisplayName())
               .setPrimaryIdentity(event.getPrimaryIdentity())
               .setProfile(event.getProfile())
               .setNature(event.getNature())
               .setStatus(event.getStatus());

        switch (event.getOriginCase()) {
            case ORG_ENTITY:
                builder.setOrgEntity(event.getOrgEntity());
                break;
            case EXTERNAL_DOMAIN:
                builder.setExternalDomain(event.getExternalDomain());
                break;
            case ORIGIN_NOT_SET: // Fallthrough intended.
            default:
                throw newIllegalArgumentException(
                        "No `origin` found in UserCreated event");
        }
    }

    @Apply
    private void on(UserMoved event) {
        builder().setOrgEntity(event.getNewOrgEntity());
    }

    @Apply
    private void on(@SuppressWarnings("unused") // Event data is not required.
                    UserDeleted event) {
        setDeleted(true);
    }

    @Apply
    private void on(UserStatusChanged event) {
        builder().setStatus(event.getNewStatus());
    }

    @Apply
    private void on(SecondaryIdentityAdded event) {
        builder().addSecondaryIdentity(event.getIdentity());
    }

    @Apply
    private void on(SecondaryIdentityRemoved event) {
        removeIdentity(event.getIdentity());
    }

    @Apply
    private void on(PrimaryIdentityChanged event) {
        builder().setPrimaryIdentity(event.getIdentity());
    }

    @Apply
    private void on(UserRenamed event) {
        builder().setDisplayName(event.getNewName());
    }

    @Apply
    private void on(PersonProfileUpdated event) {
        builder().setProfile(event.getUpdatedProfile());
    }

    private Optional<Identity> findAuthIdentity(RemoveSecondaryIdentity command) {
        return state().getSecondaryIdentityList()
                      .stream()
                      .filter(identityMatcher(command))
                      .findFirst();
    }

    private void removeIdentity(Identity identity) {
        List<Identity> identities = builder().getSecondaryIdentityList();
        if (identities.contains(identity)) {
            int index = identities.indexOf(identity);
            builder().removeSecondaryIdentity(index);
        }
    }

    private static Predicate<Identity> identityMatcher(RemoveSecondaryIdentity command) {
        return identity -> {
            boolean idMatches = identity.getUserId()
                                        .equals(command.getUserId());
            boolean directoryMatches = identity.getDirectoryId()
                                               .equals(command.getDirectoryId());
            return idMatches && directoryMatches;
        };
    }

    private static IdentityDoesNotExist identityDoesNotExist(RemoveSecondaryIdentity command) {
        return IdentityDoesNotExist
                .newBuilder()
                .setId(command.getId())
                .setDirectoryId(command.getDirectoryId())
                .setUserId(command.getUserId())
                .build();
    }
}
