package com.example.athenaeum;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class BookDB {
    private FirebaseFirestore booksDB;
    private ArrayList<Book> books;

    public BookDB() {
        booksDB = FirebaseFirestore.getInstance();
    }

    public CollectionReference getCollection() {
        return booksDB.collection("Books");
    }

    public void addBook(Book book) {
        final Book book1=book;
        booksDB.collection("Books")
                .add(book)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, book1.getISBN()+" added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Couldn't add "+book1.getISBN());
                    }
                });
    }
}
