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
import java.util.concurrent.Semaphore;

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
    private Semaphore printPermits;

    /**
     * Constructor.
     * @param user name of the user that Matcher is trying to match the password of.
     * @param password hashed value of the user's password.
     * @param dictionaryOfPasswords collection of hashed passwords, along with their corresponding plaintext values.
     * @param printPermits semaphore that allows/blocks threads from printing users along with the passwords found.
     *                   Used along with the semaphore hashesDone to allow/block threads from printing results.
     *                   Used to maintain order of task execution.
     */
    public Matcher(String user, String password, ConcurrentHashMap<String,
            String> dictionaryOfPasswords, LinkedList<Matcher> matcherList, Semaphore printPermits){
        this.user = user;
        this.password = password;
        this.dictionaryOfPasswords = dictionaryOfPasswords;
        this.matcherList = matcherList;
        this.printPermits = printPermits;
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
            // Break if we have found the password, or if all passwords have been read in and hashed.
            if (this.dictionaryOfPasswords.containsKey(this.password) || this.printPermits.availablePermits() > 0)
                break;
            else
                // Since no matches were found, let other threads use the time that this thread has to check.
                Thread.yield();
        }

        // Wait until we're back at the head of the list before printing, so we maintain task execution order.
        while (matcherList.peek() != this)
            Thread.yield();

        // Print only the users whose passwords have been found.
        if (this.dictionaryOfPasswords.get(this.password) != null)
            System.out.println(this.user + " " + this.dictionaryOfPasswords.get(this.password));

        // Remove this thread from the linked list, since we are completely done with it.
        matcherList.pop();
    }
}
