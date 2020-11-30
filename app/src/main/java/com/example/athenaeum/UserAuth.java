/*
 * UserAuth
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
 *
 * Referenced for User Authentication
 * https://firebase.google.com/docs/auth
 * by Google Developers
 * Published November 17, 2020
 * Licensed under the Apache 2.0 license.
 */

package com.example.athenaeum;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * This class handles the Firebase authentication of users.
 */
public class UserAuth {
    private FirebaseAuth userAuth;

    /**
     * Constructor to set the userAuth as the current FirebaseAuth instance.
     */
    public UserAuth() {
        userAuth = FirebaseAuth.getInstance();
    }

    /**
     * Adds a user using the given profile's email and password
     * @param profile An Athenaeum profile containing a new user's email and password.
     * @return Returns the Task that contains the attempted sign up.
     */
    public Task<AuthResult> addUser(AthenaeumProfile profile) {
        return userAuth.createUserWithEmailAndPassword(profile.getEmail(), profile.getPassword());
    }

    /**
     * Signs in to an existing user using the given email and password
     * @param email The given email
     * @param password The given password
     * @return Returns the Task containing the attempted sign in.
     */
    public Task<AuthResult> signIn(String email, String password) {
        return userAuth.signInWithEmailAndPassword(email, password);
    }

    /**
     * Retrieves the UserAuth from Firebase.
     * @return The FirebaseUser of the current user.
     */
    public FirebaseUser getUser() {
        return userAuth.getCurrentUser();
    }

    /**
     * Signs out of the current FirebaseUser.
     */
    public void signOut() {
        userAuth.signOut();
    }
}
