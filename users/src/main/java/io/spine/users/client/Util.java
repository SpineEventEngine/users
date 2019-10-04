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

package io.spine.users.client;

import io.spine.client.ActorRequestFactory;
import io.spine.core.TenantId;
import io.spine.core.UserId;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Utility class for client-side operations.
 *
 * <p>Presumably these routines will be migrated to Spine Core once the reusable client-side
 * functionality is matured inside this library.
 */
public final class Util {

    /** Prevents instantiation of this utility class. */
    private Util() {
    }

    /**
     * Creates request factory for sending system-requests on behalf of the passed class.
     *
     * @param cls
     *         the class full name of which will be used in the value of {@code UserId} when
     *         sending requests
     * @param tenant
     *         the ID of the tenant in a multi-tenant application, {@code null} for single-tenant
     * @return new instance of the factory
     */
    public static
    ActorRequestFactory systemRequestFactoryFor(Class<?> cls, @Nullable TenantId tenant) {
        UserId thisClassName = UserId
                .newBuilder()
                .setValue(cls.getName())
                .build();
        return ActorRequestFactory
                .newBuilder()
                .setTenantId(tenant)
                .setActor(thisClassName)
                .build();
    }
}
