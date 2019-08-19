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
import io.spine.users.OrganizationOrUnit;
import io.spine.users.group.Group;
import io.spine.users.group.command.MoveGroup;
import io.spine.users.group.event.GroupMoved;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.group.given.GroupTestCommands.moveGroup;
import static io.spine.users.server.group.given.GroupTestEnv.groupParentOrgUnit;

@DisplayName("`MoveGroup` command should")
class MoveGroupTest extends GroupCommandTest<MoveGroup, GroupMoved> {

    private static final OrganizationOrUnit NEW_PARENT = groupParentOrgUnit();

    @Test
    @DisplayName("produce `GroupMoved` event and change the group parent OrgUnit")
    @Override
    protected void produceEventAndChangeState() {
        preCreateGroup();
        super.produceEventAndChangeState();
    }

    @Override
    protected MoveGroup command(GroupId id) {
        return moveGroup(id, NEW_PARENT);
    }

    @Override
    protected GroupMoved expectedEventAfter(MoveGroup command) {
        return GroupMoved
                .newBuilder()
                .setId(command.getId())
                .setNewOrgEntity(command.getNewOrgEntity())
                .build();
    }

    @Override
    protected Group expectedStateAfter(MoveGroup command) {
        return Group
                .newBuilder()
                .setId(command.getId())
                .setOrgEntity(command.getNewOrgEntity())
                .build();
    }
}
