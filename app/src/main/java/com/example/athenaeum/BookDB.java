package com.example.athenaeum;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class BookDB {
    private FirebaseFirestore booksDB = FirebaseFirestore.getInstance();
    private ArrayList<Book> books;

    public BookDB() {
        booksDB = FirebaseFirestore.getInstance();
    }

    public CollectionReference getCollection() {
        return booksDB.collection("Books");
    }

    public void addBook(Book book) {
        final Book book1 = book;
        booksDB.collection("Books")
                .document(book.getISBN()).set(book)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Book added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Couldn't add " + book1.getISBN());
                    }
                });
    }

    public ArrayList<Book> searchBooks(String keyword) {
        final ArrayList<Book> bookSearch = new ArrayList<>();
        final String keyword1 = keyword;
        Task<QuerySnapshot> bookQuery = booksDB.collection("Books").get();
        while (!bookQuery.isComplete()) {
        }
        for (QueryDocumentSnapshot document : bookQuery.getResult()) {
            Book book = document.toObject(Book.class);
            try {
                Log.d("book", book.getDescription());
                String fullDescription = book.getDescription()+book.getAuthor()+book.getISBN()+book.getTitle();
                if (fullDescription.toLowerCase().contains(keyword1.toLowerCase())) {
                    bookSearch.add(document.toObject(Book.class));
                }
            } catch (Exception e) {
                Log.d("Error", "failed search");
            }
        }
        return bookSearch;
    }

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

    public ArrayList<Book> getRequestedBooks(String uid) {
        final ArrayList<Book> bookRequest = new ArrayList<>();
        final String uid1=uid;
        Task<QuerySnapshot> bookQuery = booksDB.collection("Books").get();
        while (!bookQuery.isComplete()) {
        }
        for (QueryDocumentSnapshot document : bookQuery.getResult()) {
            Book book = document.toObject(Book.class);
            try {
                Log.d("book", book.getDescription());
                if (book.getRequesters().contains(uid1) && book.getStatus().equals("Requested")) {
                    bookRequest.add(document.toObject(Book.class));
                }
            } catch (Exception e) {
                Log.d("Error", "failed getting requested books");
            }
        }
        return bookRequest;
    }

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
        return (Book) bookFind.getResult().toObject(Book.class);
    }

    public void deleteBook(String ISBN) {
        DocumentReference bookRef = booksDB.collection("Books").document(ISBN);
        Task<Void> bookDelete = bookRef.delete();
        while (!bookDelete.isComplete()) {
        }
    }
    public void requestBook(Book book, String uid) {
        book.request(uid);
        this.addBook(book);
    }
    public void acceptRequest(Book book, String uid) {
        book.accept(uid);
        this.addBook(book);
    }
    public void confirmBorrow(Book book, String uid) {
        book.confirm(uid);
        this.addBook(book);
    }
}
