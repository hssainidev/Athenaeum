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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
        final User user1 = user;
        usersDB.collection("Users")
                .document(uid).set(user)
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

    public User getUser(String uid) {

        DocumentReference userRef = usersDB.collection("Users").document(uid);
        Task<DocumentSnapshot> userFind = userRef.get();
        try {
            userFind.wait();
        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
        while (!userFind.isComplete()) {
        }
        return (User) userFind.getResult().toObject(User.class);
    }
    public void deleteBookFromUser(String uid, String isbn) {
        User user=getUser(uid);
        ArrayList<String> books=user.getBooks();
        books.remove(isbn);
        user.setBooks(books);
        addUser(user, uid);
    }

    public ArrayList<AthenaeumProfile> searchUsers(String query) {
        query = query.toLowerCase();
        ArrayList<AthenaeumProfile> profiles = new ArrayList<>();
        Task<QuerySnapshot> userQuery = usersDB.collection("Users").get();
        while (!userQuery.isComplete()) {
        }
        for(QueryDocumentSnapshot document : userQuery.getResult()) {
            User user = document.toObject(User.class);
            AthenaeumProfile profile = user.getProfile();
            try {
                Log.d("username", profile.getUsername());
                if(profile.getUsername().toLowerCase().contains(query) || profile.getName().toLowerCase().contains(query)){
                    profiles.add(profile);
                }
            } catch (Exception e) {
                Log.d("Error", String.valueOf(e));
            }
        }

        return profiles;
    }

    public boolean doesUsernameExist(String username) {
        Task<QuerySnapshot> userQuery = usersDB.collection("Users").get();
        while (!userQuery.isComplete()) {
        }
        for(QueryDocumentSnapshot document : userQuery.getResult()) {
            User user = document.toObject(User.class);
            AthenaeumProfile profile = user.getProfile();
            try {
                if (profile.getUsername().equals(username)) {
                    return true;
                }
            } catch (Exception e) {
                Log.d("Error", String.valueOf(e));
            }
        }
        return false;
    }

}
