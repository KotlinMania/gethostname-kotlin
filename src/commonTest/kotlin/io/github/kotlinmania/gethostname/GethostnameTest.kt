// port-lint: source src/lib.rs
package io.github.kotlinmania.gethostname

import kotlin.test.Test
import kotlin.test.assertTrue

class LibTest {
    @Test
    fun gethostnameIsNonEmpty() {
        val hostname = gethostname()
        assertTrue(hostname.isNotEmpty(), "Failed to get hostname: hostname empty?")
    }
}
