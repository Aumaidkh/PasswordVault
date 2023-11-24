package com.hopcape.cryptotoolkit

import android.content.Context
import com.hopcape.cryptotoolkit.decrypt.DecryptorImpl
import com.hopcape.cryptotoolkit.encrypt.EncryptorImpl
import com.hopcape.cryptotoolkit.keys.KeystoreKeyManagerImpl
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class CryptoManagerDelegate: ReadOnlyProperty<Any,CryptoManager> {

    private val config by lazy {
        CryptoConfig()
    }
    override fun getValue(thisRef: Any, property: KProperty<*>): CryptoManager {
        return CryptoManager(
            encryptor = EncryptorImpl(config),
            decryptor = DecryptorImpl(config),
            keyManager = KeystoreKeyManagerImpl()
        )
    }
}

fun Context.getCryptoManager() = CryptoManagerDelegate()