/*
 * Copyright 2020, TeamDev. All rights reserved.
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

package io.spine.roles.server;

import io.spine.roles.InheritedRoles;
import io.spine.roles.InheritedRoles.Builder;
import io.spine.roles.InheritedRoles.Item;
import io.spine.roles.RoleId;
import io.spine.users.GroupId;
import io.spine.validate.Validated;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Helper object for querying and updating {@link io.spine.roles.InheritedRoles InheritedRoles}.
 */
@SuppressWarnings("BadImport") /* We use inner types annotated with `@Nullable` in this class.
 If we do put as annotated inner type (as ErrorProne wants), for example:
            InheritedRoles.@Nullable Builder builder;
 we get (and this is funny) the error `PMDException: Error while parsing` followed by the name of
 this source. Since we cannot keep both static analyzers happy, let's keep it the in the more
 compact form via the import of the inner class. */
final class InheritedRolesHelper {

    private final InheritedRoles roles;
    private final @Nullable Builder builder;

    InheritedRolesHelper(InheritedRoles roles, @Nullable Builder builder) {
        this.roles = checkNotNull(roles);
        this.builder = builder;
    }

    boolean contains(RoleId role) {
        Optional<Item> item = itemWithRole(role);
        return item.isPresent();
    }

    private Optional<Item> itemWithRole(RoleId role) {
        return roles.getItemList()
                    .stream()
                    .filter(item -> role.equals(item.getRole()))
                    .findFirst();
    }

    void addRole(GroupId group, RoleId role) {
        checkNotNull(builder);
        builder.addItem(item(group, role));
    }

    private static @Validated Item item(GroupId group, RoleId role) {
        return Item.newBuilder()
                   .setGroup(group)
                   .setRole(role)
                   .vBuild();
    }

    void removeRole(GroupId group, RoleId role) {
        checkNotNull(builder);
        int index = builder.getItemList()
                           .indexOf(item(group, role));
        if (index != -1) {
            builder.removeItem(index);
        }
    }
}
