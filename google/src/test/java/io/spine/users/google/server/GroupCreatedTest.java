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

import com.google.common.truth.extensions.proto.ProtoSubject;
import io.spine.testing.server.blackbox.BlackBoxContext;
import io.spine.users.GroupId;
import io.spine.users.google.event.GoogleGroupCreated;
import io.spine.users.group.command.CreateGroup;
import io.spine.users.server.UsersContextTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.spine.users.google.server.given.GoogleGroupTestEnv.newGroupId;
import static io.spine.users.google.server.given.GoogleGroupTestEvents.externalGoogleGroupCreated;
import static io.spine.users.google.server.given.GoogleGroupTestEvents.internalGoogleGroupCreated;

@DisplayName("`GoogleGroupPm` should, when")
@Disabled("Until new API is introduced")
class GroupCreatedTest extends UsersContextTest {

    private static final GroupId GROUP_ID = newGroupId();

    @Nested
    @Disabled // Enable when Organization projection is ready.
    @DisplayName("internal `GoogleGroupCreated`")
    class InternalGroupCreated {

        @Test
        @DisplayName("translate it to `CreateGroup` command")
        void testBeTranslated() {
            GoogleGroupCreated event = internalGoogleGroupCreated(GROUP_ID);
            BlackBoxContext afterEvent = context().receivesEvent(event);

            ProtoSubject assertCommand =
                    afterEvent.assertCommands()
                              .withType(CreateGroup.class)
                              .message(0);

            CreateGroup expectedCmd = CreateGroup
                    .newBuilder()
                    .setGroup(GROUP_ID)
                    .setDisplayName(event.getDisplayName())
                    .setEmail(event.getEmail())
                    .build();
            assertCommand
                     .comparingExpectedFieldsOnly()
                     .isEqualTo(expectedCmd);
            //TODO:2019-08-17:alex.tymchenko: check that the command has `OrgEntity` set.

//            expectThat(GoogleGroupTestPms.emptyPm(GROUP_ID)).producesCommand(CreateGroup.class, command -> {
//                assertEquals(GROUP_ID, command.getId());
//                assertEquals(message().getDisplayName(), command.getDisplayName());
//                assertEquals(message().getEmail(), command.getEmail());
//                assertTrue(command.hasOrgEntity());
//            });
        }
    }

    @Nested
    @Disabled // Enable when Organization projection is ready.
    @DisplayName("external `GoogleGroupCreated`")
    class ExternalGroupCreated {

        @Test
        @DisplayName("translate it to `CreateGroup` command")
        void testBeTranslated() {

            GoogleGroupCreated event = externalGoogleGroupCreated(GROUP_ID);
            CreateGroup expectedCmd = CreateGroup
                    .newBuilder()
                    .setGroup(GROUP_ID)
                    .setDisplayName(event.getDisplayName())
                    .setEmail(event.getEmail())
                    .build();
            context().receivesEvent(event)
                     .assertCommands()
                     .withType(CreateGroup.class)
                     .message(0)
                     .comparingExpectedFieldsOnly()
                     .isEqualTo(expectedCmd);
        }
    }
}
