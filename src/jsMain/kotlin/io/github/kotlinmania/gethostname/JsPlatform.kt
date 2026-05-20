// port-lint: ignore platform actual for the JavaScript target glue in src/lib.rs
package io.github.kotlinmania.gethostname

private fun isNodeRequireAvailable(): Boolean =
    js("typeof require !== 'undefined'") as Boolean

private fun nodeHostname(): String {
    // Dynamically require the os module at runtime (not compile time).
    // This avoids webpack trying to bundle it for browser targets.
    val nodeOs = js("require('os')")
    return nodeOs.hostname().toString()
}

private fun browserHostnameOrNull(): String? =
    if (js("typeof window !== 'undefined' && !!(window.location && window.location.hostname)") as Boolean) {
        val hostname = js("window.location.hostname")
        hostname?.toString()
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

internal actual fun readHostname(): String = getHostnameImpl()
