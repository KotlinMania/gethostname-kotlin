package io.github.kotlinmania.gethostname

public actual fun gethostname(): String {
    val isWindows = System.getProperty("os.name").startsWith("Windows", ignoreCase = true)
    if (isWindows) {
        val env = System.getenv("COMPUTERNAME")
        if (!env.isNullOrBlank()) {
            return env
        }
        return try {
            java.net.InetAddress.getLocalHost().hostName
        } catch (_: Throwable) {
            val process = ProcessBuilder("hostname").redirectErrorStream(true).start()
            val output = process.inputStream.bufferedReader().use { it.readText() }
            val status = process.waitFor()
            check(status == 0) { "gethostname: hostname failed: ${output.trimEnd()}" }
            output.trimEnd()
        }
    }
    return try {
        val process = ProcessBuilder("uname", "-n").redirectErrorStream(true).start()
        val output = process.inputStream.bufferedReader().use { it.readText() }
        val status = process.waitFor()
        if (status == 0 && output.isNotBlank()) {
            output.trimEnd()
        } else {
            java.net.InetAddress.getLocalHost().hostName
        }
    } catch (_: Throwable) {
        val env = System.getenv("HOSTNAME")
        if (!env.isNullOrBlank()) {
            env
        } else {
            java.net.InetAddress.getLocalHost().hostName
        }
    }
}
