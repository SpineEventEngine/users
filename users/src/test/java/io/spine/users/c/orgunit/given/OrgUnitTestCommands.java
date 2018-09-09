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

package io.spine.users.c.orgunit.given;

import io.spine.users.OrgUnitId;
import io.spine.users.c.orgunit.AddOrgUnitAttribute;
import io.spine.users.c.orgunit.AddOrgUnitAttributeVBuilder;
import io.spine.users.c.orgunit.ChangeOrgUnitOwner;
import io.spine.users.c.orgunit.ChangeOrgUnitOwnerVBuilder;
import io.spine.users.c.orgunit.CreateOrgUnit;
import io.spine.users.c.orgunit.CreateOrgUnitVBuilder;
import io.spine.users.c.orgunit.DeleteOrgUnit;
import io.spine.users.c.orgunit.DeleteOrgUnitVBuilder;
import io.spine.users.c.orgunit.MoveOrgUnit;
import io.spine.users.c.orgunit.MoveOrgUnitVBuilder;
import io.spine.users.c.orgunit.OrgUnitAggregate;
import io.spine.users.c.orgunit.RemoveOrgUnitAttribute;
import io.spine.users.c.orgunit.RemoveOrgUnitAttributeVBuilder;
import io.spine.users.c.orgunit.UpdateOrgUnitAttribute;
import io.spine.users.c.orgunit.UpdateOrgUnitAttributeVBuilder;

import static io.spine.users.c.orgunit.given.OrgUnitTestEnv.newOrgUnitAttributeName;
import static io.spine.users.c.orgunit.given.OrgUnitTestEnv.newOrgUnitAttributeValue;
import static io.spine.users.c.orgunit.given.OrgUnitTestEnv.newOrgUnitParentEntity;
import static io.spine.users.c.orgunit.given.OrgUnitTestEnv.orgUnitAttributeName;
import static io.spine.users.c.orgunit.given.OrgUnitTestEnv.orgUnitAttributeValue;
import static io.spine.users.c.orgunit.given.OrgUnitTestEnv.orgUnitDomain;
import static io.spine.users.c.orgunit.given.OrgUnitTestEnv.orgUnitName;
import static io.spine.users.c.orgunit.given.OrgUnitTestEnv.orgUnitNewOwner;
import static io.spine.users.c.orgunit.given.OrgUnitTestEnv.orgUnitOwner;
import static io.spine.users.c.orgunit.given.OrgUnitTestEnv.orgUnitParentEntity;

/**
 * Test commands for {@link OrgUnitAggregate}.
 *
 * @author Vladyslav Lubenskyi
 */
public final class OrgUnitTestCommands {

    /**
     * Prevents instantiation.
     */
    private OrgUnitTestCommands() {
    }

    public static CreateOrgUnit createOrgUnit(OrgUnitId id) {
        return CreateOrgUnitVBuilder.newBuilder()
                                    .setId(id)
                                    .setDisplayName(orgUnitName())
                                    .setDomain(orgUnitDomain())
                                    .setParentEntity(orgUnitParentEntity())
                                    .setOwner(orgUnitOwner())
                                    .putAttributes(orgUnitAttributeName(), orgUnitAttributeValue())
                                    .build();

    }

    public static DeleteOrgUnit deleteOrgUnit(OrgUnitId id) {
        return DeleteOrgUnitVBuilder.newBuilder()
                                    .setId(id)
                                    .build();
    }

    public static ChangeOrgUnitOwner changeOrgUnitOwner(OrgUnitId id) {
        return ChangeOrgUnitOwnerVBuilder.newBuilder()
                                         .setId(id)
                                         .setNewOwner(orgUnitNewOwner())
                                         .build();
    }

    public static AddOrgUnitAttribute addOrgUnitAttribute(OrgUnitId id) {
        return AddOrgUnitAttributeVBuilder.newBuilder()
                                          .setId(id)
                                          .setName(newOrgUnitAttributeName())
                                          .setValue(newOrgUnitAttributeValue())
                                          .build();
    }

    public static RemoveOrgUnitAttribute removeOrgUnitAttribute(OrgUnitId id) {
        return RemoveOrgUnitAttributeVBuilder.newBuilder()
                                             .setId(id)
                                             .setName(orgUnitAttributeName())
                                             .build();
    }

    public static UpdateOrgUnitAttribute updateOrgUnitAttribute(OrgUnitId id) {
        return UpdateOrgUnitAttributeVBuilder.newBuilder()
                                             .setId(id)
                                             .setName(orgUnitAttributeName())
                                             .setNewValue(newOrgUnitAttributeValue())
                                             .build();
    }

    public static MoveOrgUnit moveOrgUnit(OrgUnitId id) {
        return MoveOrgUnitVBuilder.newBuilder()
                                  .setId(id)
                                  .setNewParentEntity(newOrgUnitParentEntity())
                                  .build();
    }

}
