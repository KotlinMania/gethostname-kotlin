// port-lint: source src/lib.rs (platform glue, JS target via Node `os.hostname()`)
package io.github.kotlinmania.gethostname

private fun isNodeRequireAvailable(): Boolean =
    js("typeof require !== 'undefined'") as Boolean

private fun nodeHostname(): String {
    // Dynamically require the os module at runtime (not compile time).
    // This avoids webpack trying to bundle it for browser targets.
    val nodeOs = js("require('os')")
    return nodeOs.hostname() as String
}

private fun browserHostnameOrNull(): String? =
    if (js("typeof window !== 'undefined' && window.location && window.location.hostname") as Boolean) {
        js("window.location.hostname") as String
    } else {
        null
    }

private fun getHostnameImpl(): String {
    return try {
        if (isNodeRequireAvailable()) {
            nodeHostname()
        } else {
            browserHostnameOrNull() ?: "localhost"
        }
    } catch (_: dynamic) {
        browserHostnameOrNull() ?: "localhost"
    }
}

public actual fun gethostname(): String = getHostnameImpl()
