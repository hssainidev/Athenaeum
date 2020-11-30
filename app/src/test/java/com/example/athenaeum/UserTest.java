package com.example.athenaeum;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class UserTest {

    @Test
    void testCreateUser() {
        AthenaeumProfile profile = new AthenaeumProfile("Test", "TestName", "tester", "7801234567", "testing@gmail.com");
        User user = new User(profile);
        assertEquals(user.getProfile(), profile);
    }

    @Test
    void testOverloadedProfile() {
        AthenaeumProfile profile = new AthenaeumProfile("Test", "tester", "testing@gmail.com");
        User user= new User(profile);
        assertEquals(user.getProfile(), profile);
    }

    @Test
    void testSetProfile() {
        AthenaeumProfile wrongProfile= new AthenaeumProfile("Test1", "testing", "wrong@gmail.com");
        AthenaeumProfile profile = new AthenaeumProfile("Test", "tester", "testing@gmail.com");
        User user =new User(wrongProfile);
        user.setProfile(profile);
        assertEquals(user.getProfile(), profile);

    }

    @Test
    void testAddBook() {
        AthenaeumProfile profile = new AthenaeumProfile("Test", "tester", "testing@gmail.com");
        User user= new User(profile);
        Book book = new Book("123456789", "Author", "Name");
        user.addBook(book.getISBN());
        assertEquals(user.getBooks().size(),1);
    }

    @Test
    void testSetBooks() {
        AthenaeumProfile profile = new AthenaeumProfile("Test", "tester", "testing@gmail.com");
        User user= new User(profile);
        Book book = new Book("123456789", "Author", "Name");
        ArrayList<String> books=new ArrayList<>();
        books.add(book.getISBN());
        user.setBooks(books);
        assertEquals(user.getBooks().size(),1);
        books.remove(book.getISBN());
        user.setBooks(books);
        assertEquals(user.getBooks().size(),0);
    }
}
