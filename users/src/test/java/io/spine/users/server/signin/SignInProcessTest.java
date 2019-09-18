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

package io.spine.users.server.signin;

import io.spine.server.BoundedContextBuilder;
import io.spine.users.server.Directory;
import io.spine.users.server.DirectoryFactory;
import io.spine.users.server.UsersContext;
import io.spine.users.server.UsersContextTest;
import io.spine.users.server.signin.given.StubDirectoryFactory;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * An abstract base for the tests of {@link SignInProcess}.
 */
public abstract class SignInProcessTest extends UsersContextTest {

    private @Nullable Directory directory;

    @Override
    protected BoundedContextBuilder contextBuilder() {
        directory = createDirectory();
        @Nullable DirectoryFactory factory = directory != null
                ? new StubDirectoryFactory(directory)
                : null;
        return UsersContext.newBuilder(factory);
    }

    /**
     * Override this method in derived tests to create directories suited for the tests.
     */
    protected @Nullable Directory createDirectory() {
        return null;
    }

    protected final @Nullable Directory directory() {
        return directory;
    }
}
