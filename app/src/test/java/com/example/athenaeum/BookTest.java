package com.example.athenaeum;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the book class.
 */
public class BookTest {

    /**
     * Make sure the constructor doesn't return null
     */
    @Test
    void testCreateBook() {
        Book book = null;
        book = new Book("123456789", "Author", "Book");
        assertNotNull(book);
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
        assertNotEquals(owner, borrower);
        book.setOwnerUID(owner);
        book.request(borrower);
        assertTrue(book.getRequesters().contains(borrower));
        book.accept(borrower);
        assertTrue(book.getRequesters().contains(borrower));
        book.giveBook(owner);
        assertEquals(book.getOwnerUID(), book.getBorrowerUID());
        assertEquals(book.getStatus(), "Borrowed");
        book.confirm(borrower);
        assertEquals(book.getBorrowerUID(), borrower);
        book.returnBook();
        assertEquals(book.getStatus(), "Available");
        assertEquals(book.getRequesters().size(), 0);
        book.receiveReturn();
        assertEquals(book.getStatus(), "Available");
        assertNull(book.getBorrowerUID());
    }
}
