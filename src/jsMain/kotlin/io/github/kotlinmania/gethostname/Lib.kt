// port-lint: source src/lib.rs (platform glue, JS target via Node `os.hostname()`)
package io.github.kotlinmania.gethostname

// External declarations for Node.js os module
@JsModule("os")
@JsNonModule
private external object NodeOs {
    fun hostname(): String
}

private fun getHostnameImpl(): String {
    return try {
        // Check if we're in Node.js environment
        if (js("typeof require !== 'undefined'") as Boolean) {
            NodeOs.hostname()
        } else if (js("typeof window !== 'undefined' && window.location && window.location.hostname") as Boolean) {
            // Browser environment - return the browser's location hostname
            js("window.location.hostname") as String
        } else {
            // Fallback for other environments
            "localhost"
        }
    } catch (e: dynamic) {
        // If NodeOs fails (e.g., in browser after webpack bundling), try browser fallback
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
