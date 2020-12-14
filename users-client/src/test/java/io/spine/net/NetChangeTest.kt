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
package io.spine.net

import com.google.common.testing.NullPointerTester
import io.spine.testing.UtilityClassTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@DisplayName("`NetChange` utility class should")
internal class NetChangeTest : UtilityClassTest<NetChange>(NetChange::class.java) {
    
    override fun configure(tester: NullPointerTester) {
        super.configure(tester)
        tester.setDefault(EmailAddress::class.java, EmailAddress.getDefaultInstance())
              .setDefault(Url::class.java, Url.getDefaultInstance())
    }

    @Nested
    @DisplayName("not allow same")
    inner class NotSame {

        @Test
        fun `email address`() {
            val address = EmailAddresses.valueOf("one@example.com")
            assertRejects { NetChange.of(address, address) }
        }

        @Test
        fun `URL instances`() {
            val url = Urls.create("example.com")
            assertRejects { NetChange.of(url, url) }
        }

        private fun assertRejects(executable: () -> Unit) {
            assertThrows<IllegalArgumentException>(executable)
        }
    }
}
