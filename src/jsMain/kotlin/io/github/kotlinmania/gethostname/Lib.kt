// port-lint: source src/lib.rs (platform glue, JS target via Node `os.hostname()`)
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
