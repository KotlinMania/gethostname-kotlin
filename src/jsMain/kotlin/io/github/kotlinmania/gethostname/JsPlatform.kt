package io.github.kotlinmania.gethostname

private fun nodeHostnameOrNull(): String? =
    js(
        """
        (() => {
          try {
            if (typeof process !== 'undefined') {
              if (typeof process.getBuiltinModule === 'function') {
                const mod = process.getBuiltinModule('node:os') || process.getBuiltinModule('os');
                if (mod && typeof mod.hostname === 'function') return mod.hostname();
              }
              if (process.mainModule && typeof process.mainModule.require === 'function') {
                const mod = process.mainModule.require('os');
                if (mod && typeof mod.hostname === 'function') return mod.hostname();
              }
            }
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

private fun getHostnameImpl(): String =
    nodeHostnameOrNull()
        ?: browserHostnameOrNull()
        ?: throw RuntimeException("gethostname: host name unavailable in this JavaScript environment")

public actual fun gethostname(): String = getHostnameImpl()
