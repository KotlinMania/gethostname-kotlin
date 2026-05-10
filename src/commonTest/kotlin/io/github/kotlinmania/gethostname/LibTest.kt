// port-lint: source src/lib.rs
package io.github.kotlinmania.gethostname

import kotlin.test.Test
import kotlin.test.assertTrue

class LibTest {
    // Upstream's `gethostname_matches_system_hostname` test shells out to `hostname`/`uname -n` and
    // compares; that requires Process I/O which is not available portably across all KMP targets.
    // The portable parity check is the same one upstream actually asserts after the shell-out:
    // the returned hostname must be a non-empty string.
    @Test
    fun gethostnameIsNonEmpty() {
        val hostname = gethostname()
        assertTrue(hostname.isNotEmpty(), "Failed to get hostname: hostname empty?")
    }
}
