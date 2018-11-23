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

package io.spine.users.q.group.given;

import io.spine.core.UserId;
import io.spine.net.EmailAddress;
import io.spine.net.EmailAddressVBuilder;
import io.spine.net.InternetDomain;
import io.spine.net.InternetDomainVBuilder;
import io.spine.testing.core.given.GivenUserId;
import io.spine.users.GroupId;
import io.spine.users.GroupIdVBuilder;
import io.spine.users.OrganizationId;
import io.spine.users.OrganizationIdVBuilder;
import io.spine.users.OrganizationOrUnit;
import io.spine.users.OrganizationOrUnitVBuilder;
import io.spine.users.RoleId;
import io.spine.users.RoleIdVBuilder;
import io.spine.users.RoleName;
import io.spine.users.RoleNameVBuilder;
import io.spine.users.RoleNamesVBuilder;
import io.spine.users.c.group.GroupCreated;
import io.spine.users.c.group.RoleNamesEnrichment;
import io.spine.users.c.group.RoleNamesEnrichmentVBuilder;
import io.spine.users.c.user.RoleInGroup;

import java.util.List;
import java.util.stream.Collectors;

import static io.spine.base.Identifier.newUuid;

/**
 * Test environment for {@link io.spine.users.q.group.GroupViewTest}.
 */
public class GroupViewTestEnv {

    private static final GroupId CHILD_GROUP_ID = newGroupId();
    private static final GroupId GROUP_ID = newGroupId();
    private static final RoleId ROLE_ID = roleUuid();

    /**
     * Prevents instantiation.
     */
    private GroupViewTestEnv() {
    }

    public static GroupId newGroupId() {
        return GroupIdVBuilder.newBuilder()
                              .setValue(newUuid())
                              .build();
    }

    public static EmailAddress email() {
        return EmailAddressVBuilder.newBuilder()
                                   .setValue("projection@spine.io")
                                   .build();
    }

    public static GroupId childGroup() {
        return CHILD_GROUP_ID;
    }

    public static GroupId groupId() {
        return GROUP_ID;
    }

    public static String groupDisplayName() {
        return "Developers Developers Developers";
    }

    public static RoleId role() {
        return ROLE_ID;
    }

    public static RoleInGroup roleInGroup() {
        return RoleInGroup.MEMBER;
    }

    public static UserId member() {
        return GivenUserId.of("group member");
    }

    public static OrganizationOrUnit orgEntity() {
        return OrganizationOrUnitVBuilder.newBuilder()
                                         .setOrganization(organizationId())
                                         .build();
    }

    public static RoleName roleName(RoleId roleId) {
        return RoleNameVBuilder.newBuilder()
                               .setId(roleId)
                               .build();
    }

    public static List<RoleName> expectedRoles(GroupCreated message) {
        return message.getRoles()
                      .getIdList()
                      .stream()
                      .map(GroupViewTestEnv::roleName)
                      .collect(Collectors.toList());
    }

    public static RoleNamesEnrichment roleNamesEnrichment(GroupCreated message) {
        RoleNamesEnrichment enrichmentMessage = RoleNamesEnrichmentVBuilder
                .newBuilder()
                .setRoles(RoleNamesVBuilder.newBuilder()
                                           .addAllName(expectedRoles(message))
                                           .build())
                .build();
        return enrichmentMessage;
    }

    static InternetDomain externalDomain() {
        return InternetDomainVBuilder.newBuilder()
                                     .setValue("google.com")
                                     .build();
    }

    private static OrganizationId organizationId() {
        return OrganizationIdVBuilder.newBuilder()
                                     .setValue(newUuid())
                                     .build();
    }

    private static RoleId roleUuid() {
        return RoleIdVBuilder.newBuilder()
                             .setValue(newUuid())
                             .build();
    }
}
