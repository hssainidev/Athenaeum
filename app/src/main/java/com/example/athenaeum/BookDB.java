package com.example.athenaeum;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class BookDB {
    private FirebaseFirestore booksDB;
    private ArrayList<Book> books;

    public BookDB() {
        booksDB=FirebaseFirestore.getInstance();
    }
    public void addBook(Book book) {
        final Book book1=book;
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
                        Log.w(TAG, "Couldn't add "+book1.getISBN());
                    }
                });
    }
    public ArrayList<Book> searchBooks(String keyword) {
        final ArrayList<Book> bookSearch=new ArrayList<>();
        final String keyword1=keyword;
        Task<QuerySnapshot> bookQuery=booksDB.collection("Books").get();
        while(!bookQuery.isComplete()){}
        for (QueryDocumentSnapshot document: bookQuery.getResult()) {
            Book book=document.toObject(Book.class);
            if (book.getDescription().contains(keyword1)) {
                bookSearch.add(document.toObject(Book.class));
            }
        }
        return bookSearch;
    }


}
