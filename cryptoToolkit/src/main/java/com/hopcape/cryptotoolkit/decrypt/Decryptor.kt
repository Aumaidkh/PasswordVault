package com.hopcape.cryptotoolkit.decrypt

import java.security.Key

interface Decryptor {
    fun decrypt(data: String,key: Key): String?
}