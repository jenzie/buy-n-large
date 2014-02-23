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

public class Matcher {
    public String getMatch(String hash, HashMap<String, String> database) {
        if (database.containsValue(hash)) {
            for (Map.Entry<String, String> entry : database.entrySet()) {
                if(hash.equals(entry.getValue()))
                    return entry.getKey();
            }
        }
        return null;
    }
}
