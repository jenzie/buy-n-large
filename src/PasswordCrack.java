/**
 author: Jenny Zhen
 date: 02.17.14
 language: Java
 file: PasswordCrack.java
 assignment: BuyNLarge
 http://www.cs.rit.edu/~wrc/courses/csci251/projects/1/
 sources:
 Google search: "java byte array to hex string"
 http://stackoverflow.com/questions/332079/in-java-how-do-i-convert-a-byte-array-to-a-string-of-hex-digits-while-keeping-l
 http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java
 */

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordCrack {

    public static void main(String[] args) {
        String password = "12345";
        byte[] data = getHash(password);
        String hexString = byteArrayToHexString(data);

        System.out.println(hexString);
        System.out.println("ca6323767829ee7075655b283e14f2da9353006474779127b595e6d991fffe3f");
    }
    
    private static byte[] getHash(String data) {
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

    private static String byteArrayToHexString(byte[] data) {
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
}