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

package io.spine.users.server.db;

import io.spine.base.CommandMessage;
import io.spine.base.EventMessage;
import io.spine.users.GroupId;
import io.spine.users.db.Group;
import io.spine.users.db.command.CreateGroup;
import io.spine.users.server.given.Given;

import static io.spine.users.server.db.given.Given.groupDescription;
import static io.spine.users.server.db.given.Given.groupEmail;
import static io.spine.users.server.db.given.Given.groupName;

/**
 * An implementation base for the {@link Group} aggregate command handler tests.
 *
 * @param <C>
 *         the type of the command being tested
 */
public abstract class GroupCommandTest<C extends CommandMessage, E extends EventMessage>
        extends DbCommandTest<GroupId, C, E, Group, GroupAccountPart> {

    private static final GroupId GROUP_ID = Given.groupId();

    @Override
    protected GroupId entityId() {
        return GROUP_ID;
    }

    @Override
    protected Class<GroupAccountPart> entityClass() {
        return GroupAccountPart.class;
    }

    /**
     * Creates the {@link GroupAccountPart} with the some predefined state and the {@link #GROUP_ID}
     * identifier.
     */
    protected void preCreateGroup() {
        preCreateGroup(GROUP_ID);
    }

    /**
     * Creates the {@link GroupAccountPart} with the some predefined state and the specified identifier.
     */
    protected void preCreateGroup(GroupId id) {
        CreateGroup createGroup = CreateGroup
                .newBuilder()
                .setGroup(id)
                .setDisplayName(groupName())
                .setDescription(groupDescription())
                .setEmail(groupEmail())
                .vBuild();
        context().receivesCommand(createGroup);
    }
}
