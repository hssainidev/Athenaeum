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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignupActivity extends AppCompatActivity {
    UserDB userDB;
    EditText pass;
    EditText email;
    EditText username;
    UserAuth auth;
    EditText passConfirm;
    public Boolean verify;

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
        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
                }

                verify = true;

                // Initialize the authorization of a new user.
                if (verify) {
                    final AthenaeumProfile profile = new AthenaeumProfile(usernameText, passwordText, emailText);

                    Task<AuthResult> authentication = auth.addUser(profile);
                    authentication
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // If authorizing the new user was successful, add the user to the database,
                                        // then clear the signup activity and navigate to the home screen.
                                        User user = new User(profile);
                                        userDB.addUser(user, task.getResult().getUser().getUid());
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
                                        Log.d("Fail", task.getException().toString());
                                    }
                                }
                            });
                }
            }
        });
    }
}
