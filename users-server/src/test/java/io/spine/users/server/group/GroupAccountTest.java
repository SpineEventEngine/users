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

package io.spine.users.server.group;

import io.spine.testing.server.blackbox.BlackBoxContext;
import io.spine.users.GroupId;
import io.spine.users.Group;
import io.spine.users.command.CreateGroup;
import io.spine.users.event.GroupCreated;
import io.spine.users.rejection.Rejections.GroupAlreadyExists;
import io.spine.users.rejection.Rejections.UnavalableForPreviouslyDeletedGroup;
import io.spine.users.server.UsersContextTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.spine.users.server.given.TestIdentifiers.groupId;
import static io.spine.users.server.group.given.GroupTestCommands.createGroup;
import static io.spine.users.server.group.given.GroupTestCommands.deleteGroup;

@DisplayName("A group should")
class GroupAccountTest extends UsersContextTest {

    private GroupId group;
    private BlackBoxContext context;

    @BeforeEach
    void generateId() {
        group = groupId();
        context = context();
    }

    @Nested
    @DisplayName("be created on a command")
    class Creation {

        private CreateGroup cmd;

        @BeforeEach
        void createGroupAccount() {
            cmd = createGroup(group);
            context.receivesCommand(cmd);
        }

        @Test
        @DisplayName("emitting event")
        void event() {
            assertEvent(
                    GroupCreated
                            .newBuilder()
                            .setGroup(group)
                            .setDisplayName(cmd.getDisplayName())
                            .setEmail(cmd.getEmail())
                            .setDescription(cmd.getDescription())
                            .vBuild()
            );
        }

        @Test
        @DisplayName("updating entity state")
        void entityState() {
            assertEntity(
                    group,
                    Group.newBuilder()
                         .setId(cmd.getGroup())
                         .setDisplayName(cmd.getDisplayName())
                         .setDescription(cmd.getDescription())
                         .setEmail(cmd.getEmail())
                         .vBuild()
            );
        }

        @Nested
        @DisplayName("rejecting if")
        class Rejecting {

            @Test
            @DisplayName("a group with the requested ID already exists")
            void ifDuplicate() {
                // Request creation of a group having the same ID (and other fields random).
                context.receivesCommand(createGroup(group));

                context.assertEvent(
                        GroupAlreadyExists
                                .newBuilder()
                                .setGroup(cmd.getGroup())
                                .vBuild()
                );
            }

            @Test
            @DisplayName("a group with such ID was previously deleted")
            void ifDeletedBefore() {
                context.receivesCommand(deleteGroup(group));

                context.receivesCommand(createGroup(group));

                context.assertEvent(
                        UnavalableForPreviouslyDeletedGroup
                                .newBuilder()
                                .setGroup(group)
                                .build()
                );
            }
        }
    }
}
