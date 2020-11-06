package com.example.athenaeum;

import android.os.Bundle;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    User user;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //initialize toolbar
        Toolbar toolbar = findViewById(R.id.profile_toolbar);

        user = savedInstanceState.getSerializable("user");



        toolbar.setTitle("");

    }

}
