package com.example.athenaeum;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity{

    String title;
    TextView username;
    TextView name;
    TextView phoneNum;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.profile_toolbar);

        AthenaeumProfile profile = (AthenaeumProfile) getIntent().getExtras().getSerializable("profile");

        title = profile.getUsername() + "'s Profile";

        toolbar.setTitle(title);

        username = findViewById(R.id.profile_username);
        name = findViewById(R.id.profile_name);
        phoneNum = findViewById(R.id.profile_phoneNum);
        email = findViewById(R.id.profile_email);

        username.setText(profile.getUsername());
        name.setText(profile.getName());
        phoneNum.setText(profile.getPhoneNum());
        email.setText(profile.getEmail());

    }
}