package com.hopcape.cryptotoolkit

import com.hopcape.cryptotoolkit.decrypt.Decryptor
import com.hopcape.cryptotoolkit.encrypt.Encryptor
import com.hopcape.cryptotoolkit.keys.KeyManager
import java.security.Key

class CryptoManager(
    private val encryptor: Encryptor,
    private val decryptor: Decryptor,
    private val keyManager: KeyManager
) {
    fun encrypt(plainText: String,key: Key) =
        encryptor.encrypt(plainText,key)

    fun decrypt(encryptedString: String, key: Key) =
        decryptor.decrypt(
            data = encryptedString,
            key = key
        )

    fun createKey(password: String,salt: ByteArray) =
        keyManager.generateKey(
            password = password,
            salt = salt
        )
}