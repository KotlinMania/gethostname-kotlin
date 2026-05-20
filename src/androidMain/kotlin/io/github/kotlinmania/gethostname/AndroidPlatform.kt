// port-lint: ignore platform actual for the Android cfg branch in src/lib.rs
package io.github.kotlinmania.gethostname

import android.system.Os

internal actual fun readHostname(): String = Os.uname().nodename
