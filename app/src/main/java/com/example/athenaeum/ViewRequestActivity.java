/*
 * ViewRequestActivity
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
 */

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

/**
 * This class shows a list of the users requesting the logged in user's book.
 */
public class ViewRequestActivity extends AppCompatActivity{
    ListView requesterList;
    ArrayAdapter<String> requesterAdapter;
    UserDB userDB = new UserDB();
    ArrayList<String> requesterDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set up view and ListView for displaying requesters.
        setContentView(R.layout.activity_view_requests);
        requesterList = findViewById(R.id.requester_list);
        final BookDB booksDB = new BookDB();

        //get Book and UID from Intent
        Intent i = getIntent();
        final Book book = (Book) getIntent().getSerializableExtra("BOOK");
        final String uid = getIntent().getExtras().getString("UID");

        //populate ArrayList with usernames of requesting users
        final ArrayList<String> arrayList = book.getRequesters();
        for(String requesterUid : arrayList){
            requesterDataList.add(userDB.getUser(requesterUid).getProfile().getUsername());
        }

        //display usernames on screen
        requesterAdapter = new ArrayAdapter<>(this,R.layout.view_requests_list, requesterDataList);
        requesterList.setAdapter(requesterAdapter);

        //listener for interaction with the requester list
        requesterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final String entryUid = arrayList.get(position);;

                //set up button for accepting requests
                final Button accept_button = (Button) findViewById(R.id.accept);
                accept_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        booksDB.acceptRequest(book, entryUid);
                        setResult(2);
                        finish();
                    }
                });

                //set up button for declining requests
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
