package com.example.athenaeum;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class UserDB {
    private ArrayList<User> users;
    private FirebaseFirestore usersDB;
    public UserDB() {
        usersDB=FirebaseFirestore.getInstance();
    }
    public void addUser(User user) {
        final User user1=user;
        usersDB.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, user1.getProfile().getUsername()+" added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Couldn't add "+user1.getProfile().getUsername());
                    }
                });
    }

}
