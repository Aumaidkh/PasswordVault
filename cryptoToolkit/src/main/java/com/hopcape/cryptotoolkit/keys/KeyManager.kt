package com.hopcape.cryptotoolkit.keys

import javax.crypto.SecretKey

interface KeyManager {
    fun generateKey(password: String, salt: ByteArray): SecretKey

}