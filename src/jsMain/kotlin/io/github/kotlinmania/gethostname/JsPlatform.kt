// port-lint: ignore platform actual for the JavaScript target glue in src/lib.rs
package io.github.kotlinmania.gethostname

private fun nodeHostnameOrNull(): String? =
    js(
        """
        (() => {
          try {
            const rq = (typeof module !== 'undefined' && module.require)
              ? module.require.bind(module)
              : (new Function('return typeof require === "function" ? require : null'))();
            return rq ? rq('os').hostname() : null;
          } catch (error) {
            return null;
          }
        })()
        """,
    )?.toString()

private fun browserHostnameOrNull(): String? =
    if (js("typeof window !== 'undefined' && !!(window.location && window.location.hostname)") as Boolean) {
        val hostname = js("window.location.hostname")
        hostname?.toString()
    } else {
        null
    }

private fun getHostnameImpl(): String {
    return nodeHostnameOrNull()
        ?: browserHostnameOrNull()
        ?: throw RuntimeException("gethostname: host name unavailable in this JavaScript environment")
}

internal actual fun readHostname(): String = getHostnameImpl()
