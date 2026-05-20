// port-lint: source src/lib.rs
package io.github.kotlinmania.gethostname

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class GethostnameTest {
    @Test
    fun gethostnameMatchesSystemHostname() {
        val output = systemHostnameCommand()
        if (output.success) {
            val hostname = output.stdout
            assertTrue(hostname.isNotEmpty(), "Failed to get hostname: hostname empty?")
            assertEquals(hostname.trimEnd().lowercase(), gethostname().lowercase())
        } else {
            fail("Failed to get hostname! ${output.stderr}")
        }
    }
}
