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

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance ("SHA-256");
        String password = "12345";
        byte[] data = password.getBytes ("UTF-8");
        for (int i = 0; i < 100000; i++) {
            md.update (data);
            data = md.digest();
        }
        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<data.length;i++) {
            //hexString.append(Integer.toHexString(0xFF & data[i]));
            String hex = Integer.toHexString(0xFF & data[i]);
            if (hex.length() == 1) {
                // could use a for loop, but we're only dealing with a single byte
                hexString.append('0');
            }
            hexString.append(hex);
        }
        System.out.println(hexString.toString());
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            //hexChars[j * 2] = hexArray[v >>> 4];
            //hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}