// port-lint: source src/lib.rs (platform glue, Wasm-JS target via Node `os.hostname()`)
@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)
package io.github.kotlinmania.gethostname

private val hostnameImpl: () -> String =
    js(
        "() => {\n" +
            "  if (typeof require !== 'undefined') { return require('os').hostname(); }\n" +
            "  if (typeof window !== 'undefined' && window.location && window.location.hostname) { return window.location.hostname; }\n" +
            "  return 'localhost';\n" +
            "}",
    )

public actual fun gethostname(): String = hostnameImpl()
