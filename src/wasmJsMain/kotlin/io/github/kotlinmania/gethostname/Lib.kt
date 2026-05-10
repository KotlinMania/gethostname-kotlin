// port-lint: source src/lib.rs (platform glue, Wasm-JS target via Node `os.hostname()`)
@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)
package io.github.kotlinmania.gethostname

private val hostnameImpl: () -> String =
    js(
        "() => {\n" +
            "  const os = require('os');\n" +
            "  return os.hostname();\n" +
            "}",
    )

public actual fun gethostname(): String = hostnameImpl()
