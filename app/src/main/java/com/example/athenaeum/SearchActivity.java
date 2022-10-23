/*
 * SearchActivity
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * An Activity that allows a user to search for available and requested books by keyword
 * found in the title, author, or isbn.
 */
public class SearchActivity extends AppCompatActivity {
    private EditText keyword;
    private ListView listView;
    private ArrayAdapter<Book> adapter;
    private ArrayList<Book> list;
    private BookDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize the keyword value and the book database.
        keyword = findViewById(R.id.keyword);
        final Button search = findViewById(R.id.search);
        final String uid = getIntent().getExtras().getString("UID");
        db = new BookDB();

        // Initialize the listener for the search button
        search.setOnClickListener(view -> {
            // Retrieve the keyword from the EditText and search the books using it.
            String keyword_string = keyword.getText().toString();
            list = db.searchBooks(keyword_string);

            // Initialize the ListView with the retrieved books.
            listView = findViewById(R.id.result_list);
            adapter = new CustomBookList(SearchActivity.this, list);
            listView.setAdapter(adapter);

            // Initialize the listener for selecting a book.
            listView.setOnItemClickListener((parent, view1, position, id) -> {
                Book book = (Book) parent.getAdapter().getItem(position);
                Intent intent = new Intent(SearchActivity.this, BookInfoActivity.class);
                intent.putExtra("BOOK", book);
                intent.putExtra("UID", uid);
                // Start the activity for a result so that we can update the list if a book is changed.
                startActivityForResult(intent, 1);
            });

        });


    }

    /**
     * This function retrieves the result from various activities and updates the list.
     * @param requestCode Contains the request code sent to the other activity.
     * @param resultCode Contains the result code from the other activity, if applicable.
     * @param data Contains the data from the other activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            //Refresh the book list display
            list.clear();
            String keyword_string = keyword.getText().toString();
            list = db.searchBooks(keyword_string);
            adapter.notifyDataSetChanged();
        }
    }
}
