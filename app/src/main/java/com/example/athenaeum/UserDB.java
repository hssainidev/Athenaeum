/*
 * UserDB
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

import android.util.Log;

import androidx.annotation.NonNull;

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

import static android.content.ContentValues.TAG;

/**
 * This class represents a connection to the firestore database.
 * It contains methods intended for dealing with the Users collection.
 */
public class UserDB {

    private ArrayList<User> users;
    private FirebaseFirestore usersDB;

    /**
     * This constructs a UserDB object by connecting to the firestore database.
     */
    public UserDB() {
        usersDB = FirebaseFirestore.getInstance();
    }

    /**
     * This returns the Users collection from the connected database.
     *
     * @return Return the Users collection.
     */
    public CollectionReference getCollection() {
        return usersDB.collection("Users");
    }

    /**
     * This adds or updates an entry in the Users collection.
     * If the UID provided is already in use, it updates the associated entry.
     * Otherwise, a new entry is created.
     *
     * @param user The User object to be used in the update/creation of the entry.
     * @param uid The UID used to get a path to an existing entry or create a new one.
     */
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

    /**
     * This returns a new User object representing an entry in the Users collection with the
     * supplied UID.
     *
     * @param uid The UID associated to the required entry.
     * @return Returns the desired User object.
     */
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

    /**
     * This removes a book referenced by the supplied ISBN from the user entry referenced
     * by the supplied UID.
     *
     * @param uid The UID of the user entry the book should be removed from.
     * @param isbn The ISBN of the book to be removed.
     */
    public void deleteBookFromUser(String uid, String isbn) {
        User user=getUser(uid);
        ArrayList<String> books=user.getBooks();
        books.remove(isbn);
        user.setBooks(books);
        addUser(user, uid);
    }

    /**
     * Returns a list of AthenaeumProfiles that contain the query string in their name or
     * username fields.
     *
     * @param query The string to be used in the search.
     * @return Returns the list of matching AthenaeumProfiles.
     */
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

    /**
     * Checks whether a given username already exists in the collection.
     *
     * @param username The username to check for.
     * @return Returns true if the username already exists, false if not.
     */
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
