package com.hopcape.cryptotoolkit.encrypt

import java.security.Key

interface Encryptor {
    fun encrypt(data: String,key: Key): String?
}