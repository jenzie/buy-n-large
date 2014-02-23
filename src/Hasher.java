/**
 author: Jenny Zhen
 date: 02.20.14
 language: Java
 file: Hasher.java
 assignment: BuyNLarge
 http://www.cs.rit.edu/~wrc/courses/csci251/projects/1/
 */

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;

public class Hasher implements Runnable {
    private String plaintextData;
    private String hashData;
    private ConcurrentHashMap<String, String> dictionaryOfPasswords;

    public Hasher(String plaintextData, ConcurrentHashMap<String, String> dictionaryOfPasswords) {
        this.plaintextData = plaintextData;
        this.dictionaryOfPasswords = dictionaryOfPasswords;
    }

    private byte[] getHash(String data) {
        MessageDigest messageDigest = null;
        byte[] result = new byte[0];

        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            result = data.getBytes("UTF-8");

            // Hash 100k times.
            for (int i = 0; i < 100000; i++) {
                if (messageDigest != null) {
                    messageDigest.update(result);
                    result = messageDigest.digest();
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;
    }

    private String byteArrayToHexString(byte[] data) {
        StringBuilder hexString = new StringBuilder();
        for (byte piece : data) {
            // Convert each byte to hex, while ANDing with 0xFF to get the least significant byte (masking).
            String hex = Integer.toHexString(0xFF & piece);

            // Check each byte for missing leading zeroes.
            if (hex.length() == 1)
                hexString.append('0');

            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * When a thread calls .start(), it runs the .run() method.
     */
    @Override
    public void run() {
        this.hashData = byteArrayToHexString(getHash(this.plaintextData));
        this.dictionaryOfPasswords.put(this.hashData, this.plaintextData);
    }
}