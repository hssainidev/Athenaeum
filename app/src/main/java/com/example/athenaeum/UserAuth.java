package com.example.athenaeum;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class UserAuth {
    private FirebaseAuth userAuth;
    public UserAuth() {
        userAuth=FirebaseAuth.getInstance();
    }
    public Task<AuthResult> addUser(AthenaeumProfile profile) {
        return userAuth.createUserWithEmailAndPassword(profile.getEmail(), profile.getPassword());
    }
    public Task<AuthResult> signIn(AthenaeumProfile profile) {
        Task<AuthResult> task =userAuth.signInWithEmailAndPassword(profile.getEmail(), profile.getPassword());
        return userAuth.signInWithEmailAndPassword(profile.getEmail(), profile.getPassword());

    }
}
