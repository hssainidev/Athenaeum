/*
 * BookDB
 *
 * November 30 2020
 *
 * Copyright 2020 Natalie Iwaniuk, Harpreet Saini, Jack Gray, Jorge Marquez Peralta, Ramana Vasanthan, Sree Nidhi Thanneeru
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.athenaeum;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * This class represents a connection to the firestore database.
 * It contains methods intended for dealing with the Books collection.
 */
public class BookDB {
    private final FirebaseFirestore booksDB;
    private ArrayList<Book> books;

    /**
     * This constructs a BookDB object by connecting to the firestore database.
     */
    public BookDB() {
        booksDB = FirebaseFirestore.getInstance();
    }

    /**
     * This returns the Books collection from the connected database.
     *
     * @return Return the Books collection.
     */
    public CollectionReference getCollection() {
        return booksDB.collection("Books");
    }

    /**
     * This adds or updates an entry in the Books collection.
     * If the book ISBN provided is already in use, it updates the associated entry.
     * Otherwise, a new entry is created.
     *
     * @param book The Book object to be added or used to update the collection.
     */
    public void addBook(Book book) {
        final Book book1 = book;
        booksDB.collection("Books")
                .document(book.getISBN()).set(book)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Book added successfully"))
                .addOnFailureListener(e -> Log.w(TAG, "Couldn't add " + book1.getISBN()));
    }

    /**
     * Returns a list of Books that contain the keyword string in their description, author,
     * ISBN, or Title fields.
     *
     * @param keyword The string to be used in the search.
     * @return Returns the list of matching Books.
     */
    public ArrayList<Book> searchBooks(String keyword) {
        final ArrayList<Book> bookSearch = new ArrayList<>();
        Task<QuerySnapshot> bookQuery = booksDB.collection("Books").get();
        while (!bookQuery.isComplete()) {
        }
        for (QueryDocumentSnapshot document : bookQuery.getResult()) {
            Book book = document.toObject(Book.class);
            try {
                if (book.getStatus().equals("Accepted") || book.getStatus().equals("Borrowed")) { continue; }
                Log.d("book", book.getDescription());
                String fullDescription = book.getDescription() + book.getAuthor() + book.getISBN()+book.getTitle();
                if (fullDescription.toLowerCase().contains(keyword.toLowerCase())) {
                    bookSearch.add(document.toObject(Book.class));
                }
            } catch (Exception e) {
                Log.d("Error", "failed search");
            }
        }
        return bookSearch;
    }

    /**
     * Returns a list of books that have been borrowed by the user connected to the supplied
     * borrowerUid.
     * @param borrowerUid The UID string of the borrowing user.
     * @return Returns the list of matching Books.
     */
    public ArrayList<Book> getBorrowedBooks(String borrowerUid) {
        final ArrayList<Book> borrowedBooks = new ArrayList<>();
        Task<QuerySnapshot> bookQuery = booksDB.collection("Books").get();
        while (!bookQuery.isComplete()) {
        }
        for (QueryDocumentSnapshot document : bookQuery.getResult()) {
            Book book = document.toObject(Book.class);
            try {
                Log.d("book", book.getDescription());
                if (book.getBorrowerUID().equals(borrowerUid) && !book.getOwnerUID().equals(borrowerUid)) {
                    borrowedBooks.add(document.toObject(Book.class));
                }
            } catch (Exception e) {
                Log.d("Error", "failed finding borrowed books");
            }
        }
            return borrowedBooks;
    }

    /**
     * Returns a list of books that have been requested by the user connected to the supplied
     * uid.
     * @param uid The UID string of the requesting user.
     * @return Returns the list of matching Books.
     */
    public ArrayList<Book> getRequestedBooks(String uid) {
        final ArrayList<Book> bookRequest = new ArrayList<>();
        Task<QuerySnapshot> bookQuery = booksDB.collection("Books").get();
        while (!bookQuery.isComplete()) {
        }
        for (QueryDocumentSnapshot document : bookQuery.getResult()) {
            Book book = document.toObject(Book.class);
            try {
                Log.d("book", book.getDescription());
                if (book.getRequesters().contains(uid) && book.getStatus().equals("Requested")) {
                    bookRequest.add(document.toObject(Book.class));
                }
            } catch (Exception e) {
                Log.d("Error", "failed getting requested books");
            }
        }
        return bookRequest;
    }

