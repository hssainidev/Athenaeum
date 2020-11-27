package com.example.athenaeum;

import java.util.ArrayList;

/**
 * This is a class of user objects that have an array of books and
 * a connected profile object.
 */

public class User {
    private ArrayList<String> books;
    private AthenaeumProfile profile;

    /**
     * This constructs a User object from a given profile, creating an empty array for
     * the book objects.
     *
     * @param profile This is the connected profile of the user.
     */
    public User(AthenaeumProfile profile) {
        this.profile = profile;
        books = new ArrayList<>();
    }

    public User() {
    }

    /**
     * This returns the user's array of books.
     *
     * @return Return the array of books owned by the user.
     */
    public ArrayList<String> getBooks() {
        return books;
    }

    /**
     * This sets a user's books
     *
     * @param books This is the new array of books.
     */
    public void setBooks(ArrayList<String> books) {
        this.books = books;
    }

    /**
     * This adds a new book to the user's array of books.
     *
     * @param book This is the new book.
     */
    public void addBook(String book) {
        books.add(book);
    }

    /**
     * This returns the user's connected profile.
     *
     * @return Return the profile of the user.
     */
    public AthenaeumProfile getProfile() {
        return profile;
    }

    /**
     * This sets a user's profile.
     *
     * @param profile This is the new profile.
     */
    public void setProfile(AthenaeumProfile profile) {
        this.profile = profile;
    }
}
