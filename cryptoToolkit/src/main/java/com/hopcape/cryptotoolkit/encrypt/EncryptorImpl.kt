package com.hopcape.cryptotoolkit.encrypt

import android.os.Build
import com.hopcape.cryptotoolkit.CryptoConfig
import java.io.ByteArrayOutputStream
import java.security.Key
import java.util.Base64
import javax.crypto.Cipher

internal class EncryptorImpl(private val cryptoConfig: CryptoConfig) : Encryptor {

    override fun encrypt(data: String, key: Key): String? {
        val cipher = getEncryptCipher(key)
        val iv = cipher.iv

        return runCatching {
            val result = cipher.doFinal(data.toByteArray(Charsets.ISO_8859_1))

            val outputStream = ByteArrayOutputStream()
            outputStream.use { stream ->
                // Write iv size
                stream.write(iv.size)
                // Write the iv
                stream.write(iv)
                // Write the encrypted bytes size
                stream.write(result.size)
                // Write the actual bytes
                stream.write(result)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Base64.getEncoder().encodeToString(outputStream.toByteArray())
            } else {
                android.util.Base64.encodeToString(outputStream.toByteArray(), android.util.Base64.DEFAULT)
            }
        }.getOrElse {
            it.printStackTrace()
            null
        }
    }

    private fun getEncryptCipher(key: Key): Cipher {
        return Cipher.getInstance(cryptoConfig.transformation).apply {
            init(Cipher.ENCRYPT_MODE, key)
        }
    }
}