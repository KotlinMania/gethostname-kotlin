// port-lint: source src/lib.rs (platform test glue, POSIX command comparison)
package io.github.kotlinmania.gethostname

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.cinterop.toKString
import platform.posix.fgets
import platform.posix.pclose
import platform.posix.popen
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

private data class CommandOutput(
    val stdout: String,
    val status: Int,
)

private fun exitCode(status: Int): Int {
    val exited = (status and 0x7f) == 0
    return if (exited) (status shr 8) and 0xff else 1
}

@OptIn(ExperimentalForeignApi::class)
private fun readCommand(command: String): CommandOutput {
    val pipe = popen(command, "r") ?: fail("failed to get hostname")
    val output = StringBuilder()
    val buffer = ByteArray(512)

    while (true) {
        val line = fgets(buffer.refTo(0), buffer.size, pipe) ?: break
        output.append(line.toKString())
    }

    return CommandOutput(output.toString(), exitCode(pclose(pipe)))
}

class PosixLibTest {
    @Test
    fun gethostnameMatchesSystemHostname() {
        val output = readCommand("uname -n 2>&1")
        if (output.status == 0) {
            val hostname = output.stdout
            assertTrue(hostname.isNotEmpty(), "Failed to get hostname: hostname empty?")
            assertEquals(hostname.trimEnd().lowercase(), gethostname().lowercase())
        } else {
            fail("Failed to get hostname! ${output.stdout}")
        }
    }
}
