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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignupActivity extends AppCompatActivity {
    UserDB userDB;
    EditText pass;
    EditText email;
    EditText user;
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
        user = findViewById(R.id.signupUsername);
        passConfirm = findViewById(R.id.signupPasswordConfirm);

        final Button signupButton = findViewById(R.id.buttonSignup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                verify = true;
                String password = "";
                String emailString = "";
                String username = "";
                // Do checking for data entered in the text boxes.
                if (!(pass.getText().toString().equals(passConfirm.getText().toString()))) {
                    verify = false;
                    pass.setError("Passwords must match");
                    passConfirm.setError("Passwords must match");
                } else {
                    password = pass.getText().toString();
                }
                emailString = email.getText().toString();
                username = user.getText().toString();
                if (verify) {
                    final AthenaeumProfile profile = new AthenaeumProfile(username, password, emailString);

                    Task<AuthResult> authentication = auth.addUser(profile);
                    authentication
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        User user = new User(profile);
                                        userDB.addUser(user, task.getResult().getUser().getUid());
                                        Log.d("tag", "successfully added account");
                                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Log.d("Fail", task.getException().toString());
                                    }
                                }
                            });
                }
            }
        });
    }
}
