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

package io.spine.users.server.given;

import io.spine.users.GroupId;
import io.spine.users.OrgUnitId;
import io.spine.users.OrganizationId;

import static io.spine.base.Identifier.newUuid;

/**
 * Factory methods for creating test values of identifiers in Users bounded context.
 */
public class GivenId {

    /** Prevents instantiation of this utility class. */
    private GivenId() {
    }

    public static OrganizationId organizationUuid() {
        return organizationId("org-" + newUuid());
    }

    public static OrgUnitId orgUnitUuid() {
        return OrgUnitId
                .vBuilder()
                .setValue(newUuid())
                .build();
    }

    public static OrganizationId organizationId(String value) {
        return OrganizationId
                .vBuilder()
                .setValue(value)
                .build();
    }

    public static GroupId groupUuid() {
        return groupId("group-" + newUuid());
    }

    private static GroupId groupId(String value) {
        return GroupId
                .vBuilder()
                .setValue(value)
                .build();
    }
}
