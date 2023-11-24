package com.hopcape.cryptotoolkit.keys

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.Key
import java.security.KeyStore
import java.security.spec.KeySpec
import javax.crypto.KeyGenerator
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

internal class KeystoreKeyManagerImpl: KeyManager {

    private val KEY_ALIAS = "newKeyAnotherKey"
    private val KEYSTORE_PROVIDER = "AndroidKeyStore"
    private val ITERATION_COUNT = 10000
    private val KEY_LENGTH = 256


    private val keyStore =
        KeyStore.getInstance(KEYSTORE_PROVIDER).apply {
            load(null)
        }

    override fun generateKey(password: String, salt: ByteArray): Key {
        return generateDerivedKey(password,salt)
    }

    private fun generateDerivedKey(password: String, salt: ByteArray): Key {
        val keySpec: KeySpec = PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH)
        val secretKeyFactory: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val keyBytes = secretKeyFactory.generateSecret(keySpec).encoded
        return SecretKeySpec(keyBytes, KeyProperties.KEY_ALGORITHM_AES)
    }

    private fun generateKey(): Key {
        if (keyStore.containsAlias(KEY_ALIAS)){
            return keyStore.getKey(KEY_ALIAS,null)
        }
        val keyGenerator = getKeyGenerator()

        return keyGenerator.generateKey()
    }

    private fun getKeyGenerator(): KeyGenerator {
        return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES).apply {
            KeyGenParameterSpec
                .Builder(KEY_ALIAS,KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_ENCRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build().also {
                    init(it)
                }

        }
    }

}