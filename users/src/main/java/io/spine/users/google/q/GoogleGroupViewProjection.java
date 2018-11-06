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

package io.spine.users.google.q;

import io.spine.core.Subscribe;
import io.spine.server.projection.Projection;
import io.spine.users.GroupId;
import io.spine.users.google.c.group.GoogleGroupAliasesChanged;
import io.spine.users.google.c.group.GoogleGroupCreated;

/**
 * A view of a Google Group.
 *
 * <p>This projection subscribes on events happen to a Google Group and stores all information
 * that is specific for a Google Group. For example:
 * <ul>
 *     <li>an identifier of a group at Google;
 *     <li>the list of email aliases;
 *     <li>etc.
 * </ul>
 */
public class GoogleGroupViewProjection extends Projection<GroupId, GoogleGroupView, GoogleGroupViewVBuilder> {

    protected GoogleGroupViewProjection(GroupId id) {
        super(id);
    }

    @Subscribe
    public void on(GoogleGroupCreated event) {
        getBuilder().setId(event.getId())
                    .setGoogleId(event.getGoogleId())
                    .addAllAliases(event.getAliasesList());
    }

    @Subscribe
    public void on(GoogleGroupAliasesChanged event) {
        getBuilder().clearAliases()
                    .addAllAliases(event.getNewAliasesList());
    }
}
