// port-lint: ignore Windows command helper for the upstream src/lib.rs test
package io.github.kotlinmania.gethostname

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.cinterop.toKString
import platform.posix._pclose
import platform.posix._popen
import platform.posix.fgets
import kotlin.test.fail

@OptIn(ExperimentalForeignApi::class)
internal actual fun systemHostnameCommand(): HostnameCommandOutput {
    val command = "hostname 2>&1"
    val pipe = _popen(command, "r") ?: fail("failed to get hostname")
    val output = StringBuilder()
    val buffer = ByteArray(512)

    while (true) {
        val line = fgets(buffer.refTo(0), buffer.size, pipe) ?: break
        output.append(line.toKString())
    }

    val status = _pclose(pipe)
    return HostnameCommandOutput(output.toString(), output.toString(), status == 0)
}
