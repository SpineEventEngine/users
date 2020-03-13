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
import io.spine.users.group.command.ChangeGroupDescription;
import io.spine.users.group.event.GroupDescriptionChanged;
import io.spine.users.server.GroupCommandTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.group.given.GroupTestCommands.changeGroupDescription;

@DisplayName("`ChangeGroupDescription` command should")
class ChangeGroupDescriptionTest
        extends GroupCommandTest<ChangeGroupDescription, GroupDescriptionChanged> {

    @Test
    @DisplayName("produce `GroupDescriptionChanged` event and set the updated description")
    @Override
    protected void produceEventAndChangeState() {
        preCreateGroup();
        super.produceEventAndChangeState();
    }

    @Override
    protected ChangeGroupDescription command(GroupId id) {
        return changeGroupDescription(id);
    }

    @Override
    protected GroupDescriptionChanged expectedEventAfter(ChangeGroupDescription command) {
        return GroupDescriptionChanged
                .newBuilder()
                .setId(command.getId())
                .setNewDescription(command.getDescription())
                .build();
    }

    @Override
    protected Group expectedStateAfter(ChangeGroupDescription command) {
        return Group
                .newBuilder()
                .setId(command.getId())
                .setDescription(command.getDescription())
                .build();
    }
}
