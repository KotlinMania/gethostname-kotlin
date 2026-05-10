// port-lint: source src/lib.rs (platform glue, JS target via Node `os.hostname()`)
package io.github.kotlinmania.gethostname

private fun nodeRequire(name: String): dynamic = js("require")(name)

public actual fun gethostname(): String {
    val os: dynamic = nodeRequire("os")
    return os.hostname().toString()
}
