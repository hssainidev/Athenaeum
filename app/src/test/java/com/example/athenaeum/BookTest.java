package com.example.athenaeum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test class for the book class.
 */
public class BookTest {

    /**
     * Make sure the constructor doesn't return null
     */
    @Test
    void testCreateBook() {
        Book book;
        book = new Book("123456789", "Author", "Book");
        Assertions.assertNotNull(book);
    }

    /**
     * Go through the flow of borrowing and returning a book and make sure the fields
     * are set correctly
     */
    @Test
    void testBorrowAndReturn() {
        Book book = new Book("123456789", "Author", "Book");
        String owner = "1234";
        String borrower = "5678";
        Assertions.assertNotEquals(owner, borrower);
        book.setOwnerUID(owner);
        book.request(borrower);
        Assertions.assertTrue(book.getRequesters().contains(borrower));
        book.accept(borrower);
        Assertions.assertTrue(book.getRequesters().contains(borrower));
        book.giveBook(owner);
        Assertions.assertEquals(book.getOwnerUID(), book.getBorrowerUID());
        Assertions.assertEquals(book.getStatus(), "Borrowed");
        book.confirm(borrower);
        Assertions.assertEquals(book.getBorrowerUID(), borrower);
        book.returnBook();
        Assertions.assertEquals(book.getStatus(), "Available");
        Assertions.assertEquals(book.getRequesters().size(), 0);
        book.receiveReturn();
        Assertions.assertEquals(book.getStatus(), "Available");
        Assertions.assertNull(book.getBorrowerUID());
    }
}
