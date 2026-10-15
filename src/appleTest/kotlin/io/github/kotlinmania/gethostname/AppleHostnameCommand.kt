package io.github.kotlinmania.gethostname

import io.github.kotlinmania.libc.Utsname
import io.github.kotlinmania.libc.unix.bsd.apple.uname

internal actual fun systemHostnameCommand(): HostnameCommandOutput {
    val info = Utsname()
    return if (uname(info) == 0) {
        val end = info.nodename.indexOf(0.toByte()).let { if (it < 0) info.nodename.size else it }
        HostnameCommandOutput(info.nodename.decodeToString(0, end), "", true)
    } else {
        HostnameCommandOutput("", "uname failed", false)
    }
}
