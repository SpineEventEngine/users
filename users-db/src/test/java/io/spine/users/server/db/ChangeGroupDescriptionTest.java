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

import io.spine.users.GroupId;
import io.spine.users.db.Group;
import io.spine.users.db.command.ChangeGroupDescription;
import io.spine.users.event.GroupDescriptionChanged;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.db.given.Command.changeGroupDescription;

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
                .setGroup(command.getGroup())
                .setDescription(command.getDescription())
                .build();
    }

    @Override
    protected Group expectedStateAfter(ChangeGroupDescription command) {
        return Group
                .newBuilder()
                .setId(command.getGroup())
                .setDescription(command.getDescription().getNewValue())
                .build();
    }
}
