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

package io.spine.users.google.server;

import io.spine.net.InternetDomain;
import io.spine.server.command.Command;
import io.spine.server.procman.ProcessManager;
import io.spine.users.GroupId;
import io.spine.users.OrganizationId;
import io.spine.users.google.event.GoogleGroupCreated;
import io.spine.users.google.event.GoogleGroupDeleted;
import io.spine.users.google.event.GoogleGroupDescriptionChanged;
import io.spine.users.google.event.GoogleGroupEmailChanged;
import io.spine.users.google.event.GoogleGroupJoinedParentGroup;
import io.spine.users.google.event.GoogleGroupLeftParentGroup;
import io.spine.users.google.event.GoogleGroupRenamed;
import io.spine.users.group.command.ChangeGroupDescription;
import io.spine.users.group.command.ChangeGroupEmail;
import io.spine.users.group.command.CreateGroup;
import io.spine.users.group.command.DeleteGroup;
import io.spine.users.group.command.AddGroupToGroup;
import io.spine.users.group.command.RemoveGroupFromGroup;
import io.spine.users.group.command.RenameGroup;
import io.spine.users.google.GoogleGroupLifecycle;

import java.util.Optional;

import static java.util.Optional.empty;

/**
 * Translates the terminology of Google Groups events into commands in
 * the language of the Users Context.
 */
@SuppressWarnings("OverlyCoupledClass") // It is OK for a process manager.
public class GoogleGroupLifecyclePm
        extends ProcessManager<GroupId, GoogleGroupLifecycle, GoogleGroupLifecycle.Builder> {

    GoogleGroupLifecyclePm(GroupId id) {
        super(id);
    }

    @Command
    CreateGroup on(GoogleGroupCreated event) {
        InternetDomain domain = event.getDomain();
        Optional<OrganizationId> organization = organizationByDomain(domain);
        CreateGroup result =
                organization.map(id -> commands().createInternalGroup(event))
                            .orElseGet(() -> commands().createExternalGroup(event));
        return result;
    }

    @Command
    AddGroupToGroup on(GoogleGroupJoinedParentGroup event) {
        return commands().joinParentGroup(event);
    }

    @Command
    RemoveGroupFromGroup on(GoogleGroupLeftParentGroup event) {
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
    ChangeGroupEmail on(GoogleGroupEmailChanged event) {
        return commands().changeEmail(event);
    }

    @Command
    ChangeGroupDescription on(GoogleGroupDescriptionChanged event) {
        return commands().changeDescription(event);
    }

    private Optional<OrganizationId>
    organizationByDomain(@SuppressWarnings("PMD.UnusedFormalParameter") InternetDomain domain) {
        // TODO:2018-09-27:vladyslav.lubenskyi: implement this look up when Organization
        //  projection is ready
        return empty();
    }

    private static CommandFactory commands() {
        return CommandFactory.instance();
    }
}
