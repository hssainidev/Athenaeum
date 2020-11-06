package com.example.athenaeum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignupActivity extends AppCompatActivity {

    // Initialize edit texts.
    EditText username;
    EditText email;
    EditText password;
    EditText passwordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize listener for signup button
        final Button signupButton = findViewById(R.id.buttonSignup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Check that all the data is valid on the client side.

                // Retrieve the username text and make sure that something was entered
                username = findViewById(R.id.signupUsername);
                String usernameText = username.getText().toString();
                if (usernameText.length() == 0) {
                    username.setError("You must enter a username.");
                    return;
                }

                // Retrieve the email text and make sure that something was entered
                // that has at least an @ symbol and a . symbol.
                email = findViewById(R.id.signupEmail);
                String emailText = email.getText().toString();
                if (emailText.length() == 0 || emailText.indexOf('@') == -1 || emailText.indexOf('.') == -1) {
                    email.setError("You must enter a valid email.");
                    return;
                }

                // Retrieve the password texts and make sure that something was entered.
                // Also check and make sure that the passwords match.
                password = findViewById(R.id.signupPassword);
                String passwordText = password.getText().toString();
                passwordConfirm = findViewById(R.id.signupPasswordConfirm);
                String passwordConfirmText = passwordConfirm.getText().toString();
                if (passwordText.length() == 0 || passwordConfirmText.length() == 0 || !passwordText.equals(passwordConfirmText)) {
                    passwordConfirm.setError("Your passwords must match.");
                    return;
                }

                // Initialize the authorization of a new user.
                UserAuth auth = new UserAuth();
                final UserDB db = new UserDB();
                AthenaeumProfile profile = new AthenaeumProfile(usernameText, passwordText, emailText);
                Task<AuthResult> authentication = auth.addUser(profile);
                final User user= new User(profile);
                authentication.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // If authorizing the new user was successful, add the user to the database,
                            // then clear the signup activity and navigate to the home screen.
                            db.addUser(user, task.getResult().getUser().getUid());

                            username.setText("");
                            email.setText("");
                            password.setText("");
                            passwordConfirm.setText("");

                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // Otherwise, display an error.
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
