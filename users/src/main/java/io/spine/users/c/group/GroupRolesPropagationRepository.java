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

package io.spine.users.c.group;

import com.google.common.collect.ImmutableSet;
import io.spine.server.procman.ProcessManagerRepository;
import io.spine.users.GroupId;
import io.spine.users.c.user.UserJoinedGroup;
import io.spine.users.c.user.UserLeftGroup;

/**
 * The repository for {@link GroupRolesPropagationPm}.
 */
public class GroupRolesPropagationRepository
        extends ProcessManagerRepository<GroupId, GroupRolesPropagationPm, GroupRolesPropagation> {

    public GroupRolesPropagationRepository() {
        super();
        getEventRouting().route(UserJoinedGroup.class,
                                (event, context) -> ImmutableSet.of(event.getGroupId()))
                         .route(UserLeftGroup.class,
                                (event, context) -> ImmutableSet.of(event.getGroupId()));
    }
}