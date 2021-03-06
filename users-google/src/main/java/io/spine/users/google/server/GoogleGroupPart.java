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

package io.spine.users.google.server;

import io.spine.server.aggregate.AggregatePart;
import io.spine.server.aggregate.Apply;
import io.spine.users.GroupId;
import io.spine.users.google.event.GoogleGroupAliasesChanged;
import io.spine.users.google.event.GoogleGroupCreated;
import io.spine.users.google.GoogleGroup;
import io.spine.users.server.db.GroupRoot;

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
        extends AggregatePart<GroupId, GoogleGroup, GoogleGroup.Builder, GroupRoot> {

    protected GoogleGroupPart(GroupRoot root) {
        super(root);
    }

    @Apply(allowImport = true)
    void on(GoogleGroupCreated event) {
        builder().setId(event.getId())
                 .setGoogleId(event.getGoogleId())
                 .addAllAlias(event.getAliasList());
    }

    @Apply(allowImport = true)
    void on(GoogleGroupAliasesChanged event) {
        builder().clearAlias()
                 .addAllAlias(event.getNewAliasList());
    }
}
