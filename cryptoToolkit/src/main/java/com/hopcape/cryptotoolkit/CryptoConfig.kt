package com.hopcape.cryptotoolkit

import java.nio.charset.Charset

internal data class CryptoConfig(
    val transformation: String = "AES/GCM/NoPadding",
    val charset: Charset = Charsets.ISO_8859_1
)
