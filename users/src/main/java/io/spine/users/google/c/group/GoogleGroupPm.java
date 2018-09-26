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

package io.spine.users.google.c.group;

import io.spine.server.command.Command;
import io.spine.server.procman.ProcessManager;
import io.spine.users.GroupId;
import io.spine.users.c.group.AssignRoleToGroup;
import io.spine.users.c.group.ChangeGroupEmail;
import io.spine.users.c.group.CreateGroup;
import io.spine.users.c.group.DeleteGroup;
import io.spine.users.c.group.JoinParentGroup;
import io.spine.users.c.group.LeaveParentGroup;
import io.spine.users.c.group.RenameGroup;
import io.spine.users.c.group.UnassignRoleFromGroup;

/**
 * A Google Group process manager.
 *
 * <p>Translates the terminology of Google Groups into 'Users' bounded context language.
 *
 * <p>This process manager handles external events happened to a Google Group and transforms them
 * into native 'Users' commands:
 *
 * <ul>
 *     <li>{@link GoogleGroupCreated} event into {@link CreateGroup} command;
 *     <li>{@link GoogleGroupJoinedParentGroup} event into {@link JoinParentGroup} command;
 *     <li>{@link GoogleGroupLeftParentGroup} event into {@link JoinParentGroup} command;
 *     <li>{@link GoogleGroupRenamed} event into {@link RenameGroup} command;
 *     <li>{@link GoogleGroupDeleted} event into {@link DeleteGroup} command;
 *     <li>{@link RoleAssignedToGoogleGroup} event into {@link AssignRoleToGroup} command;
 *     <li>{@link RoleUnassignedFromGoogleGroup} event into {@link UnassignRoleFromGroup} command;
 *     <li>{@link GoogleGroupEmailChanged} event into {@link ChangeGroupEmail} command.
 * </ul>
 *
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings("OverlyCoupledClass") // It is OK for a process manager.
public class GoogleGroupPm extends ProcessManager<GroupId, GoogleGroup, GoogleGroupVBuilder> {

    /**
     * @see ProcessManager#ProcessManager(Object)
     */
    protected GoogleGroupPm(GroupId id) {
        super(id);
    }

    @Command
    CreateGroup on(GoogleGroupCreated event) {
        return commands().createGroup(event);
    }

    @Command
    JoinParentGroup on(GoogleGroupJoinedParentGroup event) {
        return commands().joinParentGroup(event);
    }

    @Command
    LeaveParentGroup on(GoogleGroupLeftParentGroup event) {
        return commands().leaveParentGroup(event);
    }

    @Command
    RenameGroup on(GoogleGroupRenamed event) {
        return commands().renameGroup(event);
    }

    @Command
    DeleteGroup on(GoogleGroupDeleted event) {
        return commands().deleteGroup(event);
    }

    @Command
    AssignRoleToGroup on(RoleAssignedToGoogleGroup event) {
        return commands().assignRole(event);
    }

    @Command
    UnassignRoleFromGroup on(RoleUnassignedFromGoogleGroup event) {
        return commands().unassignRole(event);
    }

    @Command
    ChangeGroupEmail on(GoogleGroupEmailChanged event) {
        return commands().changeEmail(event);
    }

    private static GoogleGroupCommandFactory commands() {
        return GoogleGroupCommandFactory.instance();
    }
}
