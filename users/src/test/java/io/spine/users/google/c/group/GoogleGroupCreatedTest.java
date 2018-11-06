/*
 * Copyright 2018, TeamDev. All rights reserved.
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

package io.spine.users.google.c.group;

import io.spine.users.c.group.CreateGroup;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.spine.users.google.c.group.GoogleGroupTestPms.emptyPm;
import static io.spine.users.google.given.GoogleGroupTestEvents.externalGoogleGroupCreated;
import static io.spine.users.google.given.GoogleGroupTestEvents.internalGoogleGroupCreated;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladyslav Lubenskyi
 */
@SuppressWarnings("InnerClassMayBeStatic") // JUnit doesn't support static classes.
@DisplayName("GoogleGroupPm should, when")
class GoogleGroupCreatedTest {

    @Nested
    @Disabled // Enable when Organization projection is ready.
    @DisplayName("internal GoogleGroupCreated")
    class InternalGroupCreated extends GoogleGroupLifecycleEventTest<GoogleGroupCreated> {

        InternalGroupCreated() {
            super(internalGoogleGroupCreated(GROUP_ID));
        }

        @Test
        @DisplayName("translate it to CreateGroup command")
        void testBeTranslated() {
            expectThat(emptyPm(GROUP_ID)).producesCommand(CreateGroup.class, command -> {
                assertEquals(GROUP_ID, command.getId());
                assertEquals(message().getDisplayName(), command.getDisplayName());
                assertEquals(message().getEmail(), command.getEmail());
                assertTrue(command.hasOrgEntity());
            });
        }
    }

    @Nested
    @Disabled // Enable when Organization projection is ready.
    @DisplayName("external GoogleGroupCreated")
    class ExternalGroupCreated extends GoogleGroupLifecycleEventTest<GoogleGroupCreated> {

        ExternalGroupCreated() {
            super(externalGoogleGroupCreated(GROUP_ID));
        }

        @Test
        @DisplayName("translate it to CreateGroup command")
        void testBeTranslated() {
            expectThat(emptyPm(GROUP_ID)).producesCommand(CreateGroup.class, command -> {
                assertEquals(GROUP_ID, command.getId());
                assertEquals(message().getDisplayName(), command.getDisplayName());
                assertEquals(message().getEmail(), command.getEmail());
                assertTrue(command.hasExternalDomain());
            });
        }
    }
}
