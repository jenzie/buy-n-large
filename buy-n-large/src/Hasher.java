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
import java.util.concurrent.Semaphore;

/**
 * Hasher takes in a plaintext password and converts it into a byte array after using SHA-256 to hash. The byte array
 * is then converted into a hexadecimal string, and stored into the shared dictionary of hashed to plaintext passwords.
 */
public class Hasher implements Runnable {

    // Hidden data members.
    private String plaintextData;
    private String hashData;
    private ConcurrentHashMap<String, String> dictionaryOfPasswords;
    private Semaphore hashesDone;

    /**
     * Constructor.
     * @param plaintextData password to hash.
     * @param dictionaryOfPasswords collection of hashed passwords, along with their corresponding plaintext values.
     * @param hashesDone semaphore that keeps track of the number of passwords that were hashed.
     *                   Used along with the semaphore printPermits to allow/block threads from printing results.
     *                   Used to maintain order of task execution.
     */
    public Hasher(String plaintextData, ConcurrentHashMap<String, String> dictionaryOfPasswords, Semaphore hashesDone) {
        this.plaintextData = plaintextData;
        this.dictionaryOfPasswords = dictionaryOfPasswords;
        this.hashesDone = hashesDone;
    }

    /**
     * Gets the hashed value of the plaintext password as a byte array.
     * @param data plaintext password to hash.
     * @return hashed password as a byte array.
     */
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

    /**
     * Converts a hashed byte array into a hexadecimal string.
     * @param data the byte array to convert into a hexadecimal string.
     * @return a hexadecimal string representing the byte array given.
     */
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
     *
     * When this thread is started, it hashes the plaintext password using SHA-256 and adds it into the dictionary
     * of hashed values to plaintext values.
     */
    @Override
    public void run() {
        this.hashData = byteArrayToHexString(getHash(this.plaintextData));
        this.dictionaryOfPasswords.put(this.hashData, this.plaintextData);

        // Update the semaphore by releasing one, which increases the number of available permits by one.
        this.hashesDone.release();
    }
}