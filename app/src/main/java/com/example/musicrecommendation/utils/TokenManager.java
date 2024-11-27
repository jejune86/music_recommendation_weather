package com.example.musicrecommendation.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class TokenManager {
    private static final String TAG = "TokenManager";
    private static final String PREF_NAME = "SpotifyTokenPrefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_EXPIRY_TIME = "expiry_time";
    private static final String KEY_IV = "encryption_iv";
    
    private static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    private static final String KEY_ALIAS = "SpotifyTokenKey";
    
    private final SharedPreferences prefs;
    private final Context context;
    
    public TokenManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        initializeKeyStore();
    }
    
    private void initializeKeyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
            keyStore.load(null);
            
            if (!keyStore.containsAlias(KEY_ALIAS)) {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES,
                    KEYSTORE_PROVIDER
                );
                
                KeyGenParameterSpec keySpec = new KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setRandomizedEncryptionRequired(true)
                    .build();
                
                keyGenerator.init(keySpec);
                keyGenerator.generateKey();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error initializing KeyStore", e);
        }
    }
    
    public void saveToken(String token, long expiresIn) {
        try {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
            keyStore.load(null);
            SecretKey secretKey = (SecretKey) keyStore.getKey(KEY_ALIAS, null);
            
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            
            byte[] encryptedToken = cipher.doFinal(token.getBytes(StandardCharsets.UTF_8));
            String encodedToken = Base64.encodeToString(encryptedToken, Base64.DEFAULT);
            String encodedIV = Base64.encodeToString(cipher.getIV(), Base64.DEFAULT);
            
            long expiryTime = System.currentTimeMillis() + (expiresIn * 1000);
            
            prefs.edit()
                .putString(KEY_ACCESS_TOKEN, encodedToken)
                .putString(KEY_IV, encodedIV)
                .putLong(KEY_EXPIRY_TIME, expiryTime)
                .apply();
                
        } catch (Exception e) {
            Log.e(TAG, "Error saving token", e);
        }
    }
    
    public String getToken() {
        if (!isTokenValid()) {
            return null;
        }
        
        try {
            String encodedToken = prefs.getString(KEY_ACCESS_TOKEN, null);
            String encodedIV = prefs.getString(KEY_IV, null);
            
            if (encodedToken == null || encodedIV == null) {
                return null;
            }
            
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
            keyStore.load(null);
            SecretKey secretKey = (SecretKey) keyStore.getKey(KEY_ALIAS, null);
            
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(128, Base64.decode(encodedIV, Base64.DEFAULT));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
            
            byte[] decryptedBytes = cipher.doFinal(Base64.decode(encodedToken, Base64.DEFAULT));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
            
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving token", e);
            return null;
        }
    }
    
    public boolean isTokenValid() {
        long expiryTime = prefs.getLong(KEY_EXPIRY_TIME, 0);
        return System.currentTimeMillis() < expiryTime;
    }
    
    public void clearToken() {
        prefs.edit()
            .remove(KEY_ACCESS_TOKEN)
            .remove(KEY_IV)
            .remove(KEY_EXPIRY_TIME)
            .apply();
    }
}