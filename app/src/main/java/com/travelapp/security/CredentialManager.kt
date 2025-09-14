package com.travelapp.security

import android.content.Context
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * Secure credential management for API keys and sensitive data
 * 
 * WARNING: This is a basic implementation for development.
 * In production, use Android Keystore and proper key management.
 */
object CredentialManager {
    
    private const val PREFS_NAME = "secure_credentials"
    private const val ENCRYPTED_API_KEY = "encrypted_api_key"
    private const val ENCRYPTED_CLIENT_ID = "encrypted_client_id"
    
    // Base64 encoded encryption key (in production, generate this securely)
    private val ENCRYPTION_KEY = "MySecretKey12345".toByteArray()
    
    /**
     * Initialize credentials for development
     * In production, these would be loaded from secure storage
     */
    fun initializeCredentials(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        
        // Only set if not already present
        if (!prefs.contains(ENCRYPTED_API_KEY)) {
            val apiKey = "AIzaSyD2wR2RWXptEZBXIc5nITMg-Whdy7sZLww"
            val clientId = "163020410697-vh21r0uh65d0rdlfbmiu2n55ghnuedj0.apps.googleusercontent.com"
            
            prefs.edit()
                .putString(ENCRYPTED_API_KEY, encrypt(apiKey))
                .putString(ENCRYPTED_CLIENT_ID, encrypt(clientId))
                .apply()
        }
    }
    
    /**
     * Get decrypted API key
     */
    fun getApiKey(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val encrypted = prefs.getString(ENCRYPTED_API_KEY, "") ?: ""
        return decrypt(encrypted)
    }
    
    /**
     * Get decrypted Client ID
     */
    fun getClientId(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val encrypted = prefs.getString(ENCRYPTED_CLIENT_ID, "") ?: ""
        return decrypt(encrypted)
    }
    
    /**
     * Simple encryption (for development only)
     * In production, use Android Keystore
     */
    private fun encrypt(plainText: String): String {
        return try {
            val key = SecretKeySpec(ENCRYPTION_KEY, "AES")
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val encrypted = cipher.doFinal(plainText.toByteArray())
            Base64.encodeToString(encrypted, Base64.DEFAULT)
        } catch (e: Exception) {
            // Fallback to base64 encoding if encryption fails
            Base64.encodeToString(plainText.toByteArray(), Base64.DEFAULT)
        }
    }
    
    /**
     * Simple decryption (for development only)
     * In production, use Android Keystore
     */
    private fun decrypt(encryptedText: String): String {
        return try {
            val key = SecretKeySpec(ENCRYPTION_KEY, "AES")
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.DECRYPT_MODE, key)
            val encrypted = Base64.decode(encryptedText, Base64.DEFAULT)
            String(cipher.doFinal(encrypted))
        } catch (e: Exception) {
            // Fallback to base64 decoding if decryption fails
            String(Base64.decode(encryptedText, Base64.DEFAULT))
        }
    }
}
