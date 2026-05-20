// port-lint: source src/lib.rs
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
internal actual fun systemHostnameCommand(): HostnameCommandOutput = memScoped {
    val info = alloc<utsname>()
    if (uname(info.ptr) == 0) {
        HostnameCommandOutput(info.nodename.toKString(), "", true)
    } else {
        val code = errno
        val message = strerror(code)?.toKString() ?: "errno=$code"
        HostnameCommandOutput("", message, false)
    }
}