    /**
     * Returns a list of books that have been requested by the user connected to the supplied
     * uid where the request has been accepted.
     *
     * @param uid The UID string of the requesting user.
     * @return Returns the list of matching Books.
     */
    public ArrayList<Book> getAcceptedBooks(String uid) {
        final ArrayList<Book> bookAccept = new ArrayList<>();
        Task<QuerySnapshot> bookQuery = booksDB.collection("Books").get();
        while (!bookQuery.isComplete()) {
        }
        for (QueryDocumentSnapshot document : bookQuery.getResult()) {
            Book book = document.toObject(Book.class);
            try {
                Log.d("book", book.getDescription());
                boolean borrowConfirm = (book.getStatus().equals("Borrowed") && book.getBorrowerUID().equals(book.getOwnerUID()));
                if (book.getRequesters().contains(uid) && (borrowConfirm || book.getStatus().equals("Accepted"))) {
                    bookAccept.add(document.toObject(Book.class));
                }
            } catch (Exception e) {
                Log.d("Error", "failed getting accepted books");
            }
        }
        return bookAccept;
    }

    /**
     * Returns a Book from the collection based on its ISBN.
     *
     * @param ISBN The ISBN string to be used when querying the database.
     * @return Returns the Book.
     */
    public Book getBook(String ISBN) {
        DocumentReference bookRef = booksDB.collection("Books").document(ISBN);
        Task<DocumentSnapshot> bookFind = bookRef.get();
        try {
            bookFind.wait();
        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
        while (!bookFind.isComplete()) {
        }
        return bookFind.getResult().toObject(Book.class);
    }

    /**
     * Deletes a Book from the collection based on its ISBN
     *
     * @param ISBN The ISBN string connected to the Book to be deleted.
     */
    public void deleteBook(String ISBN) {
        DocumentReference bookRef = booksDB.collection("Books").document(ISBN);
        Task<Void> bookDelete = bookRef.delete();
        while (!bookDelete.isComplete()) {
        }
    }

    /**
     * This calls the request method on the supplied Book and updates its entry in the
     * collection.
     *
     * @param book The Book to be updated.
     * @param uid The UID string of the requesting user.
     */
    public void requestBook(Book book, String uid) {
        book.request(uid);
        this.addBook(book);
    }

    /**
     * This calls the accept method on the supplied Book and updates its entry in the
     * collection.
     *
     * @param book The Book to be updated.
     * @param uid The UID of the accepted requesting user.
     */
    public void acceptRequest(Book book, String uid) {
        book.accept(uid);
        this.addBook(book);
    }

    /**
     * This calls the removeRequester method on the supplied Book, reevaluates its availability,
     * and updates its entry in the collection.
     *
     * @param book The Book to be updated.
     * @param uid The UID of the accepted requesting user.
     */
    public void declineRequest(Book book, String uid) {
        book.removeRequester(uid);
        if (book.getRequesters().size() == 0) {
            book.setStatus("Available");
        }
        this.addBook(book);
    }

    /**
     * This calls the confirm method on the supplied Book and updates its entry in the
     * collection.
     * @param book The Book to be updated.
     * @param uid The UID of the confirmed borrowing user.
     */
    public void confirmBorrow(Book book, String uid) {
        book.confirm(uid);
        this.addBook(book);
    }

    /**
     * This calls the giveBook method on the supplied Book and updates its entry in the
     * collection.
     * @param book The Book to be updated.
     * @param uid The UID of the borrowing user.
     */
    public void giveBookUpdate(Book book, String uid) {
        book.giveBook(uid);
        this.addBook(book);
    }

    /**
     * This calls the returnBook method on the supplied Book and updates its entry in the
     * collection.
     *
     * @param book The Book to be updated.
     */
    public void returnToOwner(Book book) {
        book.returnBook();
        this.addBook(book);
    }

    /**
     * This calls the receiveReturn method on the supplied Book and updates its entry in the
     * collection.
     *
     * @param book The Book to be updated.
     */
    public void confirmReturn(Book book) {
        book.receiveReturn();
        this.addBook(book);
    }

    /**
     * This calls the setPhoto method on the supplied Book and updates its entry in the
     * collection.
     *
     * @param book The Book to be updated.
     */
    public void addPhoto(Book book) {
        book.setPhoto();
        this.addBook(book);
    }

    /**
     * This calls the removePhoto method on the supplied Book and deletes its attached photo
     * from the database, then updates its entry.
     *
     * @param book The Book to be updated.
     */
    public void removePhoto(Book book) {
        book.removePhoto();
        FirebaseStorage storage= FirebaseStorage.getInstance();
        StorageReference storageReference=storage.getReference();
        StorageReference ref=storageReference.child(book.getISBN()+".jpg");
        Task<Void> task=ref.delete();
        while (!task.isComplete()) {}

        this.addBook(book);
    }
}