/*
 * SignupActivity
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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.Objects;

/**
 * This Activity allows users to sign up for a new account.
 * It implements client-side validation for the form, and implements server-side validation of the username being unique.
 */
public class SignupActivity extends AppCompatActivity {
    private UserDB userDB;
    private EditText pass;
    private EditText email;
    private EditText username;
    private UserAuth auth;
    private EditText passConfirm;
    private Boolean verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        userDB = new UserDB();
        auth = new UserAuth();
        pass = findViewById(R.id.signupPassword);
        email = findViewById(R.id.signupEmail);
        username = findViewById(R.id.signupUsername);
        passConfirm = findViewById(R.id.signupPasswordConfirm);

        // Initialize listener for signup button
        final Button signupButton = findViewById(R.id.buttonSignup);
        signupButton.setOnClickListener(v -> {
            // Check that all the data is valid on the client side.

            // Retrieve the username text and make sure that something was entered
            String usernameText = username.getText().toString();
            if (usernameText.length() == 0) {
                username.setError("You must enter a username.");
                return;
            }

            // Retrieve the email text and make sure that something was entered
            // that has at least an @ symbol and a . symbol.
            String emailText = email.getText().toString();
            if (emailText.length() == 0 || emailText.indexOf('@') == -1 || emailText.indexOf('.') == -1) {
                email.setError("You must enter a valid email.");
                return;
            }

            // Retrieve the password texts and make sure that something was entered.
            // Also check and make sure that the passwords match.
            String passwordText = pass.getText().toString();
            String passwordConfirmText = passConfirm.getText().toString();
            if (passwordText.length() == 0 || passwordConfirmText.length() == 0 || !passwordText.equals(passwordConfirmText)) {
                passConfirm.setError("Your passwords must match.");
                return;
            } else if (passwordText.length() < 6) {
                passConfirm.setError("Your password must be at least 6 characters long.");
                return;
            }

            if (userDB.doesUsernameExist(usernameText)) {
                username.setError("That username is already in use.");
                return;
            }

            verify = true;

            // Initialize the authorization of a new user.
            final AthenaeumProfile profile = new AthenaeumProfile(usernameText, passwordText, emailText);

            Task<AuthResult> authentication = auth.addUser(profile);
            authentication
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // If authorizing the new user was successful, add the user to the database,
                            // then clear the signup activity and navigate to the home screen.
                            User user = new User(profile);
                            userDB.addUser(user, Objects.requireNonNull(task.getResult().getUser()).getUid());
                            Log.d("tag", "successfully added account");

                            username.setText("");
                            email.setText("");
                            pass.setText("");
                            passConfirm.setText("");

                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            // Otherwise, display an error.
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Log.d("Fail", Objects.requireNonNull(task.getException()).toString());
                        }
                    });
        });
    }
}
