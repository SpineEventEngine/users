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

package io.spine.users.group.given;

import io.spine.net.EmailAddress;
import io.spine.net.EmailAddressVBuilder;
import io.spine.users.GroupId;
import io.spine.users.GroupIdVBuilder;
import io.spine.users.OrganizationOrUnit;
import io.spine.users.OrganizationOrUnitVBuilder;
import io.spine.users.RoleId;

import static io.spine.base.Identifier.newUuid;
import static io.spine.users.given.GivenId.orgUnitUuid;
import static io.spine.users.given.GivenId.organizationId;
import static io.spine.users.role.RoleIds.roleId;

/**
 * The environment for the {@link GroupPart} tests.
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

    public static EmailAddress groupEmail() {
        return EmailAddressVBuilder.newBuilder()
                                   .setValue("developers-list@gmail.com")
                                   .build();
    }

    public static OrganizationOrUnit groupOrgEntityOrganization() {
        return OrganizationOrUnitVBuilder.newBuilder()
                                         .setOrganization(organizationId("Space travel"))
                                         .build();
    }

    public static OrganizationOrUnit groupParentOrgUnit() {
        return OrganizationOrUnitVBuilder.newBuilder()
                                         .setOrgUnit(orgUnitUuid())
                                         .build();
    }

    public static RoleId groupRole() {
        return roleId(groupOrgEntityOrganization(), "administrator");
    }

    public static String groupDescription() {
        return "A relatively nice group";
    }

    static String newGroupDescription() {
        return "A very nice group";
    }

    static String newGroupName() {
        return "Developers-renamed";
    }

    static EmailAddress newGroupEmail() {
        return EmailAddressVBuilder.newBuilder()
                                   .setValue("developers-renamed-list@gmail.com")
                                   .build();
    }
}
