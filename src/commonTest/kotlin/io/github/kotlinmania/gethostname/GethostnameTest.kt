// port-lint: source src/lib.rs
package io.github.kotlinmania.gethostname

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class GethostnameTest {
    @Test
    fun gethostnameMatchesSystemHostname() {
        // Platforms with no hostname facility at all (WASI preview1 has no
        // uname/gethostname syscall and no subprocess) report the gap by
        // throwing; there is nothing to compare against, so skip honestly.
        val hostnameValue =
            try {
                gethostname()
            } catch (e: UnsupportedOperationException) {
                return
            }
        val output = systemHostnameCommand()
        if (output.success) {
            val hostname = output.stdout
            assertTrue(hostname.isNotEmpty(), "Failed to get hostname: hostname empty?")
            assertEquals(hostname.trimEnd().lowercase(), hostnameValue.lowercase())
        } else {
            fail("Failed to get hostname! ${output.stderr}")
        }
    }
}
