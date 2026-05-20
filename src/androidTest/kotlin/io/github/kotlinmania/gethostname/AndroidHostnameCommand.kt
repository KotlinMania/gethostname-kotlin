// port-lint: ignore Android uname helper for the upstream src/lib.rs test
package io.github.kotlinmania.gethostname

import android.system.Os

internal actual fun systemHostnameCommand(): HostnameCommandOutput =
    HostnameCommandOutput(Os.uname().nodename, "", true)
