package org.walletconnect.walletconnectv2.storage

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.goterl.lazysodium.utils.HexMessageEncoder
import org.walletconnect.walletconnectv2.app
import org.walletconnect.walletconnectv2.crypto.data.Key
import org.walletconnect.walletconnectv2.util.Empty

class KeyChain : KeyStore {

    //Region: Move to DI
    // TODO: updated based on https://stackoverflow.com/a/63357267
    private val sharedPreferences: SharedPreferences
        get() = EncryptedSharedPreferences.create(
            sharedPrefsFile,
            mainKeyAlias,
            app.applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    //End of region

    override fun setKey(tag: String, key1: Key, key2: Key) {
        val keys = concatKeys(key1, key2)
        sharedPreferences.edit().putString(tag, keys).apply()
    }

    override fun getKeys(tag: String): Pair<String, String> {
        val concatKeys = sharedPreferences.getString(tag, String.Empty) ?: String.Empty
        return splitKeys(concatKeys)
    }

    override fun deleteKeys(tag: String) {
        sharedPreferences.edit().remove(tag).apply()
    }

    private fun concatKeys(keyA: Key, keyB: Key): String = with(HexMessageEncoder()) {
        encode(decode(keyA.keyAsHex) + decode(keyB.keyAsHex))
    }

    private fun splitKeys(concatKeys: String): Pair<String, String> = with(HexMessageEncoder()) {
        val concatKeysByteArray = decode(concatKeys)
        val privateKeyByteArray = concatKeysByteArray.sliceArray(0 until (concatKeysByteArray.size / 2))
        val publicKeyByteArray = concatKeysByteArray.sliceArray((concatKeysByteArray.size / 2) until concatKeysByteArray.size)
        return encode(privateKeyByteArray) to encode(publicKeyByteArray)
    }

    companion object {
        private const val sharedPrefsFile: String = "wc_key_store"
        private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        private val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
    }
}