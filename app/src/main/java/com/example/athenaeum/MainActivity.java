package com.example.athenaeum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //testing for ProfileActivity
        AthenaeumProfile profile = new AthenaeumProfile("testusrname", "testname", "testpass", "testnum", "testmail");

        Intent intent = new Intent(this, ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("profile", profile);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}