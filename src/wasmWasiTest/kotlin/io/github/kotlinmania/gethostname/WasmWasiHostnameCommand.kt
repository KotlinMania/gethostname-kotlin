// port-lint: ignore - Wasm-WASI test actual; WASI preview1 exposes no hostname facility.
package io.github.kotlinmania.gethostname

// WASI has no uname/gethostname syscall and no subprocess, so there is no
// system hostname to compare against. The shared test recognises this via the
// UnsupportedOperationException thrown by gethostname() and skips the
// comparison; this actual exists only to satisfy the expect on this target.
internal actual fun systemHostnameCommand(): HostnameCommandOutput = HostnameCommandOutput("", "WASI provides no hostname facility", false)
