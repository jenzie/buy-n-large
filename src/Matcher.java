/**
 author: Jenny Zhen
 date: 02.20.14
 language: Java
 file: Matcher.java
 assignment: BuyNLarge
 http://www.cs.rit.edu/~wrc/courses/csci251/projects/1/
 */

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Matcher takes in the name of a user and its hashed password, and searches the dictionary of potential passwords
 * for an entry that contains the same hashed password, so that it can find the plaintext password for the given user.
 */
public class Matcher implements Runnable {

    // Hidden data members.
    private String user;
    private String password;
    private ConcurrentHashMap<String, String> dictionaryOfPasswords;
    private LinkedList<Matcher> matcherList;

    /**
     * Constructor.
     * @param user name of the user that Matcher is trying to match the password of.
     * @param password hashed value of the user's password.
     * @param dictionaryOfPasswords collection of hashed passwords, along with their corresponding plaintext values.
     */
    public Matcher(String user, String password, ConcurrentHashMap<String,
            String> dictionaryOfPasswords, LinkedList<Matcher> matcherList){
        this.user = user;
        this.password = password;
        this.dictionaryOfPasswords = dictionaryOfPasswords;
        this.matcherList = matcherList;
    }

    /**
     * When a thread calls .start(), it runs the .run() method.
     *
     * When this thread is started, it checks the dictionary of potential passwords for a key that matched the
     * given hashed value.
     *
     * If it does, it prints out the name of the given user and the matching plaintext password
     * that it found.
     *
     * If it doesn't, it gives its resources to other threads also trying to match, and then quits when .run() returns.
     */
    @Override
    public void run() {
        while (true) {
            if (this.dictionaryOfPasswords.containsKey(this.password))
                break;
            else
                // Since no matches were found, let other threads use the time that this thread has to check.
                Thread.yield();
        }

        while (matcherList.peek() != this)
            Thread.yield();

        System.out.println(this.user + " " + this.dictionaryOfPasswords.get(this.password));
        matcherList.pop();
    }
}
