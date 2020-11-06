package com.example.athenaeum;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class BookDB {
    private FirebaseFirestore booksDB = FirebaseFirestore.getInstance();;
    private CollectionReference booksRef = booksDB.collection("Books");
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

    /**
     * Search for a book based on a keyword in the description
     * TODO: check w/ tests
     * @param keyword search keyword provided
     * @return ArrayList<Book>
     */
    public ArrayList<Book> keywordSearch(String keyword){
        final ArrayList<Book> matchingBooks = new ArrayList<Book>();
        booksRef.whereEqualTo("status","available")
                .whereGreaterThanOrEqualTo("Description",keyword)
                .whereLessThanOrEqualTo("Description",keyword + '\uf8ff')
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            Book book = documentSnapshot.toObject(Book.class);
                            matchingBooks.add(book);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"No books found");
                    }
                });
        return matchingBooks;
    }
}
