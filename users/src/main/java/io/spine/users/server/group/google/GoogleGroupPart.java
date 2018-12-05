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

package io.spine.users.server.group.google;

import io.spine.server.aggregate.AggregatePart;
import io.spine.server.aggregate.AggregateRoot;
import io.spine.server.aggregate.Apply;
import io.spine.users.GroupId;
import io.spine.users.google.group.event.GoogleGroupAliasesChanged;
import io.spine.users.google.group.event.GoogleGroupCreated;
import io.spine.users.group.google.GoogleGroup;
import io.spine.users.group.google.GoogleGroupVBuilder;
import io.spine.users.server.group.GroupRoot;

/**
 * A Google Group part.
 *
 * <p>This part handles external events happen to a Google Groups and stores all information
 * that is specific for a Google Groups. For example:
 * <ul>
 *     <li>an identifier of a group at Google;
 *     <li>the list of email aliases;
 *     <li>etc.
 * </ul>
 */
public class GoogleGroupPart
        extends AggregatePart<GroupId, GoogleGroup, GoogleGroupVBuilder, GroupRoot> {

    /**
     * @see AggregatePart#AggregatePart(AggregateRoot)
     */
    protected GoogleGroupPart(GroupRoot root) {
        super(root);
    }

    @Apply(allowImport = true)
    void on(GoogleGroupCreated event) {
        getBuilder().setId(event.getId())
                    .setGoogleId(event.getGoogleId())
                    .addAllAlias(event.getAliasList());
    }

    @Apply(allowImport = true)
    void on(GoogleGroupAliasesChanged event) {
        getBuilder().clearAlias()
                    .addAllAlias(event.getNewAliasList());
    }
}
