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

package io.spine.users.server.group;

import io.spine.users.GroupId;
import io.spine.users.group.Group;
import io.spine.users.group.command.DeleteGroup;
import io.spine.users.group.event.GroupDeleted;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.group.given.GroupTestCommands.deleteGroup;

@DisplayName("`DeleteGroup` command should")
class DeleteGroupTest extends GroupCommandTest<DeleteGroup, GroupDeleted> {

    @Test
    @DisplayName("produce `GroupDeleted` event and delete the group")
    @Override
    protected void produceEventAndChangeState() {
        preCreateGroup();
        super.produceEventAndChangeState();
    }

    @Override
    protected DeleteGroup command(GroupId id) {
        return deleteGroup(id);
    }

    @Override
    protected GroupDeleted expectedEventAfter(DeleteGroup command) {
        return GroupDeleted
                .newBuilder()
                .setId(command.getId())
                .build();
    }

    @Override
    protected Group expectedStateAfter(DeleteGroup command) {
        return Group
                .newBuilder()
                .setId(command.getId())
                .build();
    }

    @Override
    protected boolean isDeletedAfterCommand() {
        return true;
    }
}
