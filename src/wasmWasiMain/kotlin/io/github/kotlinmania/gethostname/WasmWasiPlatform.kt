// port-lint: ignore - Wasm-WASI actual for src/lib.rs; WASI preview1 has no hostname syscall.
package io.github.kotlinmania.gethostname

// WASI preview1 exposes neither gethostname(2) nor uname(2), and provides no
// subprocess facility to shell out to `hostname`. There is no faithful host
// name to return in this sandbox, so the call reports the capability gap
// honestly rather than fabricating a value.
public actual fun gethostname(): String = throw UnsupportedOperationException("gethostname: WASI preview1 provides no hostname syscall")
