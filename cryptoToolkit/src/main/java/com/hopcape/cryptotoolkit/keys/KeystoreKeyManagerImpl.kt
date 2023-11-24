package com.hopcape.cryptotoolkit.keys

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class KeystoreKeyManagerImpl: KeyManager {

    private val KEY_ALIAS = "alias"
    private val KEYSTORE_PROVIDER = "AndroidKeyStore"
    private val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    private val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
    private val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
    private val KEY_SIZE = 256
    private val KEY_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES

    private val keyStore =
        KeyStore.getInstance(KEYSTORE_PROVIDER).apply {
            load(null)
        }

    override fun generateKey(password: String, salt: ByteArray): SecretKey {
        val existingKey = keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey(password, salt)
    }

    private fun createKey(password: String, salt: ByteArray): SecretKey {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password.toCharArray(), salt, 10000, KEY_SIZE)
        val secretKey = SecretKeySpec(factory.generateSecret(spec).encoded, KEY_ALGORITHM)

        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(BLOCK_MODE)
            .setEncryptionPaddings(PADDING)
            .setUserAuthenticationRequired(false)
            .setKeySize(KEY_SIZE)
            .build()

        val keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM, KEYSTORE_PROVIDER)
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()

        return secretKey
    }

}