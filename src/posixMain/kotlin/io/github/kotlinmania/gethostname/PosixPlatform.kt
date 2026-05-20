// port-lint: ignore platform actual for the Unix cfg branch in src/lib.rs
package io.github.kotlinmania.gethostname

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import platform.posix.errno
import platform.posix.strerror
import platform.posix.uname
import platform.posix.utsname

@OptIn(ExperimentalForeignApi::class)
internal actual fun readHostname(): String = memScoped {
    val info = alloc<utsname>()
    if (uname(info.ptr) != 0) {
        val code = errno
        val message = strerror(code)?.toKString() ?: "errno=$code"
        throw RuntimeException("gethostname: uname failed: $message")
    }
    info.nodename.toKString()
}
