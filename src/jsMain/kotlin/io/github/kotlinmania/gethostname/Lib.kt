// port-lint: source src/lib.rs (platform glue, JS target via Node `os.hostname()`)
package io.github.kotlinmania.gethostname

private fun getHostnameImpl(): String {
    return try {
        // Check if we're in Node.js environment
        if (js("typeof require !== 'undefined'") as Boolean) {
            // Dynamically require the os module at runtime (not compile time)
            // This avoids webpack trying to bundle it for browser targets
            val nodeOs = js("require('os')")
            nodeOs.hostname() as String
        } else if (js("typeof window !== 'undefined' && window.location && window.location.hostname") as Boolean) {
            // Browser environment - return the browser's location hostname
            js("window.location.hostname") as String
        } else {
            // Fallback for other environments
            "localhost"
        }
    } catch (e: dynamic) {
        // If require fails (e.g., in browser after webpack bundling), try browser fallback
        try {
            if (js("typeof window !== 'undefined' && window.location && window.location.hostname") as Boolean) {
                js("window.location.hostname") as String
            } else {
                "localhost"
            }
        } catch (e2: dynamic) {
            "localhost"
        }
    }
}

public actual fun gethostname(): String = getHostnameImpl()
