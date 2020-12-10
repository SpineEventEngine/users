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
import io.spine.users.db.command.RenameGroup;
import io.spine.users.event.GroupRenamed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.db.given.Command.renameGroup;

@DisplayName("`RenameGroup` command should")
class RenameGroupTest extends GroupCommandTest<RenameGroup, GroupRenamed> {

    @Override
    protected RenameGroup command(GroupId id) {
        return renameGroup(id);
    }

    @Override
    protected GroupRenamed expectedEventAfter(RenameGroup command) {
        return GroupRenamed
                .newBuilder()
                .setGroup(command.getGroup())
                .setName(command.getName())
                .build();
    }

    @Override
    protected Group expectedStateAfter(RenameGroup command) {
        return Group
                .newBuilder()
                .setId(command.getGroup())
                .setDisplayName(command.getName().getNewValue())
                .build();
    }

    @Test
    @DisplayName("produce `GroupRenamed` event and change the display name")
    @Override
    protected void produceEventAndChangeState() {
        preCreateGroup();
        super.produceEventAndChangeState();
    }
}
