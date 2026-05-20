// port-lint: source src/lib.rs (platform glue, Android target via uname(2))
package io.github.kotlinmania.gethostname

import android.system.Os

public actual fun gethostname(): String = Os.uname().nodename
