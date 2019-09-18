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

package io.spine.users.server.signin.given;

import io.spine.users.PersonProfile;
import io.spine.users.server.Directory;
import io.spine.users.user.Identity;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A test environment directory which can be configured with identities, allowed log-ins,
 * and profiles.
 */
public final class StubDirectory implements Directory {

    private final Set<Identity> identities = new HashSet<>();
    private final Set<Identity> allowedSignIns = new HashSet<>();
    private final Map<Identity, PersonProfile> profiles = new HashMap<>();

    public StubDirectory addIdentity(Identity identity) {
        checkNotNull(identity);
        identities.add(identity);
        return this;
    }

    public StubDirectory removeIdentity(Identity identity) {
        checkNotNull(identity);
        identities.remove(identity);
        return this;
    }

    public StubDirectory allowSignInFor(Identity identity) {
        checkNotNull(identity);
        allowedSignIns.add(identity);
        return this;
    }

    public StubDirectory prohibitSignInFor(Identity identity) {
        checkNotNull(identity);
        allowedSignIns.remove(identity);
        return this;
    }

    public StubDirectory addProfile(Identity identity, PersonProfile profile) {
        checkNotNull(identity);
        checkNotNull(profile);
        profiles.put(identity, profile);
        return this;
    }

    @Override
    public boolean hasIdentity(Identity identity) {
        checkNotNull(identity);
        boolean result = identities.contains(identity);
        return result;
    }

    @Override
    public boolean isSignInAllowed(Identity identity) {
        checkNotNull(identity);
        boolean result = allowedSignIns.contains(identity);
        return result;
    }

    @Override
    public @Nullable PersonProfile fetchProfile(Identity identity) {
        checkNotNull(identity);
        @Nullable PersonProfile result = profiles.get(identity);
        return result;
    }
}
