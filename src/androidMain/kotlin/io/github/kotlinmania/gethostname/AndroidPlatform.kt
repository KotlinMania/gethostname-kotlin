// port-lint: ignore platform actual for the Android cfg branch in src/lib.rs
package io.github.kotlinmania.gethostname

internal actual fun readHostname(): String {
    val process = ProcessBuilder("uname", "-n")
        .redirectErrorStream(true)
        .start()
    val output = process.inputStream.bufferedReader().use { it.readText() }
    val status = process.waitFor()
    if (status != 0) {
        throw RuntimeException("gethostname: uname failed: ${output.trimEnd()}")
    }
    return output.trimEnd()
}
