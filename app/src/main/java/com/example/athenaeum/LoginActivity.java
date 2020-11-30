/*
 * LoginActivity
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;

import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

/**
 * This Activity is the starting activity for the app and allows a user to log in with their authenticated account.
 * It implements client-side validation for the form.
 */
public class LoginActivity extends AppCompatActivity {
    private UserAuth auth;
    private Boolean verify;
    private String uid;

    // Initialize edit texts.
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = new UserAuth();

        // Initialize listener for signup button, which directs user to signup activity.
        final Button signupButton = findViewById(R.id.buttonSignup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        // Initialize listener for login button.
        final Button loginButton = findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Check that all the data is valid on the client side.

                // Retrieve the email text and make sure that something was entered
                // that has at least an @ symbol and a . symbol.
                email = findViewById(R.id.loginEmail);
                String emailText = email.getText().toString();
                if (emailText.length() == 0 || emailText.indexOf("@") == -1 || emailText.indexOf(".") == -1) {
                    email.setError("You must enter a valid email.");
                    return;
                }

                // Retrieve the password text and make sure that something was entered.
                password = findViewById(R.id.loginPassword);
                String passwordText = password.getText().toString();
                if (passwordText.length() == 0) {
                    password.setError("You must enter a password.");
                    return;
                }

                verify = true;
                // Add checking for correct data in login/signup

                if (verify) {
                    Task<AuthResult> authentication = auth.signIn(emailText, passwordText);
                    authentication
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // If the user information matched a user in the database,
                                        // reset the login activity information and then navigate to the home screen.
                                        uid = task.getResult().getUser().getUid();
                                        Bundle args = new Bundle();
                                        args.putSerializable("UID", uid);
                                        Log.d("Yes", "worked");
                                        email.setText("");
                                        password.setText("");
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("UID", uid);
                                        startActivity(intent);
                                    } else {
                                        verify = false;
                                        // Otherwise, there was an issue, so display an error.
                                        Toast.makeText(LoginActivity.this, "Invalid email/password.",
                                                Toast.LENGTH_SHORT).show();
                                        Log.d("No", "Failed");
                                    }
                                }
                            });
                }
            }
        });
    }
}
