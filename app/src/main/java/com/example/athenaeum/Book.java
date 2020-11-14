package com.example.athenaeum;

import java.io.File;

/**
 * This is a class of book objects that have an ISBN, author, title, status, and owner,
 * and may also have a description and/or photo.
 */

public class Book {
    private String ISBN;
    private String author;
    private String description;
    private String title;
    private String status;
    private File photo;
    private String ownerUID;

    /**
     * This constructs a Book object from a given ISBM, author, and title
     * @param ISBN
     * This is the ISBN of the book.
     * @param author
     * This is the author of the book.
     * @param title
     * This is the title of the book.
     */
    public Book(String ISBN, String author, String title) {
        this.ISBN = ISBN;
        this.author = author;
        this.title = title;
        this.status="Available";
    }
    public Book(){}

    /**
     * This returns the book's ISBN
     * @return
     * Return the ISBN of the book.
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * This sets a book's ISBN
     * @param ISBN
     * This is the new ISBN.
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * This returns the book's author
     * @return
     * Return the author of the book.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * This sets a book's author
     * @param author
     * This is the new author.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * This returns the book's description
     * @return
     * Return the description of the book.
     */
    public String getDescription() {
        return description;
    }

    /**
     * This sets a book's description
     * @param description
     * This is the new description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * This returns the book's title
     * @return
     * Return the title of the book.
     */
    public String getTitle() {
        return title;
    }

    /**
     * This sets a book's title
     * @param title
     * This is the new title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This returns the book's status
     * @return
     * Return the status of the book.
     */
    public String getStatus() {
        return status;
    }

    /**
     * This sets a book's status
     * @param status
     * This is the new status.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * This returns the book's photo
     * @return
     * Return the photo of the book.
     */
    public File getPhoto() {
        return photo;
    }

    /**
     * This sets a book's photo
     * @param photo
     * This is the new photo.
     */
    public void setPhoto(File photo) {
        this.photo = photo;
    }

    /**
     * This returns the book's owner
     * @return
     * Return the ownerUID of the book.
     */
    public String getOwnerUID() {
        return ownerUID;
    }

    /**
     * This sets a book's owner
     * @param ownerUID
     * This is the new owner.
     */
    public void setOwnerUID(String ownerUID) {
        this.ownerUID = ownerUID;
    }
}
