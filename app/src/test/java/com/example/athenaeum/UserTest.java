package com.example.athenaeum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * Test class for the user class.
 */
public class UserTest {

    /**
     * Make sure the constructor creates a usder correctly
     */
    @Test
    void testCreateUser() {
        AthenaeumProfile profile = new AthenaeumProfile("Test", "TestName", "tester", "7801234567", "testing@gmail.com");
        User user = new User(profile);
        Assertions.assertEquals(user.getProfile(), profile);
    }

    /**
     * Make sure the overloaded profile class also works
     */
    @Test
    void testOverloadedProfile() {
        AthenaeumProfile profile = new AthenaeumProfile("Test", "tester", "testing@gmail.com");
        User user = new User(profile);
        Assertions.assertEquals(user.getProfile(), profile);
    }

    /**
     * Test whether a new profile can be set which would be needed when editing information
     */
    @Test
    void testSetProfile() {
        AthenaeumProfile wrongProfile = new AthenaeumProfile("Test1", "testing", "wrong@gmail.com");
        AthenaeumProfile profile = new AthenaeumProfile("Test", "tester", "testing@gmail.com");
        User user = new User(wrongProfile);
        user.setProfile(profile);
        Assertions.assertEquals(user.getProfile(), profile);

    }

    /**
     * Make sure books can be added correctly
     */
    @Test
    void testAddBook() {
        AthenaeumProfile profile = new AthenaeumProfile("Test", "tester", "testing@gmail.com");
        User user = new User(profile);
        Book book = new Book("123456789", "Author", "Name");
        user.addBook(book.getISBN());
        Assertions.assertEquals(user.getBooks().size(), 1);
    }

    /**
     * Tests if a user can be assigned a new ArrayList of books
     */
    @Test
    void testSetBooks() {
        AthenaeumProfile profile = new AthenaeumProfile("Test", "tester", "testing@gmail.com");
        User user = new User(profile);
        Book book = new Book("123456789", "Author", "Name");
        ArrayList<String> books = new ArrayList<>();
        books.add(book.getISBN());
        user.setBooks(books);
        Assertions.assertEquals(user.getBooks().size(), 1);
        books.remove(book.getISBN());
        user.setBooks(books);
        Assertions.assertEquals(user.getBooks().size(), 0);
    }
}
