package com.hopcape.cryptotoolkit.decrypt

import android.util.Base64
import com.hopcape.cryptotoolkit.CryptoConfig
import java.io.ByteArrayInputStream
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec

internal class DecryptorImpl(private val cryptoConfig: CryptoConfig): Decryptor {

    override fun decrypt(data: String, key: Key): String? {

        val combinedBytes = Base64.decode(data,Base64.DEFAULT)
        val byteInputStream= ByteArrayInputStream(combinedBytes)
        byteInputStream.use {
            // Read Iv Size
            val ivSize = it.read()
            // Read Iv
            val iv = ByteArray(ivSize)
            it.read(iv)
            // Read Encrypted Bytes Size
            val encryptedBytesSize = it.read()
            // Read Encrypted Bytes
            val encryptedBytes = ByteArray(encryptedBytesSize)
            it.read(encryptedBytes)
            val decryptionCipher =
                getDecryptionCipher(key,iv)

            return try {
                val decryptedBytes = decryptionCipher.doFinal(encryptedBytes)
                String(decryptedBytes,cryptoConfig.charset)
            } catch (e: Exception){
                e.printStackTrace()
                null
            }
        }
    }

    private fun getDecryptionCipher(key: Key,iv: ByteArray): Cipher {
        return Cipher.getInstance(cryptoConfig.transformation).apply {
            init(Cipher.DECRYPT_MODE, key,GCMParameterSpec(128,iv))
        }
    }
}