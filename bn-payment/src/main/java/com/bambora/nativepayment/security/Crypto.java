/*
 * Copyright (c) 2016 Bambora ( http://bambora.com/ )
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.bambora.nativepayment.security;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * Class that handles encryption and decryption.
 */
public class Crypto {

    public static final String RSA_ALGORITHM =             "RSA";
    public static final String RSA_ECB_PKCS1_ALGORITHM =   "RSA/ECB/PKCS1Padding";
    public static final String AES_ALGORITHM =             "AES";
    public static final String AES_CBC_PKCS5_ALGORITHM =   "AES/CBC/PKCS5Padding";
    public static final String UTF8_CHARSET =              "UTF-8";
    public static final int RSA_KEY_SIZE =                 4096;
    public static final int AES_KEY_SIZE_128 =             128;

    /**
     * Encrypts the data byte[] with given {@link PublicKey} using RSA algorithm.
     *
     * @param data  Byte array with data to be encrypted
     * @param key   A {@link PublicKey} to use for encryption
     * @return byte[] with encrypted data
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public byte[] RSAEncrypt(byte[] data, PublicKey key) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException,
            BadPaddingException, IllegalBlockSizeException {
        if (data != null) {
            Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        }
        return null;
    }

    /**
     * Encrypts byte[] data with given {@link PrivateKey} using RSA algorithm.
     * @param data  Byte array with data to be encrypted
     * @param key   A {@link PrivateKey} to use for decryption
     * @return byte[] with decrypted data
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws UnsupportedEncodingException
     */
    public byte[] RSADecrypt(byte[] data, PrivateKey key) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, UnsupportedEncodingException {
        if (data != null) {
            Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        }
        return null;
    }

    /**
     * Encrypts a {@link String} with given {@link SecretKey} using AES algorithm.
     * <p>{@link #AESEncrypt(String, SecretKey)} is called for encryption, but the data is then
     * base 64 encoded to a String.</p>
     * @param data  A {@link String} to be encrypted
     * @param key   A {@link SecretKey} to use for encryption
     * @return Base64 encoded String of AES encrypted data
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidAlgorithmParameterException
     */
    public String AESEncryptAndEncode(String data, SecretKey key) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException,
            BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        byte[] encrypted = AESEncrypt(data, key);
        return Base64.encodeToString(encrypted, Base64.NO_WRAP);
    }

    /**
     * Encrypts a {@link String} with the {@link SecretKey} using AES encryption
     * <p>UTF-8 charset is used to decode the data to bytes which are then encrypted. CBC mode and
     * PKCS5 padding is used. The key itself is used as initial vector.</p>
     *
     * @param data  A {@link String} to encrypt
     * @param key   A {@link SecretKey} to use for encryption
     * @return Byte array with encrypted data
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidAlgorithmParameterException
     */
    public byte[] AESEncrypt(String data, SecretKey key) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException,
            BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        if (data != null) {
            AlgorithmParameterSpec iVpec = new IvParameterSpec(key.getEncoded());
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, iVpec);
            return cipher.doFinal(data.getBytes(UTF8_CHARSET));
        }
        return null;
    }

    /**
     * Decrypts a byte array with the {@link SecretKey} using AES algorithm.
     * <p>CBC mode and PKCS5 padding is used. The key itself is used as initial vector.</p>
     *
     * @param data  Byte array with encrypted data
     * @param key   {@link SecretKey} to use for decryption
     * @return Byte array with decrypted data
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidAlgorithmParameterException
     */
    public byte[] AESDecrypt(byte[] data, SecretKey key) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException,
            BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        if (data != null) {
            AlgorithmParameterSpec iVpec = new IvParameterSpec(key.getEncoded());
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, iVpec);
            return cipher.doFinal(data);
        }
        return null;
    }

    /**
     * Generates a random key pair with private and public keys for RSA encryption
     *
     * @return A new {@link KeyPair}
     * @throws NoSuchAlgorithmException
     */
    public KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyGenerator.initialize(RSA_KEY_SIZE);
        return keyGenerator.generateKeyPair();
    }

    /**
     * Generates a random private symmetric key for AES encryption
     *
     * @return A new {@link SecretKey}
     * @throws NoSuchAlgorithmException
     */
    public SecretKey generateRandomAES128() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
        keyGenerator.init(AES_KEY_SIZE_128);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }
}
