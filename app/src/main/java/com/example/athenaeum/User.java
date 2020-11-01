package com.example.athenaeum;

import java.util.ArrayList;

public class User{
    private ArrayList<Book> books;
    private AthenaeumProfile profile;
    public User(AthenaeumProfile profile) {
        this.profile=profile;
        books=new ArrayList<>();
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    public AthenaeumProfile getProfile() {
        return profile;
    }

    public void setProfile(AthenaeumProfile profile) {
        this.profile = profile;
    }

    public void addBook(Book book) {
        books.add(book);
    }
}
