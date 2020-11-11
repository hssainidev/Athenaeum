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

public class LoginActivity extends AppCompatActivity {
    EditText pass;
    EditText email;
    UserAuth auth;
    public Boolean verify;
    public String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.loginEmail);
        pass = findViewById(R.id.loginPassword);
        auth = new UserAuth();

        final Button signupButton = findViewById(R.id.buttonSignup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        final Button loginButton = findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                verify = true;
                // Add checking for correct data in login/signup

                if (verify) {
                    Task<AuthResult> authentication = auth.signIn(email.getText().toString(), pass.getText().toString());
                    authentication
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        uid = task.getResult().getUser().getUid();
                                        Bundle args = new Bundle();
                                        args.putSerializable("UID", uid);
                                        Log.d("Yes", "worked");
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("UID", uid);
                                        startActivity(intent);
                                    } else {
                                        verify = false;
                                        Log.d("No", "Failed");
                                    }
                                }
                            });
                }
            }
        });
    }
}
