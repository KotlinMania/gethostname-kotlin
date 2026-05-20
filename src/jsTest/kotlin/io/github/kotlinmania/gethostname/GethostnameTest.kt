// port-lint: source src/lib.rs (platform test glue, JS command/environment comparison)
package io.github.kotlinmania.gethostname

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

private fun nodeCommandHostname(): String? {
    if (!(js("typeof process !== 'undefined' && !!(process.versions && process.versions.node)") as Boolean)) {
        return null
    }

    val result = js(
        """
        (() => {
          const rq = (typeof module !== 'undefined' && module.require)
            ? module.require.bind(module)
            : (new Function('return typeof require === "function" ? require : null'))();
          if (!rq) {
            return { ok: false, stdout: 'failed to get hostname' };
          }
          const childProcess = rq('child_process');
          const isWindows = process.platform === 'win32';
          try {
            return {
              ok: true,
              stdout: childProcess.execFileSync(isWindows ? 'hostname' : 'uname', isWindows ? [] : ['-n'], {
                encoding: 'utf8',
                stdio: ['ignore', 'pipe', 'pipe']
              })
            };
          } catch (error) {
            return {
              ok: false,
              stdout: String((error && error.stderr) || (error && error.message) || error)
            };
          }
        })()
        """,
    )

    if (!(result.ok as Boolean)) {
        fail("Failed to get hostname! ${result.stdout}")
    }

    return result.stdout.toString()
}

private fun browserEnvironmentHostname(): String =
    js(
        """
        (() => {
          if (typeof window !== 'undefined' && window.location && window.location.hostname) {
            return window.location.hostname;
          }
          return 'localhost';
        })()
        """,
    ).toString()

class JsLibTest {
    @Test
    fun gethostnameMatchesSystemHostname() {
        val hostname = nodeCommandHostname() ?: browserEnvironmentHostname()
        assertTrue(hostname.isNotEmpty(), "Failed to get hostname: hostname empty?")
        assertEquals(hostname.trimEnd().lowercase(), gethostname().lowercase())
    }
}
