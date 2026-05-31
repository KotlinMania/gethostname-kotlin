// port-lint: source src/lib.rs
package io.github.kotlinmania.gethostname

public actual fun gethostname(): String {
    val process =
        ProcessBuilder("uname", "-n")
            .redirectErrorStream(true)
            .start()
    val output = process.inputStream.bufferedReader().use { it.readText() }
    val status = process.waitFor()
    if (status != 0) {
        check(status == 0) { "gethostname: uname failed: ${output.trimEnd()}" }
    }
    return output.trimEnd()
}
