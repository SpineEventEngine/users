/*
 * Copyright (c) 2000-2018 TeamDev. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package io.spine.users.signin;

import io.spine.users.signin.command.SignUserOut;
import io.spine.users.signin.event.SignOutCompleted;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.users.signin.TestProcManFactory.createEmptyProcMan;
import static io.spine.users.signin.given.SignInTestCommands.signOutCommand;
import static io.spine.users.signin.given.SignInTestEnv.userId;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vladyslav Lubenskyi
 */
@DisplayName("SignInPm should, when SignUserOut")
class SignUserOutCommandTest extends SignInPmCommandTest<SignUserOut> {

    SignUserOutCommandTest() {
        super(userId(), command());
    }

    @Test
    @DisplayName("generate SignOutCompleted event")
    void failProcess() {
        SignInPm emptyProcMan = createEmptyProcMan(entityId());

        expectThat(emptyProcMan).producesEvent(SignOutCompleted.class, command -> {
            assertEquals(message().getId(), command.getId());
        });
    }

    private static SignUserOut command() {
        return signOutCommand(userId());
    }
}
