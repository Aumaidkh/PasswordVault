package com.hopcape.cryptotoolkit

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hopcape.cryptotoolkit.decrypt.Decryptor
import com.hopcape.cryptotoolkit.decrypt.DecryptorImpl
import com.hopcape.cryptotoolkit.encrypt.Encryptor
import com.hopcape.cryptotoolkit.encrypt.EncryptorImpl
import com.hopcape.cryptotoolkit.keys.KeyManager
import com.hopcape.cryptotoolkit.keys.KeystoreKeyManagerImpl

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var encryptor: Encryptor
    private lateinit var decryptor: Decryptor
    private lateinit var keyManager: KeyManager

    @Before
    fun setup(){
        encryptor = EncryptorImpl(CryptoConfig())
        decryptor = DecryptorImpl(CryptoConfig())
        keyManager = KeystoreKeyManagerImpl()
    }
    @Test
    fun encryptionDecryption_worksCorrectly(){
        val plainText = "Hello World"
        val password = "This is dummy Password"
        val salt = "RandomSalt".toByteArray()

        val key = keyManager.generateKey(
            password = password,
            salt = salt
        )
        val encryptedString = encryptor.encrypt(
            data = plainText,
            key = key
        )
        val decryptedString = decryptor.decrypt(
            data = encryptedString ?: "",
            key = key
        )
        assert(plainText == decryptedString)
    }

    @Test
    fun encryptionDecryption_doesNotWork_whenTwoDifferentPasswordsAreUsed(){
        val plainText = "Hello World"
        val password = "This is dummy Password"
        val salt = "RandomSalt".toByteArray()

        val key1 = keyManager.generateKey(
            password = password,
            salt = salt
        )
        val encryptedString = encryptor.encrypt(
            data = plainText,
            key = key1
        )
        val key2 = keyManager.generateKey(
            password = "NewPassword",
            salt = salt
        )
        val decryptedString = decryptor.decrypt(
            data = encryptedString ?: "",
            key = key2
        )
        assert(plainText != decryptedString)
    }
}