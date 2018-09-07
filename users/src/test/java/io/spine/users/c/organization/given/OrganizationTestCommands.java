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

package io.spine.users.c.organization.given;

import io.spine.users.OrganizationId;
import io.spine.users.c.organization.AddOrganizationAttribute;
import io.spine.users.c.organization.AddOrganizationAttributeVBuilder;
import io.spine.users.c.organization.ChangeOrganizationOwner;
import io.spine.users.c.organization.ChangeOrganizationOwnerVBuilder;
import io.spine.users.c.organization.CreateOrganization;
import io.spine.users.c.organization.CreateOrganizationVBuilder;
import io.spine.users.c.organization.DeleteOrganization;
import io.spine.users.c.organization.DeleteOrganizationVBuilder;
import io.spine.users.c.organization.OrganizationAggregate;
import io.spine.users.c.organization.RemoveOrganizationAttribute;
import io.spine.users.c.organization.RemoveOrganizationAttributeVBuilder;
import io.spine.users.c.organization.UpdateOrganizationAttribute;
import io.spine.users.c.organization.UpdateOrganizationAttributeVBuilder;

import static io.spine.users.c.organization.given.OrganizationTestEnv.newOrgAttributeName;
import static io.spine.users.c.organization.given.OrganizationTestEnv.newOrgAttributeValue;
import static io.spine.users.c.organization.given.OrganizationTestEnv.orgAttributeName;
import static io.spine.users.c.organization.given.OrganizationTestEnv.orgAttributeValue;
import static io.spine.users.c.organization.given.OrganizationTestEnv.orgDomain;
import static io.spine.users.c.organization.given.OrganizationTestEnv.orgName;
import static io.spine.users.c.organization.given.OrganizationTestEnv.orgNewOwner;
import static io.spine.users.c.organization.given.OrganizationTestEnv.orgOwner;
import static io.spine.users.c.organization.given.OrganizationTestEnv.orgTenant;

/**
 * Test commands for {@link OrganizationAggregate}.
 *
 * @author Vladyslav Lubenskyi
 */
public class OrganizationTestCommands {

    /**
     * Prevents instantiation.
     */
    private OrganizationTestCommands() {
    }

    public static CreateOrganization createOrganization(OrganizationId id) {
        return CreateOrganizationVBuilder.newBuilder()
                                         .setId(id)
                                         .setDisplayName(orgName())
                                         .setDomain(orgDomain())
                                         .setTenant(orgTenant())
                                         .setOwner(orgOwner())
                                         .putAttributes(orgAttributeName(), orgAttributeValue())
                                         .build();

    }

    public static DeleteOrganization deleteOrganization(OrganizationId id) {
        return DeleteOrganizationVBuilder.newBuilder()
                                         .setId(id)
                                         .build();
    }

    public static ChangeOrganizationOwner changeOrganizationOwner(OrganizationId id) {
        return ChangeOrganizationOwnerVBuilder.newBuilder()
                                              .setId(id)
                                              .setNewOwner(orgNewOwner())
                                              .build();
    }

    public static AddOrganizationAttribute addOrganizationAttribute(OrganizationId id) {
        return AddOrganizationAttributeVBuilder.newBuilder()
                                               .setId(id)
                                               .setName(newOrgAttributeName())
                                               .setValue(newOrgAttributeValue())
                                               .build();
    }

    public static RemoveOrganizationAttribute removeOrganizationAttribute(OrganizationId id) {
        return RemoveOrganizationAttributeVBuilder.newBuilder()
                                                  .setId(id)
                                                  .setName(orgAttributeName())
                                                  .build();
    }

    public static UpdateOrganizationAttribute updateOrganizationAttribute(OrganizationId id) {
        return UpdateOrganizationAttributeVBuilder.newBuilder()
                                                  .setId(id)
                                                  .setName(orgAttributeName())
                                                  .setNewValue(newOrgAttributeValue())
                                                  .build();
    }

}
