// port-lint: source src/lib.rs (platform test glue, Wasm-JS command/environment comparison)
@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package io.github.kotlinmania.gethostname

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

private val nodeCommandHostnameImpl: () -> String? =
    js(
        "() => {\n" +
            "  if (typeof process === 'undefined' || !(process.versions && process.versions.node)) {\n" +
            "    return null;\n" +
            "  }\n" +
            "  if (typeof require !== 'function') {\n" +
            "    return null;\n" +
            "  }\n" +
            "  const childProcess = require('child_process');\n" +
            "  const isWindows = process.platform === 'win32';\n" +
            "  try {\n" +
            "    return childProcess.execFileSync(isWindows ? 'hostname' : 'uname', isWindows ? [] : ['-n'], {\n" +
            "      encoding: 'utf8',\n" +
            "      stdio: ['ignore', 'pipe', 'pipe']\n" +
            "    });\n" +
            "  } catch (error) {\n" +
            "    throw new Error('Failed to get hostname! ' + String((error && error.stderr) || (error && error.message) || error));\n" +
            "  }\n" +
            "}",
    )

private val browserEnvironmentHostnameImpl: () -> String =
    js(
        "() => {\n" +
            "  if (typeof window !== 'undefined' && window.location && window.location.hostname) {\n" +
            "    return window.location.hostname;\n" +
            "  }\n" +
            "  return 'localhost';\n" +
            "}",
    )

class WasmJsLibTest {
    @Test
    fun gethostnameMatchesSystemHostname() {
        val hostname =
            try {
                nodeCommandHostnameImpl()
            } catch (failure: Throwable) {
                fail(failure.message ?: "Failed to get hostname!")
            } ?: browserEnvironmentHostnameImpl()

        assertTrue(hostname.isNotEmpty(), "Failed to get hostname: hostname empty?")
        assertEquals(hostname.trimEnd().lowercase(), gethostname().lowercase())
    }
}
