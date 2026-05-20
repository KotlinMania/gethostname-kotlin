// port-lint: ignore POSIX command helper for the upstream src/lib.rs test
package io.github.kotlinmania.gethostname

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.cinterop.toKString
import platform.posix.fgets
import platform.posix.pclose
import platform.posix.popen
import kotlin.test.fail

private fun exitCode(status: Int): Int {
    val exited = (status and 0x7f) == 0
    return if (exited) (status shr 8) and 0xff else 1
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun systemHostnameCommand(): HostnameCommandOutput {
    val command = "uname -n 2>&1"
    val pipe = popen(command, "r") ?: fail("failed to get hostname")
    val output = StringBuilder()
    val buffer = ByteArray(512)

    while (true) {
        val line = fgets(buffer.refTo(0), buffer.size, pipe) ?: break
        output.append(line.toKString())
    }

    val status = exitCode(pclose(pipe))
    return HostnameCommandOutput(output.toString(), output.toString(), status == 0)
}
