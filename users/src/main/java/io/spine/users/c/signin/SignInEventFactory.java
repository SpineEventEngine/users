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

package io.spine.users.c.signin;

import io.spine.core.ActorContext;
import io.spine.core.CommandContext;
import io.spine.core.UserId;
import io.spine.users.UserAuthIdentity;
import io.spine.users.c.EntityEventFactory;

/**
 * An event factory for the {@link SignIn} process manager.
 *
 * @author Vladyslav Lubenskyi
 */
class SignInEventFactory extends EntityEventFactory {

    private SignInEventFactory(ActorContext actorContext) {
        super(actorContext);
    }

    /**
     * Retrieves an instance of {@link SignInEventFactory}.
     *
     * @param context the {@link CommandContext} of the command to handle
     * @return new instance of {@link SignInEventFactory}
     */
    static SignInEventFactory instance(CommandContext context) {
        ActorContext actorContext = context.getActorContext();
        return new SignInEventFactory(actorContext);
    }

    SignInSuccessful completeSignIn(UserId id, UserAuthIdentity identity) {
        return SignInSuccessfulVBuilder.newBuilder()
                                       .setId(id)
                                       .setIdentity(identity)
                                       .build();
    }

    SignInFailed failSignIn(UserId id, UserAuthIdentity identity,
                            SignInFailureReason reason) {
        return SignInFailedVBuilder.newBuilder()
                                   .setId(id)
                                   .setIdentity(identity)
                                   .setFailureReason(reason)
                                   .build();
    }

    SignOutCompleted signOut(UserId id) {
        return SignOutCompletedVBuilder.newBuilder()
                                       .setId(id)
                                       .build();
    }
}
