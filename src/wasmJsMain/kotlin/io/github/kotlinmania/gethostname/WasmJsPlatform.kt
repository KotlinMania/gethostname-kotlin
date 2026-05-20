// port-lint: ignore platform actual for the Wasm-JS target glue in src/lib.rs
@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)
package io.github.kotlinmania.gethostname

private val hostnameImpl: () -> String? =
    js(
        "() => {\n" +
            "  try {\n" +
            "    const rq = typeof require === 'function' ? require : null;\n" +
            "    if (rq) {\n" +
            "      return rq('os').hostname();\n" +
            "    }\n" +
            "  } catch (e) {}\n" +
            "  try {\n" +
            "    if (typeof window !== 'undefined' && window.location && window.location.hostname) {\n" +
            "      return window.location.hostname;\n" +
            "    }\n" +
            "  } catch (e) {}\n" +
            "  return null;\n" +
            "}",
    )

internal actual fun readHostname(): String =
    hostnameImpl() ?: throw RuntimeException("gethostname: host name unavailable in this Wasm-JS environment")
