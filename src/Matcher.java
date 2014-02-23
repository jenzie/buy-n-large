/**
 author: Jenny Zhen
 date: 02.20.14
 language: Java
 file: Matcher.java
 assignment: BuyNLarge
 http://www.cs.rit.edu/~wrc/courses/csci251/projects/1/
 */

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Matcher implements Runnable {
    private String user;
    private String password;
    private ConcurrentHashMap<String, String> dictionaryOfPasswords;

    public Matcher(String user, String password, ConcurrentHashMap<String, String> dictionaryOfPasswords){
        this.user = user;
        this.password = password;
        this.dictionaryOfPasswords = dictionaryOfPasswords;
    }

    @Override
    public void run() {
        while (true) {
            if (this.dictionaryOfPasswords.containsKey(this.password)) {
                System.out.println(this.user + " " + this.dictionaryOfPasswords.get(this.password));
                break;
            }
            else {
                // Since no matches were found, let other threads use the time that this thread has to check.
                Thread.yield();
            }
        }
    }

    /*
    public String getMatch(String hash, HashMap<String, String> database) {
        if (database.containsValue(hash)) {
            for (Map.Entry<String, String> entry : database.entrySet()) {
                if(hash.equals(entry.getValue()))
                    return entry.getKey();
            }
        }
        return null;
    }*/
}
