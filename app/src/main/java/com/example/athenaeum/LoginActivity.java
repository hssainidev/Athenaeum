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

public class LoginActivity extends AppCompatActivity {

    // Initialize edit texts.
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

                // Initialize the authorization of the given user credentials.
                UserAuth auth = new UserAuth();
                Task<AuthResult> authentication = auth.signIn(emailText, passwordText);
                authentication.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // If the user information matched a user in the database,
                            // reset the login activity information and then navigate to the home screen.
                            email.setText("");
                            password.setText("");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // Otherwise, there was an issue, so display an error.
                            Toast.makeText(LoginActivity.this, "Invalid email/password.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
