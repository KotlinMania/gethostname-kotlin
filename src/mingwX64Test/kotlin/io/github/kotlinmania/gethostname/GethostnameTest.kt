// port-lint: source src/lib.rs (platform test glue, Windows command comparison)
package io.github.kotlinmania.gethostname

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.cinterop.toKString
import platform.posix._pclose
import platform.posix._popen
import platform.posix.fgets
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

private data class CommandOutput(
    val stdout: String,
    val status: Int,
)

@OptIn(ExperimentalForeignApi::class)
private fun readCommand(command: String): CommandOutput {
    val pipe = _popen(command, "r") ?: fail("failed to get hostname")
    val output = StringBuilder()
    val buffer = ByteArray(512)

    while (true) {
        val line = fgets(buffer.refTo(0), buffer.size, pipe) ?: break
        output.append(line.toKString())
    }

    return CommandOutput(output.toString(), _pclose(pipe))
}

class MingwLibTest {
    @Test
    fun gethostnameMatchesSystemHostname() {
        val output = readCommand("hostname 2>&1")
        if (output.status == 0) {
            val hostname = output.stdout
            assertTrue(hostname.isNotEmpty(), "Failed to get hostname: hostname empty?")
            assertEquals(hostname.trimEnd().lowercase(), gethostname().lowercase())
        } else {
            fail("Failed to get hostname! ${output.stdout}")
        }
    }
}
