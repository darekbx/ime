package em.i.authentication

import android.content.Context
import android.content.Context.FINGERPRINT_SERVICE
import android.hardware.fingerprint.FingerprintManager
import android.os.CancellationSignal
import java.security.KeyStore
import android.security.keystore.KeyProperties
import android.security.keystore.KeyGenParameterSpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class Fingerprint {

    companion object {
        val KEY_NAME = "emi_key"
        val ANDROID_KEY_STORE = "AndroidKeyStore"
    }

    var cancellationSignal: CancellationSignal? = null

    private fun keyGenerator() {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
        with(keyGenerator) {
            init(
                    KeyGenParameterSpec
                            .Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                            .setUserAuthenticationRequired(true)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                            .build()
            )
            generateKey()
        }
    }

    private fun createCipher(keyStore: KeyStore): Cipher {
        val key = keyStore.getKey(KEY_NAME, null)
        return Cipher.getInstance(
                "${KeyProperties.KEY_ALGORITHM_AES}" +
                        "/${KeyProperties.BLOCK_MODE_CBC}" +
                        "/${KeyProperties.ENCRYPTION_PADDING_PKCS7}")
                .apply {
                    init(Cipher.ENCRYPT_MODE, key)
                }
    }

    fun stopListeningAuthentication() {
        if (cancellationSignal != null) {
            cancellationSignal?.cancel()
            cancellationSignal = null
        }
    }

    fun listenForFingerprint(context: Context, callback: FingerprintManager.AuthenticationCallback) {
        val fingerprintManager = context.getSystemService(FINGERPRINT_SERVICE) as FingerprintManager
        fingerprintManager
                .takeIf { it.isHardwareDetected and it.hasEnrolledFingerprints() }
                ?.let {
                    try {
                        startAuthentication(it, callback)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        callback.onAuthenticationError(0, e.message)
                    }
                }
    }

    private fun startAuthentication(it: FingerprintManager, callback: FingerprintManager.AuthenticationCallback) {
        keyGenerator()
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }
        val cipher = createCipher(keyStore).apply {
            init(Cipher.ENCRYPT_MODE, keyStore.getKey(KEY_NAME, null) as SecretKey)
        }
        cancellationSignal = CancellationSignal()
        it.authenticate(FingerprintManager.CryptoObject(cipher), cancellationSignal, 0, callback, null)
    }
}