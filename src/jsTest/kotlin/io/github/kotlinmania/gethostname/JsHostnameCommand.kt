// port-lint: ignore JavaScript command helper for the upstream src/lib.rs test
package io.github.kotlinmania.gethostname

import kotlin.test.fail

private fun nodeCommandHostname(): HostnameCommandOutput? {
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
        return HostnameCommandOutput("", result.stdout.toString(), false)
    }

    return HostnameCommandOutput(result.stdout.toString(), "", true)
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

internal actual fun systemHostnameCommand(): HostnameCommandOutput =
    nodeCommandHostname() ?: HostnameCommandOutput(browserEnvironmentHostname(), "", true)
