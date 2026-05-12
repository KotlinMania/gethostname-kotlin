// port-lint: source src/lib.rs (platform glue, mingw target via GetComputerNameExW)
package io.github.kotlinmania.gethostname

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UIntVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.windows.GetComputerNameExW
import platform.windows.WCHARVar
import platform.windows._COMPUTER_NAME_FORMAT

// The DNS host name of the local computer. If the local computer is a node in a cluster, lpBuffer
// receives the DNS host name of the local computer, not the name of the cluster virtual server.
@OptIn(ExperimentalForeignApi::class)
public actual fun gethostname(): String = memScoped {
    val bufferSize = alloc<UIntVar>().apply { value = 0u }

    // This call always fails with ERROR_MORE_DATA, because we pass NULL to get the required buffer
    // size. GetComputerNameExW then fills bufferSize with the size of the host name string plus a
    // trailing zero word.
    GetComputerNameExW(
        _COMPUTER_NAME_FORMAT.ComputerNamePhysicalDnsHostname,
        null,
        bufferSize.ptr,
    )
    check(bufferSize.value > 0u) { "GetComputerNameExW did not provide buffer size" }

    val buffer = allocArray<WCHARVar>((bufferSize.value + 1u).toInt())
    if (GetComputerNameExW(
            _COMPUTER_NAME_FORMAT.ComputerNamePhysicalDnsHostname,
            buffer,
            bufferSize.ptr,
        ) == 0
    ) {
        throw RuntimeException(
            "GetComputerNameExW failed to read hostname.\n" +
                "        Please report this issue to <https://github.com/KotlinMania/gethostname-kotlin/issues>!",
        )
    }

    val total = bufferSize.value.toInt()
    val chars = CharArray(total) { i -> buffer[i].toInt().toChar() }
    chars.concatToString()
}
