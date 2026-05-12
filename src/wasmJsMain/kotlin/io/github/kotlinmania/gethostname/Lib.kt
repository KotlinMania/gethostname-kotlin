// port-lint: source src/lib.rs (platform glue, Wasm-JS target via Node `os.hostname()`)
@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)
package io.github.kotlinmania.gethostname

// For WasmJS, we can't use @JsModule or dynamic types, so we use a simple js() call
// with proper fallbacks that webpack can handle
private val hostnameImpl: () -> String =
    js(
        "() => {\n" +
            "  try {\n" +
            "    if (typeof require !== 'undefined') {\n" +
            "      const os = require('os');\n" +
            "      return os.hostname();\n" +
            "    }\n" +
            "  } catch (e) {}\n" +
            "  try {\n" +
            "    if (typeof window !== 'undefined' && window.location && window.location.hostname) {\n" +
            "      return window.location.hostname;\n" +
            "    }\n" +
            "  } catch (e) {}\n" +
            "  return 'localhost';\n" +
            "}",
    )

public actual fun gethostname(): String = hostnameImpl()
