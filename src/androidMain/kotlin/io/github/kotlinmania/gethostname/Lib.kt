// port-lint: source src/lib.rs (platform glue, Android/JVM target via java.net.InetAddress)
package io.github.kotlinmania.gethostname

import java.net.InetAddress

public actual fun gethostname(): String = InetAddress.getLocalHost().hostName
