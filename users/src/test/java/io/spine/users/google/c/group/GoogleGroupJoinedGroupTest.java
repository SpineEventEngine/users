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

import io.spine.users.c.group.JoinParentGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.google.c.group.GoogleGroupTestPms.emptyPm;
import static io.spine.users.google.c.group.given.GoogleGroupTestEvents.googleGroupJoinedParentGroup;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("GoogleGroupPm should, when GoogleGroupJoinedParentGroup")
class GoogleGroupJoinedGroupTest extends GoogleGroupPmEventTest<GoogleGroupJoinedParentGroup> {

    GoogleGroupJoinedGroupTest() {
        super(googleGroupJoinedParentGroup(GROUP_ID));
    }

    @Test
    @DisplayName("translate it to JoinParentGroup command")
    void testBeTranslated() {
        expectThat(emptyPm(GROUP_ID)).producesCommand(JoinParentGroup.class, command -> {
            assertEquals(GROUP_ID, command.getId());
            assertEquals(message().getNewParentId(), command.getParentGroupId());

        });
    }
}
