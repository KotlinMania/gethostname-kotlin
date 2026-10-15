package io.github.kotlinmania.gethostname

import io.github.kotlinmania.windowssys.windows.win32.system.systeminformation.ComputerNamePhysicalDnsHostname
import io.github.kotlinmania.windowssys.windows.win32.system.systeminformation.GetComputerNameExW

// The DNS host name of the local computer. If the local computer is a node in a cluster, lpBuffer
// receives the DNS host name of the local computer, not the name of the cluster virtual server.
public actual fun gethostname(): String = getComputerPhysicalDnsHostname()

private fun getComputerPhysicalDnsHostname(): String {
    val bufferSize = uintArrayOf(0u)

    // This call always fails with ERROR_MORE_DATA, because we pass NULL to get the required buffer
    // size. GetComputerNameExW then fills bufferSize with the size of the host name string plus a
    // trailing zero word.
    GetComputerNameExW(
        ComputerNamePhysicalDnsHostname,
        null,
        bufferSize,
    )
    check(bufferSize[0] > 0u) { "GetComputerNameExW did not provide buffer size" }

    val capacity = bufferSize[0].toInt()
    val buffer = UShortArray(capacity)
    val readSucceeded =
        GetComputerNameExW(
            ComputerNamePhysicalDnsHostname,
            buffer,
            bufferSize,
        ) != 0
    check(readSucceeded) {
        "GetComputerNameExW failed to read hostname.\n" +
            "        Please report this issue to <https://github.com/KotlinMania/gethostname-kotlin/issues>!"
    }
    check(bufferSize[0].toInt() == capacity - 1) {
        "GetComputerNameExW changed the buffer size unexpectedly"
    }

    val total = bufferSize[0].toInt()
    val chars = CharArray(total) { i -> buffer[i].toInt().toChar() }
    return chars.concatToString()
}
