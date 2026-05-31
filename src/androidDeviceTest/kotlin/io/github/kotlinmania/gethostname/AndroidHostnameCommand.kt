// port-lint: source src/lib.rs
package io.github.kotlinmania.gethostname

internal actual fun systemHostnameCommand(): HostnameCommandOutput {
    val process =
        ProcessBuilder("uname", "-n")
            .redirectErrorStream(true)
            .start()
    val output = process.inputStream.bufferedReader().use { it.readText() }
    val status = process.waitFor()
    return HostnameCommandOutput(output, output, status == 0)
}
