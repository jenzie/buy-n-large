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

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class PasswordCrack {
    public static void main(String[] args) {
        // Check command line arguments.
        if(args.length != 2) {
            System.err.println("Usage: java PasswordCrack dictionary db");
            System.exit(0);
        }

        PasswordCrack passwordCrack = new PasswordCrack();

        ArrayList<String> dictionary = passwordCrack.parseDictionary(args[0]);
        HashMap<String, String> database = passwordCrack.parseDatabase(args[1]);

        String password = "12345";
        byte[] data = getHash(password);
        String hexString = byteArrayToHexString(data);

        System.out.println(hexString);
        System.out.println("ca6323767829ee7075655b283e14f2da9353006474779127b595e6d991fffe3f");
    }

    private ArrayList<String> parseDictionary(String dictFile) {
        try {
            ArrayList<String> dictionary = new ArrayList<String>();
            BufferedReader reader = new BufferedReader(new FileReader(dictFile));
            String line = reader.readLine();

            while (line != null) {
                dictionary.add(line);
                line = reader.readLine();
            }
            return dictionary;
        } catch (FileNotFoundException e) {
            System.err.println(
                    "Usage: java PasswordCrack dictionary db\n" +
                    "Error: File " + dictFile + " could not be read.");
            return null;
        } catch (IOException e) {
            System.err.println(
                    "Usage: java PasswordCrack dictionary db\n" +
                            "Error: File " + dictFile + " is empty.");
            return null;
        }
    }

    private HashMap<String, String> parseDatabase(String dbFile) {
        try {
            HashMap<String, String> database = new HashMap<String, String>();
            BufferedReader reader = new BufferedReader(new FileReader(dbFile));
            String line = reader.readLine();

            while (line != null) {
                String[] entry = line.split("\\s+");

                // Check for a user, password pair.
                if (entry.length == 2) {
                    database.put(entry[0], entry[1]);
                } else if (entry.length == 1) {
                    database.put(entry[0], null);
                }

                line = reader.readLine();
            }
            return database;
        } catch (FileNotFoundException e) {
            System.err.println(
                    "Usage: java PasswordCrack dictionary db\n" +
                            "Error: File " + dbFile + " could not be read.");
            return null;
        } catch (IOException e) {
            System.err.println(
                    "Usage: java PasswordCrack dictionary db\n" +
                            "Error: File " + dbFile + " is empty.");
            return null;
        }
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