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

package com.bambora.nativepayment.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * This is a class containing utils related to hashing.
 *
 * Created by oskarhenriksson on 13/10/15.
 */
public class HashUtils {

    private static final String UTF8_ENCODING = "UTF-8";
    private static final  char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    private HashUtils() {}

    /**
     * A helper method for generating a hmac of an value given a certain key.
     *
     * @param type The type of hash algorithm to be used
     * @param value The value to be hmac'd
     * @param key The key used to generate the hmac
     * @return A hmac of the value with the given key using the inputted algorithm
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String generateHmac(String type, String value, String key) throws GeneralSecurityException {

        SecretKeySpec secret = new SecretKeySpec(key.getBytes(Charset.forName(UTF8_ENCODING)), type);
        Mac mac = Mac.getInstance(type);
        mac.init(secret);
        byte[] bytes = mac.doFinal(value.getBytes(Charset.forName(UTF8_ENCODING)));
        return bytesToHex(bytes);
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * This is a method for generating the proof of work used by the user registration.
     * This method combines uuid, path, timestamp and a hash in order to create an hash.
     * The hash must have a number of leading zeroes defined in the input param powDifficulty.
     * The hash algorithm used is SHA-1
     *
     * Important to note here is that this method can take some time an should not be executed
     * in the thread that handles GUI.
     *
     *
     * @param uuid The uuid of the registered user
     * @param path The path of the request
     * @param powDifficulty The number of leading zeroes the pow has to have
     * @return A string representing the generated pow
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String generatePow(String uuid, String path, int powDifficulty) throws Exception {
        int salt = 0;
        int timeStamp = (int) (System.currentTimeMillis() / 1000L);
        String stringToHash = timeStamp
                + ":" + uuid
                + ":" + path
                + ":" + "1234"; // Extra two bytes at the end i.e a salt

        byte [] bytesToHash = stringToHash.getBytes(Charset.forName(UTF8_ENCODING));
        int stringLength = stringToHash.length();
        int bytesToHashLength = bytesToHash.length;

        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(bytesToHash, 0, stringLength);
        byte[] sha1hash = md.digest();

        while(!validPow(sha1hash, powDifficulty)) {
            salt++;
            bytesToHash[bytesToHashLength-1] = (byte) (59 + salt%68);
            bytesToHash[bytesToHashLength-2] = (byte) (59 + (salt >>> 8)%68);
            bytesToHash[bytesToHashLength-3] = (byte) (127 - (salt >>> 16)%68);
            bytesToHash[bytesToHashLength-4] = (byte) (127 - (salt >>> 24)%68);

            md.update(bytesToHash, 0, stringToHash.length());
            sha1hash = md.digest();
        }

        String hash = bytesToHex(sha1hash);
        return  new String(bytesToHash, UTF8_ENCODING)
                + ":" + hash;
    }

    /**
     * A method for checking if the POW generated is valid. A valid hash has a number of
     * leading zeroes specified in the input parameter powDifficulty
     *
     * @param hash The hash to validate
     * @param powDifficulty An int saying how many leading zeroes the hash has to have in order
     *                      to be seen as a valid hash
     * @return A boolean indicating whether the hash inputted is valid of not.
     */
    private static boolean validPow(byte [] hash, int powDifficulty) {

        for(int i = 0; i < powDifficulty; i++) {
            byte byteToCheck = hash[i/8];
            byte mask = (byte) (-128 >> i%8);
            int maskByteToCheck = byteToCheck & mask;

            if(maskByteToCheck != 0) {
                return false;
            }

        }

        return true;
    }
}
