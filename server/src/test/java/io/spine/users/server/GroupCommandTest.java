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

import io.spine.base.CommandMessage;
import io.spine.base.EventMessage;
import io.spine.users.GroupId;
import io.spine.users.group.Group;
import io.spine.users.group.command.CreateGroup;
import io.spine.users.server.given.TestIdentifiers;

import static io.spine.users.server.group.given.GroupTestEnv.groupDescription;
import static io.spine.users.server.group.given.GroupTestEnv.groupEmail;
import static io.spine.users.server.group.given.GroupTestEnv.groupName;
import static io.spine.users.server.group.given.GroupTestEnv.groupOrgEntityOrganization;

/**
 * An implementation base for the {@link Group} aggregate command handler tests.
 *
 * @param <C>
 *         the type of the command being tested
 */
public abstract class GroupCommandTest<C extends CommandMessage, E extends EventMessage>
        extends CommandTest<GroupId, C, E, Group, GroupAccount> {

    private static final GroupId GROUP_ID = TestIdentifiers.groupId();

    @Override
    protected GroupId entityId() {
        return GROUP_ID;
    }

    @Override
    protected Class<GroupAccount> entityClass() {
        return GroupAccount.class;
    }

    /**
     * Creates the {@link GroupAccount} with the some predefined state and the {@link #GROUP_ID}
     * identifier.
     */
    protected void preCreateGroup() {
        preCreateGroup(GROUP_ID);
    }

    /**
     * Creates the {@link GroupAccount} with the some predefined state and the specified identifier.
     */
    protected void preCreateGroup(GroupId id) {
        CreateGroup createGroup = CreateGroup
                .newBuilder()
                .setId(id)
                .setOrgEntity(groupOrgEntityOrganization())
                .setDisplayName(groupName())
                .setEmail(groupEmail())
                .setDescription(groupDescription())
                .vBuild();
        context().receivesCommand(createGroup);
    }
}
