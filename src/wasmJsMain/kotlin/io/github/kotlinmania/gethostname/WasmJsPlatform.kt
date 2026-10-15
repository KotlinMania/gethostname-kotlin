@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package io.github.kotlinmania.gethostname

private val hostnameImpl: () -> String? =
    js(
        "() => {\n" +
            "  try {\n" +
            "    if (typeof process !== 'undefined') {\n" +
            "      if (typeof process.getBuiltinModule === 'function') {\n" +
            "        const mod = process.getBuiltinModule('node:os') || process.getBuiltinModule('os');\n" +
            "        if (mod && typeof mod.hostname === 'function') {\n" +
            "          return mod.hostname();\n" +
            "        }\n" +
            "      }\n" +
            "      if (process.mainModule && typeof process.mainModule.require === 'function') {\n" +
            "        const mod = process.mainModule.require('os');\n" +
            "        if (mod && typeof mod.hostname === 'function') {\n" +
            "          return mod.hostname();\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  } catch (e) {}\n" +
            "  try {\n" +
            "    const fn = new Function('return typeof require === \"function\" ? require : null');\n" +
            "    const rq = (typeof module !== 'undefined' && module.require) ? module.require.bind(module) : fn();\n" +
            "    if (rq) {\n" +
            "      const mod = rq('os');\n" +
            "      if (mod && typeof mod.hostname === 'function') {\n" +
            "        return mod.hostname();\n" +
            "      }\n" +
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

public actual fun gethostname(): String =
    hostnameImpl() ?: throw RuntimeException("gethostname: host name unavailable in this Wasm-JS environment")
