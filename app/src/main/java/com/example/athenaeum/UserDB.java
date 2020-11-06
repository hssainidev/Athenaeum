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

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class UserDB {
    private ArrayList<User> users;

    private FirebaseFirestore usersDB;

    public UserDB() {
        usersDB = FirebaseFirestore.getInstance();
    }

    public CollectionReference getCollection() {
        return usersDB.collection("Users");
    }

    public void addUser(User user, String uid) {
        final User user1=user;
        HashMap<String, User> data=new HashMap<>();
        data.put("UserObject", user);
        usersDB.collection("Users")
                .document(uid).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Added User successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Could not add user");
                    }
                });
    }
    public Task<DocumentSnapshot> getUser(String uid) {
        DocumentReference userRef = this.getCollection().document(uid);
        return userRef.get();
    }

}
