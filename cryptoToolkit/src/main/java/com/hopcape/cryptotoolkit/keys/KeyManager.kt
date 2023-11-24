package com.hopcape.cryptotoolkit.keys

import java.security.Key

interface KeyManager {
    fun generateKey(password: String, salt: ByteArray): Key

}