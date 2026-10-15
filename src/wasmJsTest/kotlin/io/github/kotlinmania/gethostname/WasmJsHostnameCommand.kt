@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package io.github.kotlinmania.gethostname

private val nodeCommandHostnameImpl: () -> String? =
    js(
        "() => {\n" +
            "  if (typeof process === 'undefined' || !(process.versions && process.versions.node)) {\n" +
            "    return null;\n" +
            "  }\n" +
            "  let childProcess = null;\n" +
            "  try {\n" +
            "    if (typeof process.getBuiltinModule === 'function') {\n" +
            "      childProcess = process.getBuiltinModule('node:child_process') || process.getBuiltinModule('child_process');\n" +
            "    }\n" +
            "    if (!childProcess && process.mainModule && typeof process.mainModule.require === 'function') {\n" +
            "      childProcess = process.mainModule.require('child_process');\n" +
            "    }\n" +
            "  } catch (e) {}\n" +
            "  if (!childProcess) {\n" +
            "    try {\n" +
            "      const fn = new Function('return typeof require === \"function\" ? require : null');\n" +
            "      const rq = (typeof module !== 'undefined' && module.require) ? module.require.bind(module) : fn();\n" +
            "      if (rq) childProcess = rq('child_process');\n" +
            "    } catch (e) {}\n" +
            "  }\n" +
            "  if (!childProcess) {\n" +
            "    return null;\n" +
            "  }\n" +
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

private val browserEnvironmentHostnameImpl: () -> String? =
    js(
        "() => {\n" +
            "  if (typeof window !== 'undefined' && window.location && window.location.hostname) {\n" +
            "    return window.location.hostname;\n" +
            "  }\n" +
            "  return null;\n" +
            "}",
    )

internal actual fun systemHostnameCommand(): HostnameCommandOutput {
    val hostname =
        try {
            nodeCommandHostnameImpl()
        } catch (failure: Throwable) {
            return HostnameCommandOutput("", failure.message ?: "Failed to get hostname!", false)
        } ?: browserEnvironmentHostnameImpl()
            ?: return HostnameCommandOutput("", "host name unavailable in this Wasm-JS environment", false)

    return HostnameCommandOutput(hostname, "", true)
}
