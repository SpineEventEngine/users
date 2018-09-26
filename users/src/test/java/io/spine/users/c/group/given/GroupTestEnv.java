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

package io.spine.users.c.group.given;

import io.spine.net.EmailAddress;
import io.spine.net.EmailAddressVBuilder;
import io.spine.users.GroupId;
import io.spine.users.GroupIdVBuilder;
import io.spine.users.OrgUnitId;
import io.spine.users.OrgUnitIdVBuilder;
import io.spine.users.OrganizationId;
import io.spine.users.OrganizationIdVBuilder;
import io.spine.users.OrganizationOrUnit;
import io.spine.users.OrganizationOrUnitVBuilder;
import io.spine.users.RoleId;
import io.spine.users.RoleIdVBuilder;
import io.spine.users.c.group.GroupPart;

import static io.spine.base.Identifier.newUuid;

/**
 * The environment for the {@link GroupPart} tests.
 *
 * @author Vladyslav Lubenskyi
 */
public class GroupTestEnv {

    private static final GroupId NESTED_GROUP_ID = createGroupId();

    /**
     * Prevents instantiation.
     */
    private GroupTestEnv() {
    }

    public static GroupId createGroupId() {
        return GroupIdVBuilder.newBuilder()
                              .setValue(newUuid())
                              .build();
    }

    public static GroupId upperGroupId() {
        return NESTED_GROUP_ID;
    }

    public static String groupName() {
        return "Developers";
    }

    public static String newGroupName() {
        return "Developers-renamed";
    }

    public static EmailAddress groupEmail() {
        return EmailAddressVBuilder.newBuilder()
                                   .setValue("developers-list@gmail.com")
                                   .build();
    }

    public static EmailAddress newGroupEmail() {
        return EmailAddressVBuilder.newBuilder()
                                   .setValue("developers-renamed-list@gmail.com")
                                   .build();
    }

    public static OrganizationOrUnit groupOrgEntityOrganization() {
        return OrganizationOrUnitVBuilder.newBuilder()
                                   .setOrganization(organization())
                                   .build();
    }

    public static OrganizationOrUnit groupParentOrgUnit() {
        return OrganizationOrUnitVBuilder.newBuilder()
                                   .setOrgUnit(orgUnit())
                                   .build();
    }

    public static RoleId groupRole() {
        return RoleIdVBuilder.newBuilder()
                             .setValue("administrator")
                             .build();
    }

    private static OrgUnitId orgUnit() {
        return OrgUnitIdVBuilder.newBuilder()
                                .setValue(newUuid())
                                .build();
    }

    private static OrganizationId organization() {
        return OrganizationIdVBuilder.newBuilder()
                                     .setValue(newUuid())
                                     .build();
    }
}
