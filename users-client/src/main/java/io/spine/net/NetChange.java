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

package io.spine.net;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utility class for changes of network-related values.
 */
public final class NetChange {

    /** Prevents instantiation of this utility class. */
    private NetChange() {
    }

    /**
     * Creates a change object for an email address.
     */
    public static EmailAddressChange of(EmailAddress prevValue, EmailAddress newValue) {
        checkNotNull(prevValue);
        checkNotNull(newValue);
        checkNotEqual(prevValue, newValue);
        return EmailAddressChange
                .newBuilder()
                .setPreviousValue(prevValue)
                .setNewValue(newValue)
                .vBuild();
    }

    //TODO:2020-12-10:alexander.yevsyukov: Expose `ChangePreconditions` from `common-types` library
    // and use it instead of this method.
    private static <T> void checkNotEqual(T previousValue, T newValue) {
        checkNotNull(previousValue);
        checkNotNull(newValue);
        checkArgument(!newValue.equals(previousValue),
                      "`newValue` cannot be equal to `previousValue`.");
    }
}
