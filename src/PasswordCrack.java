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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PasswordCrack {
    public static void main(String[] args) {
        // Check command line arguments.
        if(args.length != 2) {
            System.err.println("Usage: java PasswordCrack dictionary db");
            System.exit(0);
        }

        String dictFile = args[0];
        String dbFile = args[1];

        /*
        PasswordCrack passwordCrack = new PasswordCrack();

        ArrayList<String> dictionary = passwordCrack.parseDictionary(args[0]);
        HashMap<String, String> database = passwordCrack.parseDatabase(args[1]);

        Hasher hashMaker = new Hasher();

        Hashtable<String, String> hashDictionary = hashMaker.plaintextToHash(dictionary);
        for(Map.Entry<String, String> entry :hashDictionary.entrySet())
            System.out.println(entry.getKey() + " " + entry.getValue());

        Matcher matchMaker = new Matcher();
        */

        ConcurrentHashMap<String, String> dictionaryOfPasswords = new ConcurrentHashMap<String, String>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(dbFile));
            String line = reader.readLine();

            while (line != null) {
                String[] entry = line.split("\\s+");

                // Check for a user, password pair.
                if (entry.length == 2) {
                    Thread newThread = new Thread(new Matcher(entry[0], entry[1], dictionaryOfPasswords));
                    newThread.start();
                }

                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.err.println(
                    "Usage: java PasswordCrack dictionary db\n" +
                    "Error: File " + dbFile + " could not be read.");
        } catch (IOException e) {
            System.err.println(
                    "Usage: java PasswordCrack dictionary db\n" +
                    "Error: File " + dbFile + " is empty.");
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(dictFile));
            String line = reader.readLine();

            while (line != null) {
                // Check for a user, password pair.
                if (!line.isEmpty()) {
                    Thread newThread = new Thread(new Hasher(line, dictionaryOfPasswords));
                    newThread.start();
                }

                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.err.println(
                    "Usage: java PasswordCrack dictionary db\n" +
                    "Error: File " + dictFile + " could not be read.");
        } catch (IOException e) {
            System.err.println(
                    "Usage: java PasswordCrack dictionary db\n" +
                    "Error: File " + dictFile + " is empty.");
        }

        /**
         * For each user read in, make a thread. Then, after all users, for each unhashed password read in, make a thread.
         */

        /*
        String password = "12345";
        byte[] data = getHash(password);
        String hexString = byteArrayToHexString(data);

        System.out.println(hexString);
        System.out.println("ca6323767829ee7075655b283e14f2da9353006474779127b595e6d991fffe3f");
        */
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
}