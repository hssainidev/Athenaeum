package com.example.athenaeum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewRequestActivity extends AppCompatActivity{
    ListView requesterList;
    ArrayAdapter<String> requesterAdapter;
    UserDB userDB = new UserDB();
    ArrayList<String> requesterDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);
        requesterList = findViewById(R.id.requester_list);
        final BookDB booksDB = new BookDB();

        Intent i = getIntent();
        final Book book = (Book) getIntent().getSerializableExtra("BOOK");
        final String uid = getIntent().getExtras().getString("UID");

        final ArrayList<String> arrayList = book.getRequesters();
        for(String requesterUid : arrayList){
            requesterDataList.add(userDB.getUser(requesterUid).getProfile().getUsername());
        }

        requesterAdapter = new ArrayAdapter<>(this,R.layout.view_requests_list, requesterDataList);
        requesterList.setAdapter(requesterAdapter);

        requesterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final String entryUid = arrayList.get(position);;
                final Button accept_button = (Button) findViewById(R.id.accept);
                accept_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        booksDB.acceptRequest(book, entryUid);
                        setResult(2);
                        finish();
                    }
                });

                final Button decline_button = (Button) findViewById(R.id.decline);
                decline_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        booksDB.declineRequest(book, entryUid);
                        requesterDataList.remove(requesterList.getItemAtPosition(position));
                        requesterAdapter.notifyDataSetChanged();
                        setResult(2);
                    }
                });
            }
        });
    }
}
