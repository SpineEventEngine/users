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

import com.google.protobuf.Any;
import com.google.protobuf.Int32Value;
import io.spine.core.UserId;
import io.spine.core.UserIdVBuilder;
import io.spine.net.EmailAddress;
import io.spine.net.EmailAddressVBuilder;
import io.spine.users.GroupId;
import io.spine.users.GroupIdVBuilder;
import io.spine.users.OrgUnitId;
import io.spine.users.OrgUnitIdVBuilder;
import io.spine.users.OrganizationId;
import io.spine.users.OrganizationIdVBuilder;
import io.spine.users.OrganizationalEntity;
import io.spine.users.OrganizationalEntityVBuilder;
import io.spine.users.RoleId;
import io.spine.users.RoleIdVBuilder;
import io.spine.users.c.group.GroupAggregate;

import static io.spine.base.Identifier.newUuid;
import static io.spine.protobuf.AnyPacker.pack;

/**
 * The environment for the {@link GroupAggregate} tests.
 *
 * @author Vladyslav Lubenskyi
 */
public class GroupTestEnv {

    private static final GroupId NESTED_GROUP_ID = createGroupId();
    private static final UserId GROUP_OWNER = UserIdVBuilder.newBuilder()
                                                            .setValue(newUuid())
                                                            .build();

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

    public static UserId groupOwner() {
        return GROUP_OWNER;
    }

    public static UserId newGroupOwner() {
        return UserIdVBuilder.newBuilder()
                             .setValue(newUuid())
                             .build();
    }

    public static OrganizationalEntity groupOrgEntityOrganization() {
        return OrganizationalEntityVBuilder.newBuilder()
                                   .setOrganization(organization())
                                   .build();
    }

    public static OrganizationalEntity groupParentOrgUnit() {
        return OrganizationalEntityVBuilder.newBuilder()
                                   .setOrgUnit(orgUnit())
                                   .build();
    }

    public static RoleId groupRole() {
        return RoleIdVBuilder.newBuilder()
                             .setValue("administrator")
                             .build();
    }

    public static String groupAttributeName() {
        return "grouo-attribute-1";
    }

    public static Any groupAttributeValue() {
        return pack(Int32Value.of(42));
    }

    static String newGroupAttributeName() {
        return "grouo-attribute-1";
    }

    static Any newGroupAttributeValue() {
        return pack(Int32Value.of(31415));
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
