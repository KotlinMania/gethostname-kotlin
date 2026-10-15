package io.github.kotlinmania.gethostname

internal data class HostnameCommandOutput(
    val stdout: String,
    val stderr: String,
    val success: Boolean,
)

internal expect fun systemHostnameCommand(): HostnameCommandOutput
