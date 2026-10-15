package io.github.kotlinmania.gethostname

import io.github.kotlinmania.libc.unix.linuxlike.Utsname
import io.github.kotlinmania.libc.unix.linuxlike.uname

public actual fun gethostname(): String {
    val info = Utsname()
    val succeeded = uname(info) == 0
    check(succeeded) { "gethostname: uname failed" }
    val end = info.nodename.indexOf(0.toByte()).let { if (it < 0) info.nodename.size else it }
    return info.nodename.decodeToString(0, end)
}
