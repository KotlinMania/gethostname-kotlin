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
public actual fun gethostname(): String = memScoped {
    val info = alloc<utsname>()
    val succeeded = uname(info.ptr) == 0
    if (!succeeded) {
        val code = errno
        val message = strerror(code)?.toKString() ?: "errno=$code"
        check(succeeded) { "gethostname: uname failed: $message" }
    }
    info.nodename.toKString()
}
