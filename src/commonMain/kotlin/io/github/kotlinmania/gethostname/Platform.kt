// port-lint: ignore KMP expect declaration for platform hostname backends
package io.github.kotlinmania.gethostname

internal expect fun readHostname(): String
